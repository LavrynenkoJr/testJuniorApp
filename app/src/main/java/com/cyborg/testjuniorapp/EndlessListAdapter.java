package com.cyborg.testjuniorapp;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Cyborg on 11/14/2016.
 */

public class EndlessListAdapter <T> extends ArrayAdapter<T> {

    private MainActivity mainActivity;
    private int itemLayoutRes;
    private ArrayList<T> itemList;

    public EndlessListAdapter(MainActivity mainActivity, int itemLayoutRes, ArrayList<T> itemList) {
        super(mainActivity, itemLayoutRes, itemList);

        this.mainActivity = mainActivity;
        this.itemLayoutRes = itemLayoutRes;
        this.itemList = itemList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {
            convertView = mainActivity.getLayoutInflater().inflate(itemLayoutRes, parent, false);

            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.name);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String text = (String) itemList.get(position);
        if (text != null) {
            holder.text.setText(text);
        }

        return convertView;
    }

    public void onNewLoadRequired() {
        mainActivity.loadNewItems();
    }


    static class ViewHolder {
        TextView text;
    }

    protected final void addNewItem(ListView listView, T newItem){
        itemList.add(newItem);
        this.notifyDataSetChanged();
    }


}