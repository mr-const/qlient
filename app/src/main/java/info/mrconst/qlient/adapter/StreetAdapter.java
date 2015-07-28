package info.mrconst.qlient.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import info.mrconst.qlient.BR;
import info.mrconst.qlient.R;
import info.mrconst.qlient.data.BaseFeed;
import info.mrconst.qlient.data.StreetFeed;
import info.mrconst.qlient.model.Street;
import info.mrconst.qlient.notifications.Notification;
import info.mrconst.qlient.notifications.NotificationCenter;
import info.mrconst.qlient.notifications.NotificationListener;

public class StreetAdapter extends RecyclerView.Adapter<StreetAdapter.ViewHolder>
        implements NotificationListener {

    protected Context mCtx;
    protected LayoutInflater mInflater;
    protected BaseFeed mDataSource;

    public StreetAdapter(Context context, StreetFeed feed) {
        mCtx = context;
        mDataSource = feed;
        mInflater = LayoutInflater.from(mCtx);
        NotificationCenter.addListener(this, BaseFeed.NOTIFICATION_DATASET_UPDATE, mDataSource);
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

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        // For databinding to work we inflate our view using DataBindingUtil and
        // give ViewDataBinding to holder, so it'll be able to bind data when needed.
        return new ViewHolder(DataBindingUtil.inflate(mInflater, R.layout.street_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Street item = (Street)mDataSource.get(position);
        // Performing actual databinding
        holder.getBinding().setVariable(BR.street, item);
        holder.getBinding().executePendingBindings();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ViewDataBinding mBinding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public ViewDataBinding getBinding() {
            return mBinding;
        }
    }
}
