package info.mrconst.qlient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import info.mrconst.qlient.data.BaseFeed;
import info.mrconst.qlient.notifications.Notification;
import info.mrconst.qlient.notifications.NotificationCenter;
import info.mrconst.qlient.notifications.NotificationListener;

public abstract class BaseFeedAdapter extends BaseAdapter
        implements NotificationListener {

    protected Context mCtx;
    protected LayoutInflater mInflater;
    protected BaseFeed mDataSource;

    public BaseFeedAdapter(Context ctx, BaseFeed dataSource) {
        mCtx = ctx;
        mDataSource = dataSource;
        mInflater = LayoutInflater.from(mCtx);

        NotificationCenter.addListener(this, BaseFeed.NOTIFICATION_DATASET_UPDATE, mDataSource);
    }

    public BaseFeed getDataSource() {
        return mDataSource;
    }

    @Override
    public int getCount() {
        return mDataSource.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean isEmpty() {
        return mDataSource.size() == 0;
    }

    @Override
    public synchronized void onNotification(Notification notification) {
        if (BaseFeed.NOTIFICATION_DATASET_UPDATE.equals(notification.getName())) {
            notifyDataSetChanged();
        }
    }
}
