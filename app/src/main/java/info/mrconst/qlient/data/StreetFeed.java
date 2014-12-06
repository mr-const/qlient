package info.mrconst.qlient.data;

import android.content.Context;

import info.mrconst.qlient.model.Street;

public class StreetFeed extends BaseFeed<Street> {
    public StreetFeed(Context ctx) {
        super(ctx, Street.class);
    }
}
