package info.mrconst.qlient;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Дает доступ к кастомным шрифтам, используемым в приложении
 */
public final class FontManager {
    private static final String LOG_TAG = "FontManager";
    private static final String LOG_ON_INIT = "FontManager initialization...";
    private static final String LOG_ON_SECOND_INIT = "FontManager second init attempt. Ignoring.";
    private static final String LOG_ON_LOAD_TYPEFACE = "FontManager is loading typeface %s.";

    public static final String BRAILLE = "braille";
    public static final String SEMAPHORE = "semaphore";
    public static final String DEFAULT = "default";

    private Context mContext;

    private HashMap<String, Typeface> mTypefaces;
    private HashMap<String, String> mTypefaceFiles;

    private static FontManager mClass = null;

    public static void init(Context context) {
        if (mClass == null) {
            Log.d(LOG_TAG, LOG_ON_INIT);
            mClass = new FontManager(context);
        } else
            Log.w(LOG_TAG, LOG_ON_SECOND_INIT);

    }

    public static void term() {
        mClass = null;
    }

    private FontManager(Context context) {
        mContext = context;

        mTypefaceFiles = new HashMap<>();
        onInitTypefaceFilesList();

        mTypefaces = new HashMap<>();
        loadTypefaces();
    }

    private void onInitTypefaceFilesList() {
        mTypefaceFiles.put(BRAILLE, "braille.ttf");
        mTypefaceFiles.put(SEMAPHORE, "semaphore.ttf");
    }

    private Typeface loadTypeface(String filename) {
        Log.d(LOG_TAG, String.format(LOG_ON_LOAD_TYPEFACE, filename));
        return Typeface.createFromAsset(mContext.getAssets(), "fonts/" + filename);
    }

    private void loadTypefaces() {
        for (String key : mTypefaceFiles.keySet()) {
            String filename = mTypefaceFiles.get(key);
            mTypefaces.put(key, loadTypeface(filename));
        }
    }

    public static Typeface getTypeface(String typeface) {
        assert mClass != null : "FontsManager must be initialized before this call!";

        if (DEFAULT.equals(typeface))
            return Typeface.DEFAULT;
        return mClass.mTypefaces.get(typeface);
    }

    public static void setTypefaceForAllTextViews(View parent, Typeface typeface) {
        if (parent instanceof ViewGroup) {
            ViewGroup parentGroup = (ViewGroup) parent;
            for (int i = 0; i < parentGroup.getChildCount(); ++i) {
                View child = parentGroup.getChildAt(i);
                if (child instanceof TextView)
                    ((TextView) child).setTypeface(typeface);
                else if (child instanceof ViewGroup)
                    setTypefaceForAllTextViews(child, typeface);
            }
        }
    }
}
