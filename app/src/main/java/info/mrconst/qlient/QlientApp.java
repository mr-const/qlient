package info.mrconst.qlient;

import android.app.Application;

import info.mrconst.qlient.data.DataStore;
import info.mrconst.qlient.notifications.NotificationCenter;

public class QlientApp  extends Application {

    private static QlientApp mClass = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mClass = this;
        _appInit();
    }

    private void _appInit() {
        PacketReader.init();
        NotificationCenter.init();
        DataStore.init(this);
        FontManager.init(this);
    }
}
