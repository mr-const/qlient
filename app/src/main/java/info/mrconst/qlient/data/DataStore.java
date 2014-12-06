package info.mrconst.qlient.data;

import android.content.Context;

public class DataStore {

    private static DataStore _class = null;
    StreetFeed mStreets;

    private Context mCtx = null;

    public static boolean init(Context ctx) {
        if (_class == null) {
            _class = new DataStore(ctx);
        }

        return true;
    }

    DataStore(Context ctx) {
        mCtx = ctx;

        mStreets = new StreetFeed(mCtx);
    }

    public static StreetFeed streetFeed(String uk) {
        return _class.mStreets;
    }
}
