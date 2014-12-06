package info.mrconst.qlient.data;

import android.content.Context;

import java.util.ArrayList;

import info.mrconst.qlient.notifications.NotificationCenter;

public abstract class BaseFeed<T> {

    public static final String NOTIFICATION_DATASET_UPDATE = "dataset_update";

    Context mCtx;
    protected ArrayList<T> mObjects = new ArrayList<>();

    public BaseFeed(Context ctx, Class<T> tClass) {
        mCtx = ctx;
    }

    public int size() {
        return mObjects.size();
    }

    public T get(int i) {
        return mObjects.get(i);
    }
}
