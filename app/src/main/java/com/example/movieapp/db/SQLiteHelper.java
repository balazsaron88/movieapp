package com.example.movieapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "UserDataBase";

    public static final String TABLE_NAME_USER = "UserTable";
    public static final String TABLE_NAME_FAVORITES = "FavoriteTable";

    // User
    public static final String Table_Column_ID = "id";
    public static final String Table_Column_1_Name = "name";
    public static final String Table_Column_2_Email = "email";
    public static final String Table_Column_3_Password = "password";

    // User
    public static final String COLUMN_MOVIE_ID = "id";
    public static final String COLUMN_MOVIE_TITLE = "title";
    public static final String COLUMN_MOVIE_DESC = "desc";
    public static final String COLUMN_MOVIE_PICTURE = "picture";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_USER + " (" + Table_Column_ID + " INTEGER PRIMARY KEY, " + Table_Column_1_Name + " VARCHAR, " + Table_Column_2_Email + " VARCHAR, " + Table_Column_3_Password + " VARCHAR)";
        String CREATE_TABLE_FAVORITES = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_FAVORITES + " (" + COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY, " + COLUMN_MOVIE_TITLE + " VARCHAR, " + COLUMN_MOVIE_DESC + " VARCHAR, " + COLUMN_MOVIE_PICTURE + " VARCHAR)";
        database.execSQL(CREATE_TABLE_USER);
        database.execSQL(CREATE_TABLE_FAVORITES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_FAVORITES);
        onCreate(db);
    }
}