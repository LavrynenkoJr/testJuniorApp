package com.cyborg.testjuniorapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import static com.cyborg.testjuniorapp.DatabaseHelper.FIRST_NAME;
import static com.cyborg.testjuniorapp.DatabaseHelper.LAST_NAME;
import static com.cyborg.testjuniorapp.DatabaseHelper.TABLE_NAME;

/**
 * Created by Cyborg on 11/10/2016.
 */

public class InsertData extends AsyncTask<Void, Void, Void>{

    private DatabaseHelper mDatabaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public InsertData(Context context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {

        mDatabaseHelper = new DatabaseHelper(context, "testjuniorapp.db", null, 1);
        sqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        sqLiteDatabase.beginTransaction();

        try{
            for (int i=0; i<1000; i++){
                String firstName = "first_name" + i;
                String lastName = "last_name" + i;
                addUser(firstName, lastName);
            }

            sqLiteDatabase.setTransactionSuccessful();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            sqLiteDatabase.endTransaction();
            sqLiteDatabase.close();
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values){
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void aVoid){
        super.onPostExecute(aVoid);

    }

    public void addUser(String firstname, String lastname) throws InterruptedException{
        ContentValues values = new ContentValues();
        values.put(FIRST_NAME, firstname);
        values.put(LAST_NAME, lastname);

        sqLiteDatabase.insert(TABLE_NAME, null, values);
    }
}
