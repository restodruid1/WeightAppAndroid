package com.example.kevinbristowoption3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "userAccounts.db";
    private static final int VERSION = 5;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    private static final class AccountsTable {
        private static final String TABLE = "accounts";
        private static final String COL_ID = "_id";
        private static final String COL_USERNAME = "username";
        private static final String COL_PASSWORD = "password";
        private static final String COL_SMS = "seen_sms";
        private static final String COL_O = "opt_sms";

    }
    private static final class WeightsTable {
        private static final String TABLE = "weights";
        private static final String COL_ID = "id";
        private static final String COL_USERNAME = "username";
        private static final String COL_DATE = "date";
        private static final String COL_WEIGHT = "weight";
        private static final String COL_GOAL = "goal";
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + AccountsTable.TABLE + " (" +
                AccountsTable.COL_ID + " integer primary key autoincrement, " +
                AccountsTable.COL_USERNAME + " text, " +
                AccountsTable.COL_PASSWORD + " text, " +
                AccountsTable.COL_SMS + " text, " +
                AccountsTable.COL_O + " text)");

        db.execSQL("create table " + WeightsTable.TABLE + " ( " +
                WeightsTable.COL_ID + " integer primary key autoincrement, " +
                WeightsTable.COL_USERNAME + " text, " +
                WeightsTable.COL_DATE + " text, " +
                WeightsTable.COL_WEIGHT + " int, " +
                WeightsTable.COL_GOAL + " int)");

        Log.d("Database", "onCreate called");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("drop table if exists " + AccountsTable.TABLE);
        db.execSQL("drop table if exists " + WeightsTable.TABLE);
        onCreate(db);
    }

    public void addWeights(String username, int weight, String date, int goal) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WeightsTable.COL_USERNAME, username);
        values.put(WeightsTable.COL_WEIGHT, weight);
        values.put(WeightsTable.COL_DATE, date);
        values.put(WeightsTable.COL_GOAL, goal);

        db.insert(WeightsTable.TABLE, null, values);
    }

    public Cursor getAllData(String usr) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + WeightsTable.TABLE + " WHERE username = ? ORDER BY date DESC";
        String[] selectionArgs = new String[]{usr};
        return db.rawQuery(query, selectionArgs);
        //return db.rawQuery("SELECT * FROM " + WeightsTable.TABLE + " where username = " + usr + " ORDER BY date DESC", null);  // Newest first
    }

    public Integer deleteData(String usr, int weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(WeightsTable.TABLE, "username = ? and weight = ?", new String[]{usr, String.valueOf(weight)});
    }

    public boolean updateData(String usr, int goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WeightsTable.COL_GOAL, goal);
        db.update(WeightsTable.TABLE, contentValues, "username = ?", new String[]{usr});
        return true;
    }

    public int getGoal(String usr) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "select goal from " + WeightsTable.TABLE + " where username = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{usr});
        int weight;

        if (cursor.moveToFirst()) {
            weight = cursor.getInt(0);
            Log.d("GOAL WEIGHT", String.valueOf(weight));
            cursor.close();
            return weight;
        }else {
            cursor.close();
            return 0;
        }

    }

    public boolean checkUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select * from " + AccountsTable.TABLE + " where username = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{username});

        if (cursor.moveToFirst()) {
            //Username already exists
            cursor.close();
            return true;
        } else {
            //Username does not exist (ok to create account)
            cursor.close();
            return false;
        }
    }

    public boolean addAccount(String username, String password) {
        SQLiteDatabase db = getWritableDatabase();

        // SECURITY - Check if account exists
        if (this.checkUsername(username)) {
            return false;
        }

        ContentValues values = new ContentValues();
        values.put(AccountsTable.COL_USERNAME, username);
        values.put(AccountsTable.COL_PASSWORD, password);
        values.put(AccountsTable.COL_SMS, "false");
        values.put(AccountsTable.COL_O, "false");
        Log.d("values", values.toString());


        db.insert(AccountsTable.TABLE, null, values);

        return true;
    }

    public boolean getAccount(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();

        String sql = "select * from " + AccountsTable.TABLE + " where username = ? and password = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{username, password});
        //Log.d("cursor", cursor.toString());
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String username1 = cursor.getString(1);
                String password1 = cursor.getString(2);
                String sms = cursor.getString(3);

                Log.d("MyDatabase", "Account = " + username1 + ", " + password1 + ", " + sms);
            } while (cursor.moveToNext());
        } else {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    public boolean getSeenSms(String username) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteDatabase dbWrite = getWritableDatabase();

        String sql = "select * from " + AccountsTable.TABLE + " where username = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{username});
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String username1 = cursor.getString(1);
                String sms = cursor.getString(3);
                String seen = cursor.getString(4);
                Log.d("MyDatabase", "Account = " + username1 + ", " + sms + ", " + seen);
                if (sms.equals("false")) {
                    String[] args = {username1};
                    ContentValues values = new ContentValues();
                    values.put(AccountsTable.COL_SMS, "true");
                    dbWrite.update(AccountsTable.TABLE,values,"username = ?",args);
                    cursor.close();
                    return false;
                } else {
                    SmsResponse.smsOpt = cursor.getString(4);
                    cursor.close();
                    return true;
                }


            } while (cursor.moveToNext());
        } else {
            cursor.close();
            return false;
        }
    }

    public boolean getOptSms(String username, boolean opt) {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteDatabase dbWrite = getWritableDatabase();

        String sql = "select * from " + AccountsTable.TABLE + " where username = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{username});
        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(0);
                String username1 = cursor.getString(1);
                //String optSms = cursor.getString(4);
                //Log.d("MyDatabase", "Account = " + username1 + ", " + optSms);
                if (opt) {
                    String[] args = {username1};
                    ContentValues values = new ContentValues();
                    values.put(AccountsTable.COL_O, "true");
                    dbWrite.update(AccountsTable.TABLE,values,"username = ?",args);
                    cursor.close();
                    return true;
                } else {
                    String[] args = {username1};
                    ContentValues values = new ContentValues();
                    values.put(AccountsTable.COL_O, "false");
                    dbWrite.update(AccountsTable.TABLE,values,"username = ?",args);
                    cursor.close();
                    return false;
                }


            } while (cursor.moveToNext());
        } else {
            cursor.close();
            return false;
        }
    }

}
