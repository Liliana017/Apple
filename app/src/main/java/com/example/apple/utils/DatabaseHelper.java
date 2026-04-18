package com.example.apple.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.apple.models.Item;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "shop.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    private static final String TABLE_ITEMS = "items";
    private static final String COLUMN_ITEM_ID = "id";
    private static final String COLUMN_USER_ID_FK = "user_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IS_FAVORITE = "is_favorite";
    private static final String COLUMN_DATE_CREATED = "date_created";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createUsers = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT UNIQUE NOT NULL, " +
                COLUMN_PASSWORD + " TEXT NOT NULL)";
        db.execSQL(createUsers);


        String createItems = "CREATE TABLE " + TABLE_ITEMS + " (" +
                COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID_FK + " INTEGER, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PRICE + " REAL, " +
                COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0, " +
                COLUMN_DATE_CREATED + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_USER_ID_FK + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "))";
        db.execSQL(createItems);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }


    public boolean registerUser(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }

    public int loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_ID + " FROM " + TABLE_USERS +
                        " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?",
                new String[]{email, password});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);
        }
        cursor.close();
        db.close();
        return userId;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + "=?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }


    public boolean addItem(int userId, String title, String description, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID_FK, userId);
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_IS_FAVORITE, 0);
        values.put(COLUMN_DATE_CREATED, getCurrentDate());

        long result = db.insert(TABLE_ITEMS, null, values);
        db.close();
        return result != -1;
    }

    public List<Item> getAllItems(int userId) {
        List<Item> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ITEMS + " WHERE " + COLUMN_USER_ID_FK + "=? ORDER BY " + COLUMN_ITEM_ID + " DESC",
                new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                items.add(new Item(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4),
                        cursor.getInt(5) == 1,
                        cursor.getString(6)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return items;
    }

    public List<Item> getFavoriteItems(int userId) {
        List<Item> items = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_ITEMS + " WHERE " + COLUMN_USER_ID_FK + "=? AND " + COLUMN_IS_FAVORITE + "=1 ORDER BY " + COLUMN_ITEM_ID + " DESC",
                new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                items.add(new Item(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getDouble(4),
                        cursor.getInt(5) == 1,
                        cursor.getString(6)
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return items;
    }

    public boolean updateItem(int itemId, String title, String description, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_PRICE, price);

        int result = db.update(TABLE_ITEMS, values, COLUMN_ITEM_ID + "=?", new String[]{String.valueOf(itemId)});
        db.close();
        return result > 0;
    }

    public boolean deleteItem(int itemId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_ITEMS, COLUMN_ITEM_ID + "=?", new String[]{String.valueOf(itemId)});
        db.close();
        return result > 0;
    }

    public boolean toggleFavorite(int itemId) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT " + COLUMN_IS_FAVORITE + " FROM " + TABLE_ITEMS + " WHERE " + COLUMN_ITEM_ID + "=?",
                new String[]{String.valueOf(itemId)});
        int current = 0;
        if (cursor.moveToFirst()) {
            current = cursor.getInt(0);
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(COLUMN_IS_FAVORITE, current == 0 ? 1 : 0);
        int result = db.update(TABLE_ITEMS, values, COLUMN_ITEM_ID + "=?", new String[]{String.valueOf(itemId)});
        db.close();
        return result > 0;
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
}