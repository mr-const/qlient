package info.mrconst.qlient.data;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.Collection;

import info.mrconst.qlient.notifications.NotificationCenter;

public abstract class FilterableFeed<T> extends BaseFeed<T> implements Filterable {

    public static final String NOTIFICATION_FILTER_STARTED = "filter_start";
    public static final String NOTIFICATION_FILTER_FINISHED = "filter_finish";

    private static final String STATE_FILTER_CONSTRAINT = "filter_constraint";

    protected FeedFilter mFilter;
    private CharSequence mFilterConstraint = "";

    protected ArrayList<T> mFilteredObjects = new ArrayList<>();

    public FilterableFeed(Context ctx, Class<T> tClass) {
        super(ctx, tClass);
    }

    public CharSequence getFilterConstraint() {
        return mFilterConstraint;
    }

    public void startFilter(CharSequence text) {
        if (mFilter == null)
            mFilter = new FeedFilter();

        mFilterConstraint = text;
        mFilter.filter(text.toString().toLowerCase());
        NotificationCenter.sendMessage(NOTIFICATION_FILTER_STARTED, null, this, true);
    }

    @Override
    public int size() {
        return mFilteredObjects.size();
    }

    @Override
    public T get(int i) {
        return mFilteredObjects.get(i);
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    private class FeedFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<T> filteredItems = new ArrayList<>();

                for (T item: mObjects) {
                    if (itemConformsToFilterCondition(item, constraint))
                        filteredItems.add(item);
                }
                result.count = filteredItems.size();
                result.values = filteredItems;
            } else {
                synchronized (this) {
                    result.values = mObjects;
                    result.count = mObjects.size();
                }
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            mFilteredObjects.clear();
            mFilteredObjects.addAll((Collection<T>)results.values);
            NotificationCenter.sendMessage(BaseFeed.NOTIFICATION_DATASET_UPDATE, null, FilterableFeed.this, true);
            NotificationCenter.sendMessage(NOTIFICATION_FILTER_FINISHED, null, FilterableFeed.this, true);
        }
    }

    protected abstract boolean itemConformsToFilterCondition(T item, CharSequence constraint);
}
