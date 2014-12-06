package info.mrconst.qlient;

import android.app.Application;

import info.mrconst.qlient.data.DataStore;

public class QlientApp  extends Application {

    private static QlientApp mClass = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mClass = this;
        _appInit();
    }

    private void _appInit() {
        DataStore.init(this);
    }
}
