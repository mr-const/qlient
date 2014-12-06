package info.mrconst.qlient.notifications;

import android.os.Bundle;

/**
 * Created by eugene on 26.09.14.
 */
public class Notification {
    private String mName;
    private Bundle mUserInfo;
    private Object mSender;

    public Notification(String name, Bundle userInfo, Object sender) {
        mName = name;
        mUserInfo = userInfo;
        mSender = sender;
    }

    public String getName() {
        return mName;
    }

    public Bundle getUserInfo() {
        return mUserInfo;
    }

    public Object getSender() {
        return mSender;
    }
}
