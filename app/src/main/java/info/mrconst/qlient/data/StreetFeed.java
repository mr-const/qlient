package info.mrconst.qlient.data;

import android.content.Context;

import com.fasterxml.jackson.databind.JavaType;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import info.mrconst.qlient.PacketReader;
import info.mrconst.qlient.R;
import info.mrconst.qlient.model.Street;

public class StreetFeed extends BaseFeed<Street> {
    public StreetFeed(Context ctx) {
        super(ctx, Street.class);
        _readFromResources();
    }

    private void _readFromResources() {
        JavaType t = PacketReader.constructParametricType(ArrayList.class, Street.class);
        InputStream is = mCtx.getResources().openRawResource(R.raw.kiev_streets_uk);
        try {
            mObjects = PacketReader.read(is, t);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
