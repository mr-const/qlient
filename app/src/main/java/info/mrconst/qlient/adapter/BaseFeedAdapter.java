package info.mrconst.qlient.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import info.mrconst.qlient.data.BaseFeed;
import info.mrconst.qlient.notifications.Notification;
import info.mrconst.qlient.notifications.NotificationCenter;
import info.mrconst.qlient.notifications.NotificationListener;

public abstract class BaseFeedAdapter extends RecyclerView.Adapter<BaseFeedAdapter.BaseViewHolder>
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
    public int getItemCount() {
        return mDataSource.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public synchronized void onNotification(Notification notification) {
        if (BaseFeed.NOTIFICATION_DATASET_UPDATE.equals(notification.getName())) {
            notifyDataSetChanged();
        }
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder implements
            NotificationListener {

        public BaseViewHolder(View itemView) {
            super(itemView);
        }

        protected void onRecycle() {
        }

        public void setEmptyFooterVisible(boolean visible) {
        }

        public void update() {
        }

        @Override
        public void onNotification(Notification notification) {
        }
    }
}
