package com.example.anvanthinh.music.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by An Van Thinh on 5/26/2017.
 */

public class MusicProvider extends ContentProvider {
    private MusicOpenHelper mMusicHelper;
    private SQLiteDatabase db;
    public static final String WORD_BASE_PATH = "music";
    public static final String AUTHORITY = "com.example.anvanthinh.musicplayer.activity";
    public static final String URI = "content://" + AUTHORITY + "/" + WORD_BASE_PATH;
    public static final Uri CONTENT_URI = Uri.parse(URI);

    @Override
    public boolean onCreate() {
        mMusicHelper = new MusicOpenHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
