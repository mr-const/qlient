package info.mrconst.qlient;

import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;

import java.io.IOException;
import java.io.InputStream;

public class PacketReader {
    private static final String TAG = PacketReader.class.getName();

    private static PacketReader mClass = null;
    private ObjectMapper mMapper = null;

    public static boolean init() {
        if (mClass == null) {
            mClass = new PacketReader();
            return true;
        }

        Log.w(TAG, "Secondary init attempted");
        return true;
    }

    public static void term() {
        mClass.mMapper = null;
        mClass = null;
    }

    PacketReader() {
        mMapper = new CsvMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static JavaType constructParametricType(Class<?> outer, Class<?> inner) {
        return mClass.mMapper.getTypeFactory().constructParametrizedType(outer, outer, inner);
    }

    public static JavaType constructParametricType(Class<?> outer, JavaType inner) {
        return mClass.mMapper.getTypeFactory().constructParametrizedType(outer, outer, inner);
    }

    public static <ResponseType> ResponseType read(InputStream source, Class<ResponseType> t) throws IOException {
        return mClass.mMapper.readValue(source, t);
    }

    public static <ResponseType> ResponseType read(InputStream source, JavaType t) throws IOException {
        return mClass.mMapper.readValue(source, t);
    }
}
