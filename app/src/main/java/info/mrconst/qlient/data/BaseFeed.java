package info.mrconst.qlient.data;

import android.content.Context;

import java.util.ArrayList;

public abstract class BaseFeed<T> {

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
