package com.cyborg.testjuniorapp;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Cursor cursor;
    private DatabaseHelper mDatabaseHelper;

    private final int DEFAULT_MAX_ELEMENTS_IN_MEMORY = 50;
    private int ITEM_COUNT_LIMIT = 200;
    private final int TIME_TO_LOAD = 500; //in ms

    private int itemOffset = 0;

    private LinearLayout container;
    private EndlessListView<String> endlessListView;

    private ArrayList<String> itemList;
    private EndlessListAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabaseHelper = new DatabaseHelper(this, "testjuniorapp.db", null, 1);

        //mDatabaseHelper.deleteAll();
        //System.out.println("Count " + mDatabaseHelper.getUsersCount());


        if (mDatabaseHelper.getUsersCount() == 0) {
            InsertData insertData = new InsertData(this);
            insertData.execute();
        }

        container = (LinearLayout) findViewById(R.id.activity_main);
        endlessListView = (EndlessListView) findViewById(R.id.endlessListView);

        itemList = new ArrayList<>();
        adapter = new EndlessListAdapter(this, R.layout.items_layout, itemList);

        endlessListView.setAdapter(adapter);


        System.out.println("Count " + mDatabaseHelper.getUsersCount());

        ITEM_COUNT_LIMIT = mDatabaseHelper.getUsersCount();
        cursor = mDatabaseHelper.getAllWords();

        loadNewItems();

    }

    public void loadNewItems() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                endlessListView.startLoading();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Thread.sleep(TIME_TO_LOAD);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void param) {
                if(itemOffset >= ITEM_COUNT_LIMIT) {
                    endlessListView.hasMore(false);
                }
                else {
                    //ADD NEW ITEMS TO LIST
                    for (int i = itemOffset; i < itemOffset + DEFAULT_MAX_ELEMENTS_IN_MEMORY; i++) {

                        String firesNname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.FIRST_NAME));
                        String lastName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.LAST_NAME));
                        cursor.moveToNext();

                        endlessListView.addNewItem(firesNname + " " + lastName);
                    }
                    itemOffset += DEFAULT_MAX_ELEMENTS_IN_MEMORY;
                    Log.d("InfiniteListView", "Current item count = " + itemOffset);

                    endlessListView.hasMore(true);
                }

                endlessListView.stopLoading();
            }
        }.execute();
    }

}
