package info.mrconst.qlient.data;

import android.content.Context;

import com.fasterxml.jackson.databind.JavaType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import info.mrconst.qlient.PacketReader;
import info.mrconst.qlient.R;
import info.mrconst.qlient.model.Street;

public class StreetFeed extends FilterableFeed<Street> {
    public StreetFeed(Context ctx) {
        super(ctx, Street.class);
        _readFromResources();
        startFilter("");
    }

    private void _readFromResources() {
        InputStream is = mCtx.getResources().openRawResource(R.raw.kiev_streets_uk);
        try {
            mObjects = PacketReader.readStreets(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected boolean itemConformsToFilterCondition(Street item, CharSequence constraint) {
        return item.getName().contains(constraint);
    }
}
