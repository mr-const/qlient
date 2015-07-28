package info.mrconst.qlient.data;

import android.content.Context;

import com.google.common.base.CharMatcher;

import java.io.IOException;
import java.io.InputStream;

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
        String my = item.getName().toLowerCase()
                .replace("ул.", "")
                .replace("пр.", "")
                .replace("пл.", "")
                .replace("пер.", "")
                .replace("бульв.", "").trim();
        // однозначно если входящая строка длинее искомой, то мы не совпадаем даже частично.
        if (constraint.length() > my.length())
            return false;
        String their = constraint.toString().toLowerCase();
        // Перебираем все буквы входящего набора в искомом
        for(char c: their.toCharArray()) {
            int my_count = CharMatcher.is(c).countIn(my);
            int their_count = CharMatcher.is(c).countIn(their);
            if (my_count < their_count)
                return false;
        }

        return true;
    }
}
