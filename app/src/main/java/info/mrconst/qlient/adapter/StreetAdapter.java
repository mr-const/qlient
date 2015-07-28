package info.mrconst.qlient.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.mrconst.qlient.BR;
import info.mrconst.qlient.R;
import info.mrconst.qlient.data.StreetFeed;
import info.mrconst.qlient.model.Street;

public class StreetAdapter extends BaseFeedAdapter {

    public StreetAdapter(Context context, StreetFeed feed) {
        super(context, feed);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        // For databinding to work we inflate our view using DataBindingUtil and
        // give ViewDataBinding to holder, so it'll be able to bind data when needed.
        return new ViewHolder(DataBindingUtil.inflate(mInflater, R.layout.street_row, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder baseViewHolder, int position) {
        ViewHolder holder = (ViewHolder) baseViewHolder;

        final Street item = (Street)mDataSource.get(position);
        // Performing actual databinding
        holder.getBinding().setVariable(BR.street, item);
        holder.getBinding().executePendingBindings();
    }

    private static class ViewHolder extends BaseViewHolder {

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
