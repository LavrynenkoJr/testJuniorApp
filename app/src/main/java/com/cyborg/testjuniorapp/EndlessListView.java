package com.cyborg.testjuniorapp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ListView;

/**
 * Created by Cyborg on 11/14/2016.
 */

public class EndlessListView<T> extends FrameLayout {

    private ListView listView;

    private boolean loading = false;
    private View loadingView;

    private boolean hasMore = false;

    private EndlessListAdapter<String> endlessListAdapter;

    public EndlessListView(Context context) {
        super(context);
        this.init(context, null);
    }

    public EndlessListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init(context, attrs);
    }

    public EndlessListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){
        View view = inflate(context, R.layout.endless_list_view, this);

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setFooterDividersEnabled(false);

    }

    public void setAdapter(EndlessListAdapter<String> infiniteListAdapter){

        this.endlessListAdapter = infiniteListAdapter;
        listView.setAdapter(infiniteListAdapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(!hasMore)
                    return;

                int lastVisibleItem = visibleItemCount + firstVisibleItem;
                if (lastVisibleItem >= totalItemCount && !loading) {

                    EndlessListView.this.endlessListAdapter.onNewLoadRequired();

                }
            }
        });
    }

    public void addNewItem(T newItem){
        endlessListAdapter.addNewItem(listView, (String) newItem);
    }

    public void startLoading(){

        if(listView.getFooterViewsCount() > 0) {
            listView.removeFooterView(loadingView);
        }
        loading = true;
    }

    public void stopLoading(){
        if(listView.getFooterViewsCount() > 0) {
            listView.removeFooterView(loadingView);
        }
        loading = false;
    }

    public void hasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

}
