package info.mrconst.qlient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import info.mrconst.qlient.data.BaseFeed;

public abstract class BaseFeedAdapter extends BaseAdapter {

    protected Context mCtx;
    protected LayoutInflater mInflater;
    protected BaseFeed mDataSource;

    public BaseFeedAdapter(Context ctx, BaseFeed dataSource) {
        mCtx = ctx;
        mDataSource = dataSource;
        mInflater = LayoutInflater.from(mCtx);
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
}
