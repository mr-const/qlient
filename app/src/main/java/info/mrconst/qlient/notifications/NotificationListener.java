package info.mrconst.qlient.notifications;

/**
 * Created by eugene on 26.09.14.
 */
public interface NotificationListener {

    /*
     * DO NOT subscribe or unsibscribe from notifications in this method
     */
    public void onNotification(Notification notification);
}
