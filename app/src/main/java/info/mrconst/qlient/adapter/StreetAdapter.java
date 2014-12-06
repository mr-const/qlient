package info.mrconst.qlient.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import info.mrconst.qlient.data.StreetFeed;

public class StreetAdapter extends BaseFeedAdapter {

    public StreetAdapter(Context context, StreetFeed feed) {
        super(context, feed);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }
}
