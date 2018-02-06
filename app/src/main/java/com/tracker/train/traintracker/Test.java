package com.tracker.train.traintracker;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

public class Test extends AppCompatActivity {
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //membuat class yang menghandle data base
        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
            Log.d("debug","update db");
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        //mendapatkan database dari folder assets
        try {
            mDb = mDBHelper.getWritableDatabase();
            Log.d("debug","connect db");
        } catch (SQLException mSQLException) {
            throw mSQLException;
        }



        //mengambil record dari tabel provinsi
        String query="SELECT * FROM stasiun";
        Cursor cursor = mDb.rawQuery(query, null);
        //melakukan iterasi untuk setiap record
        if (cursor.moveToFirst()) {
            do {
                System.out.print(cursor.getString(0));
                System.out.print(" "+cursor.getString(1));
                System.out.print(" "+cursor.getString(2));
                System.out.print(" "+cursor.getString(3));
                System.out.println(" "+cursor.getString(4));
            } while (cursor.moveToNext());
        }

        cursor.close();
        mDb.close();

    }
}
