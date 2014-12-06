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

    private static class ViewHolder {
        TextView streetName;
        int position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = mInflater.inflate(R.layout.street_row, parent, false);

            ViewHolder holder = new ViewHolder();
            holder.streetName = (TextView)view.findViewById(R.id.streetname_view);

            view.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.position = position;

        _bindStreet(holder, (Street)mDataSource.get(position), position);

        return view;
    }

    private void _bindStreet(ViewHolder holder, Street str, int position) {
        holder.streetName.setText(str.getName());
    }
}
