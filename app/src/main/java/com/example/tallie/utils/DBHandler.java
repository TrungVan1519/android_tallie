package com.example.tallie.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.tallie.models.Cart;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private final static int DB_VERSION = 1;
    private final static String DB_NAME = "TALLIE_DB";
    private final static String TABLE_NAME = "cart";

    private final static String COL_ID = "_id";
    private final static String COL_TIME = "time";
    private final static String COL_QTY = "qty";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COL_ID + " INTEGER PRIMARY KEY, " + COL_TIME + " TEXT, " + COL_QTY + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public List<Cart> allRecords() {
        SQLiteDatabase db = getReadableDatabase();

        try {
            List<Cart> carts = new ArrayList<>();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            int time = cursor.getColumnIndex(COL_TIME);
            int qty = cursor.getColumnIndex(COL_QTY);

            while (cursor.moveToNext()) {
                carts.add(new Cart(cursor.getString(time), cursor.getInt(qty)));
            }
            cursor.close();
            db.close();

            return carts;
        } catch (Exception ex) {
            onCreate(db);
            ex.printStackTrace();

            return new ArrayList<>();
        }
    }

    public long addRecord(Cart cart) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_TIME, cart.getTime());
        contentValues.put(COL_QTY, cart.getQty());

        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();

        return result;
    }

    public void dropDB() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
