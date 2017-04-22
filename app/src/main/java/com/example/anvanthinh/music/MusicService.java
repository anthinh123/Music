package com.example.anvanthinh.music;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import java.io.IOException;
import java.util.ArrayList;

import static com.example.anvanthinh.music.ui.ListSongFragment.DURATION;

public class MusicService extends Service implements Loader.OnLoadCompleteListener<Cursor> {
    public static  final String BROADCAST = "gui broadcast";
    public static final String THONG_TIN_BAI_HAT = "thong_tin_bai_hat";
    public static final String PLAY_SONG_FROM_LIST = "choi bai hat o vi tri";
    public static final String PAUSE = "pause";
    public static final String PLAY_CONTINUES = "Play tiep tai vi tri dang dung";
    public static final String NEXT = "chuyen bai hat tiep theo";
    public static final String PREVIOUS = "quay lai bao truoc";
    public static final String LOOP_ONE = "lap lai 1 bai";
    public static final String LOOP_ALL = "lap lai danh sach";
    public static final String DONT_LOOP = "choi den bai cuoi";
    public static final String RANDOM = "choi ngau nhien";
    public static final String PROGRESS_SEEKBAR = "procress seekbar";
    public static final String TIME_CURRENT = "time current";
    public static final String TUA_NHANH = "tua_nhanh_bai_hat";
    public static String VI_TRI = "position_song_now";

    private MediaPlayer media = new MediaPlayer();
    private ArrayList<Music> mArr ;
    private int mPosition;
    private CursorLoader mCursorLoader;
    private Thread mThreadSeekbar;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        mArr = new ArrayList<Music>();
        final String sortOder = MediaStore.Audio.Media.TITLE + " ASC";
//        mCursorLoader = new CursorLoader(getApplicationContext(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, sortOder);
//        mCursorLoader.registerListener(ListSongFragment.ID_SONG_LOADER, this);
//        mCursorLoader.startLoading();

        final  Cursor c = getApplication().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, sortOder );
        for (c.moveToFirst() ; !c.isAfterLast(); c.moveToNext()){
            Music m = new Music();
            m.setPath( c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA)));
            m.setName_singer(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            m.setName_song(c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            m.setDuration(c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            mArr.add(m);
        }
        c.close();
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
                    mPosition = intent.getIntExtra(PLAY_SONG_FROM_LIST, 0);
                    playSong(mPosition);
                    break;
                case PAUSE:
                    pauseSong();
                    finishUpdateSeekbar();
                    break;
                case PLAY_CONTINUES:
                    playContinues();
                    break;
                case NEXT:
                    nextSong();
                    break;
                case PREVIOUS:
                    previousSong();
                    break;
                case TUA_NHANH:
                    finishUpdateSeekbar();
                    int time = intent.getIntExtra(VI_TRI , 0);
                    media.seekTo(time);
                    media.start();
                    updateSeekbar();
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor c) {
//        int a = c.getCount();
//        for (c.moveToFirst() ; !c.isAfterLast(); c.moveToNext()){
//            Music m = new Music();
//            m.setPath( c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA)));
//            mArr.add(m);
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Stop the cursor loader
        if (mCursorLoader != null) {
            mCursorLoader.unregisterListener(this);
            mCursorLoader.cancelLoad();
            mCursorLoader.stopLoading();
        }
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

    // ham choi nhac tai vi tri position
    private void playSong(int position) {
        final String pathMusic = mArr.get(position).getPath();
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
        updateSeekbar();
        sendBroastReceiver();
        ContinuesSong(media);
    }

    private void ContinuesSong(MediaPlayer media){
        media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextSong();
            }
        });
    }

    private void nextSong(){
        media.release();
        finishUpdateSeekbar();
        mPosition = mPosition +1;
        playSong(mPosition);
    }

    private void previousSong(){
        media.release();
        finishUpdateSeekbar();
        mPosition = mPosition -1;
        playSong(mPosition);
    }

    // gui broadcast di
    private void sendBroastReceiver(){
        Intent i = new Intent(THONG_TIN_BAI_HAT);
        Music m = mArr.get(mPosition);
        Bundle bun = new Bundle();
        bun.putSerializable(THONG_TIN_BAI_HAT, m);
        i.putExtra(THONG_TIN_BAI_HAT, bun);
        sendBroadcast(i);
    }

    // hàm cập nhật seekbar
    public void updateSeekbar(){
        if(media.isPlaying() == false) {
            media.start();
        }
        mThreadSeekbar = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < media.getDuration(); i++) {
                    try {
                        Thread.sleep(1000);
                        Intent intent_seekbar = new Intent(PROGRESS_SEEKBAR);
                        intent_seekbar.putExtra(DURATION, media.getDuration());
                        intent_seekbar.putExtra(TIME_CURRENT, media.getCurrentPosition());
                        sendBroadcast(intent_seekbar);
                    } catch (InterruptedException e) {
                        if (mThreadSeekbar.isInterrupted() == false) {
                            return;
                        }
                        e.printStackTrace();
                    }
                }
            }
        });
        mThreadSeekbar.start();
    }

    // hàm kết thúc cập nhật seekbar
    public void finishUpdateSeekbar(){
        if (mThreadSeekbar != null && mThreadSeekbar.isAlive()){
            mThreadSeekbar.interrupt();
        }
    }

    public class MyBinded extends Binder {
        public MusicService getService(){
            return MusicService.this;
        }

        public Music getInforMusicIsPlay(){
            if (mPosition > 0 ){
                return mArr.get(mPosition);
            }
            return null;
        }
    }

}
