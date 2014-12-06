package info.mrconst.qlient.notifications;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by eugene on 26.09.14.
 */
public class NotificationCenter {

    private static final int NOTIFICATION_MESSAGE = 1;
    private static final String NOTIFICATION_NAME = "notification_name";
    private static final String NOTIFICATION_USER_INFO = "notification_user_info";
    private static final String NOTIFICATION_SENDER = "notification_sender";
    private static final String NOTIFICATION_BLOCKING = "notification_blocking";

    private static NotificationCenter mInstance;

    private NotificationHandler mHandler;

    private ReentrantLock mLock = new ReentrantLock();

    private Map<String, List<InternalNotificationListener>> mListenerMap = new HashMap<String, List<InternalNotificationListener>>();
    private ReferenceQueue<NotificationListener> mObsoleteReferences = new ReferenceQueue<NotificationListener>();
    private HashMap<Message, Condition> mConditions = new HashMap<Message, Condition>();

    private NotificationCenter() {
        mHandler = new NotificationHandler();
    }

    public static void init() {
        if (mInstance == null)
            mInstance = new NotificationCenter();
    }

    public static NotificationCenter defaultCenter() {
        if (mInstance == null)
            init();
        return mInstance;
    }

    public static void sendMessage(String name, Bundle userInfo, Object sender, boolean wait) {
        Bundle bundle = new Bundle();
        bundle.putString(NOTIFICATION_NAME, name);
        if (userInfo != null)
            bundle.putBundle(NOTIFICATION_USER_INFO, userInfo);

        Message message = defaultCenter().mHandler.obtainMessage(NOTIFICATION_MESSAGE, sender);
        message.setData(bundle);

        if (wait && defaultCenter().mHandler.getLooper().getThread() == Thread.currentThread())
            defaultCenter().mHandler.handleMessage(message);
        else {
            Condition condition = defaultCenter().mLock.newCondition();
            bundle.putBoolean(NOTIFICATION_BLOCKING, wait);
            if (wait)
                defaultCenter().mConditions.put(message, condition);
            message.sendToTarget();
            defaultCenter().mLock.lock();
            try {
                if (wait) {
                    condition.await();
                    defaultCenter().mConditions.remove(message);
                }
            }
            catch (InterruptedException e) {

            }
            finally {
                defaultCenter().mLock.unlock();
            }
        }
    }

    public static void sendMessage(String name, Bundle userInfo, Object sender) {
        sendMessage(name, userInfo, sender, false);
    }

    public static void addListener(NotificationListener listener, String name) {
        addListener(listener, name, null);
    }

    public static void addListener(NotificationListener listener, String name, Object sender) {
        addListener(listener, name, sender, null);
    }

    public static void addListener(NotificationListener listener, String name, Handler handler) {
        addListener(listener, name, null, handler);
    }

    public static void addListener(NotificationListener listener, String name, Object sender, Handler handler) {
        InternalNotificationListener internalListener = new InternalNotificationListener();
        internalListener.mListener = new WeakReference<NotificationListener>(listener, defaultCenter().mObsoleteReferences);
        internalListener.mSender = sender;
        internalListener.mHandler = handler;

        synchronized (defaultCenter()) {
            if (!defaultCenter().mListenerMap.containsKey(name)) {
                List<InternalNotificationListener> list = new LinkedList<InternalNotificationListener>();
                defaultCenter().mListenerMap.put(name, list);
            }
            List<InternalNotificationListener> listeners = defaultCenter().mListenerMap.get(name);
            listeners.add(internalListener);

            defaultCenter().clearObsoleteReferences();
        }
    }

    public static void removeListener(NotificationListener listener) {
        defaultCenter()._removeListener(listener);
        defaultCenter().clearObsoleteReferences();
    }

    public static void removeListener(NotificationListener listener, Object sender) {
        defaultCenter()._removeListener(listener, sender);
        defaultCenter().clearObsoleteReferences();
    }

    public static void removeListener(NotificationListener listener, String name) {
        defaultCenter()._removeListener(listener, name);
        defaultCenter().clearObsoleteReferences();
    }

    public static void removeListener(NotificationListener listener, String name, Object sender) {
        defaultCenter()._removeListener(listener, name, sender);
        defaultCenter().clearObsoleteReferences();
    }

    private synchronized void _removeListener(NotificationListener listener) {
        for (String name : new HashSet<String>(defaultCenter().mListenerMap.keySet()))
            _removeListener(listener, name);
    }

    private synchronized void _removeListener(NotificationListener listener, Object sender) {
        for (String name : new HashSet<String>(defaultCenter().mListenerMap.keySet()))
            _removeListener(listener, name, sender);
    }

    private synchronized void _removeListener(NotificationListener listener, String name) {
        _removeListener(listener, name, null);
    }

    private synchronized void _removeListener(NotificationListener listener, String name, Object sender) {
        List<InternalNotificationListener> listeners = mListenerMap.get(name);
        if (listeners == null)
            return;

        Iterator<InternalNotificationListener> iterator = listeners.iterator();
        while (iterator.hasNext()) {
            InternalNotificationListener internalNotificationListener = iterator.next();
            if (internalNotificationListener.mListener.get() == listener && (internalNotificationListener.mSender == sender || sender == null))
                iterator.remove();
        }

        if (listeners.isEmpty())
            mListenerMap.remove(name);
    }

    private synchronized void clearObsoleteReferences() {
        Reference<? extends NotificationListener> reference = mObsoleteReferences.poll();
        while (reference != null) {
            _removeListener(reference.get());
            reference = mObsoleteReferences.poll();
        }
    }

    private static class NotificationHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            defaultCenter().clearObsoleteReferences();

            if (msg.what == NOTIFICATION_MESSAGE) {
                Bundle data = msg.getData();
                String name = data.getString(NOTIFICATION_NAME);
                Bundle userInfo = data.getBundle(NOTIFICATION_USER_INFO);
                Object sender = msg.obj;
                List<InternalNotificationListener> listeners = defaultCenter().mListenerMap.get(name);
                if (listeners == null)
                    return;

                final Notification notification = new Notification(name, userInfo, sender);

//                Log.d("NotificationCenter", "broadcasting message " + name + " with userInfo " + userInfo + " from " + sender);

                synchronized (NotificationCenter.defaultCenter()) {
                    for (InternalNotificationListener internalListener : new LinkedList<InternalNotificationListener>(listeners)) {
                        if (internalListener.mSender == sender || internalListener.mSender == null) {
                            final NotificationListener listener = internalListener.mListener.get();
                            if (listener == null)
                                continue;
//                            Log.d("NotificationCenter", "sent message to " + listener);
                            Runnable notify = new Runnable() {
                                @Override
                                public void run() {
                                    listener.onNotification(notification);
                                }
                            };
                            if (internalListener.mHandler == null)
                                notify.run();
                            else
                                internalListener.mHandler.post(notify);
                        }
                    }
                }
                defaultCenter().mLock.lock();
                try {
                    if (msg.getData().getBoolean(NOTIFICATION_BLOCKING, false))
                        defaultCenter().mConditions.get(msg).signalAll();
                }
                finally {
                    defaultCenter().mLock.unlock();
                }
            }
        }
    }

    private static class InternalNotificationListener {
        public WeakReference<NotificationListener> mListener;
        public Object mSender;
        public Handler mHandler;
    }
}
