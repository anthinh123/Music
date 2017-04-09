package com.example.anvanthinh.music;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.io.IOException;

public class MusicService extends Service implements LoaderManager.LoaderCallbacks<Cursor>, Loader.OnLoadCompleteListener<Cursor> {

    public static final String THONG_TIN_BAI_HAT = "thong_tin_bai_hat";
    public static final String PLAY_SONG_FROM_LIST = "choi bai hat o vi tri";
    public static final String PAUSE = "pause";
    public static final String PLAY_CONTINUES = "Play tiep tai vi tri dang dung";
    private final int ID_SONG_LOADER = 0;

    private Cursor mCursor;
    private CursorLoader mCursorLoader;
    private MediaPlayer media = new MediaPlayer();

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
//        final String orderBy = MediaStore.Audio.Media.TITLE + " ASC";
//        mCursorLoader = new CursorLoader( getApplicationContext(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, orderBy );
//        mCursorLoader.registerListener(ID_SONG_LOADER, this);
//        mCursorLoader.startLoading();
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null){
            final String actionIntent = intent.getAction();
            switch (actionIntent){
                case PLAY_SONG_FROM_LIST:
                    if(media.isPlaying()){
                        media.release();
                    }
                    playSong(intent.getStringExtra(PLAY_SONG_FROM_LIST));
                    break;
                case PAUSE:
                    pauseSong();
                    break;
                case PLAY_CONTINUES:
                    playContinues();
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    // ham phat nhac tiep o vi tri dang dung
    private void playContinues() {
        if(media.isPlaying() == false){
            media.start();
        }
    }

    // ham tam dung nhac
    private void pauseSong() {
        if(media.isPlaying()){
            media.pause();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the cursor loader
//        if (mCursorLoader != null) {
//            mCursorLoader.unregisterListener(this);
//            mCursorLoader.cancelLoad();
//            mCursorLoader.stopLoading();
//        }
    }

    // ham choi nhac tai vi tri position
    private void playSong(String pathMusic) {
        media = new MediaPlayer();
        media.setAudioStreamType(AudioManager.STREAM_MUSIC);
        media.setVolume(1,1);
        try {
            if (null != pathMusic){
                media.setDataSource(pathMusic);
                media.prepare();
                media.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
//        mCursor = data;
    }
}
