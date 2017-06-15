package com.example.anvanthinh.music.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by An Van Thinh on 5/26/2017.
 */

class MusicOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "music_database";
    private static final int DATABASE_VERSION = '1';
    public static final String PLAYLIST_TABLE = "PLAYLIST_TABLE";
    public static final String TEN = "ten_bai_hat";
    public static final String YEU_THICH = "yeu_thich";
    public static final String ID = "ID";
    public static final String ID_BAI_HAT = "ID_bai_hat";

    public MusicOpenHelper(Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + PLAYLIST_TABLE + " ( "
                + ID + " INTEGER PRIMARY KEY  AUTOINCREMENT, "
                + TEN + " TEXT, "
                + ID_BAI_HAT + " TEXT); "
        );

        db.execSQL(" CREATE TABLE " + PLAYLIST_TABLE + " ( "
                + ID + " INTEGER PRIMARY KEY, "
                + YEU_THICH + " INTEGER); "
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
