package info.mrconst.qlient;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ListView;

import info.mrconst.qlient.adapter.StreetAdapter;
import info.mrconst.qlient.data.BaseFeed;
import info.mrconst.qlient.data.DataStore;
import info.mrconst.qlient.data.FilterableFeed;
import info.mrconst.qlient.data.StreetFeed;
import info.mrconst.qlient.notifications.Notification;
import info.mrconst.qlient.notifications.NotificationCenter;
import info.mrconst.qlient.notifications.NotificationListener;

public class AnagramFragment extends Fragment
implements NotificationListener {
    private View mView;
    private EditText mAnagramInput;
    private ListView mStreetNameList;
    StreetAdapter mStreetAdapter;
    ProgressDialog mProgressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mView != null) {
            ((ViewGroup) mView.getParent()).removeView(mView);
            return mView;
        }

        mView = inflater.inflate(R.layout.fragment_anagram, null);

        mAnagramInput = (EditText)mView.findViewById(R.id.anagram_input);
        mStreetNameList = (ListView)mView.findViewById(R.id.street_listing);

        StreetFeed dataSource = DataStore.streetFeed("uk");
        mStreetAdapter = new StreetAdapter(getActivity(), dataSource);
        mStreetNameList.setAdapter(mStreetAdapter);

        mAnagramInput.addTextChangedListener(new FilterTextWatcher(dataSource));

        NotificationCenter.addListener(this, FilterableFeed.NOTIFICATION_FILTER_STARTED, dataSource);
        NotificationCenter.addListener(this, FilterableFeed.NOTIFICATION_FILTER_FINISHED, dataSource);

        return mView;
    }

    @Override
    public void onNotification(Notification notification) {
        switch(notification.getName()) {
            case FilterableFeed.NOTIFICATION_FILTER_STARTED: {
                if (getActivity() != null)
                    mProgressDialog = ProgressDialog.show(getActivity(),
                            getActivity().getString(R.string.wait_dialog),
                            getActivity().getString(R.string.filter_in_progress),
                            true, true);
                break;
            }
            case FilterableFeed.NOTIFICATION_FILTER_FINISHED: {
                if (mProgressDialog != null) {
                    mProgressDialog.hide();
                    mProgressDialog = null;
                }
            }
        }
    }
}
