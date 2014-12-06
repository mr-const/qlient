package info.mrconst.qlient;

import android.text.Editable;
import android.text.TextWatcher;

import info.mrconst.qlient.data.FilterableFeed;

public class FilterTextWatcher implements TextWatcher {

    private FilterableFeed mDataSource;

    public FilterTextWatcher(FilterableFeed dataSource) {
        mDataSource = dataSource;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        mDataSource.startFilter(s.toString());
    }
}