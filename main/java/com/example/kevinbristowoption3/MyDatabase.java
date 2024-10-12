package com.example.kevinbristowoption3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabase extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "userAccounts.db";
    private static final int VERSION = 1;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static final class AccountsTable {
        private static final String TABLE = "accounts";
        private static final String COL_ID = "_id";
        private static final String COL_USERNAME = "username";
        private static final String COL_PASSWORD = "password";

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + AccountsTable.TABLE + " (" +
                AccountsTable.COL_ID + " integer primary key autoincrement, " +
                AccountsTable.COL_USERNAME + " text, " +
                AccountsTable.COL_PASSWORD + " text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("drop table if exists " + AccountsTable.TABLE);
        onCreate(db);
    }

    public boolean addAccount(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();

        // SECURITY - Check if account exists
        if (this.getAccount(username,password)){
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(AccountsTable.COL_USERNAME, username);
        values.put(AccountsTable.COL_PASSWORD, password);


        db.insert(AccountsTable.TABLE, null, values);
        return true;
    }

    public boolean getAccount(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select * from " + AccountsTable.TABLE + " where username = ? and password = ?";
        Cursor cursor = db.rawQuery(sql, new String[] {username, password });
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String username1 = cursor.getString(1);
                String password1 = cursor.getString(2);

                Log.d("MyDatabase", "Account = " + username1 + ", " + password1);
            } while (cursor.moveToNext());
        } else{
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
