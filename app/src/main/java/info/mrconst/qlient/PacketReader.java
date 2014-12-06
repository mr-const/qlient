package info.mrconst.qlient;

import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import info.mrconst.qlient.model.Street;

public class PacketReader {
    private static final String TAG = PacketReader.class.getName();

    private static PacketReader mClass = null;
    private CsvMapper mMapper = null;

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
        mMapper = new CsvMapper();
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

    public static ArrayList<Street> readStreets(InputStream source) throws IOException {
        CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
        MappingIterator<Map.Entry> it =
            mClass.mMapper.reader(Street.class).with(bootstrapSchema).readValues(source);
        ArrayList<Street> objects = new ArrayList<>();
        while(it.hasNextValue()) {
            objects.add((Street)it.nextValue());
        }
        return objects;
    }
}
