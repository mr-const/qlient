package info.mrconst.qlient;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import info.mrconst.qlient.adapter.StreetAdapter;
import info.mrconst.qlient.data.DataStore;

public class AnagramFragment extends Fragment {
    private View mView;
    private EditText mAnagramInput;
    private ListView mStreetNameList;
    StreetAdapter mStreetAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mView != null) {
            ((ViewGroup) mView.getParent()).removeView(mView);
            return mView;
        }

        mView = inflater.inflate(R.layout.fragment_anagram, null);

        mAnagramInput = (EditText)mView.findViewById(R.id.anagram_input);
        mStreetNameList = (ListView)mView.findViewById(R.id.street_listing);

        mStreetAdapter = new StreetAdapter(getActivity(), DataStore.streetFeed("uk"));
        return mView;
    }
}
