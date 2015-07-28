package info.mrconst.qlient.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import info.mrconst.qlient.R;
import info.mrconst.qlient.data.StreetFeed;
import info.mrconst.qlient.model.Street;

public class StreetAdapter extends BaseFeedAdapter {

    public StreetAdapter(Context context, StreetFeed feed) {
        super(context, feed);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        return new ViewHolder(mInflater.inflate(R.layout.street_row, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder baseViewHolder, int i) {
        ViewHolder holder = (ViewHolder) baseViewHolder;
        Street str = (Street)mDataSource.get(i);
        holder.streetName.setText(str.getName());
    }

    private static class ViewHolder extends BaseViewHolder {
        TextView streetName;

        public ViewHolder(View itemView) {
            super(itemView);
            streetName = (TextView)itemView.findViewById(R.id.streetname_view);
        }
    }
}
