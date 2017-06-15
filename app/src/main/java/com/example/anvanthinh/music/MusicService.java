package com.example.anvanthinh.music;

import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.content.Loader;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.anvanthinh.music.ui.NotificationMusic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static com.example.anvanthinh.music.ui.ListSongFragment.DURATION;

public class MusicService extends Service implements Loader.OnLoadCompleteListener<Cursor> {
    public static final String BROADCAST = "gui broadcast";
    public static final String THONG_TIN_BAI_HAT = "thong_tin_bai_hat";
    public static final String UPDATE_BUTTON = "update button";
    public static final String DANH_SACH_NHAC = "danh sach bai hat";
    public static final String PLAY_SONG_FROM_LIST = "choi bai hat o vi tri";
    public static final String PAUSE = "pause";
    public static final String PLAY_CONTINUES = "Play tiep tai vi tri dang dung";
    public static final String BUTTON_HEADPHONE = "su kien tu tai nghe";
    public static final String NEXT = "chuyen bai hat tiep theo";
    public static final String PREVIOUS = "quay lai bai truoc";
    public static final int REPEAT_ONE = 1;
    public static final int REPEAT_ALL = 2;
    public static final int REPEAT_NONE = 3;
    public static final String REPEAT_MODE = "che do lap lai bai hat";
    public static final String SHUFFLE_MODE = "che do random bai hat";
    public static final int RANDOM = 4;
    public static final String PROGRESS_SEEKBAR = "procress seekbar";
    public static final String TIME_CURRENT = "time current";
    public static final String TUA_NHANH = "tua_nhanh_bai_hat";
    public static final String IS_PLAYING = "trang thai bai hat";
    public static final String VI_TRI = "position_song_now";
    public static final String TURN_OFF_NOTIFICATION = "cancel notification";

    private MediaPlayer mPlayer = new MediaPlayer();
    private ArrayList<Music> mArr;
    private int mPosition;
    private Thread mThreadSeekbar;
    private NotificationMusic mNotification;
    private final IBinder mBinder = new MyBinded();
    private int mRepeatMode;
    private int mShuffle;
    int mCount = 0;

    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinded extends Binder {
        public MusicService getService() {
            return MusicService.this;
        }

        public Music getSongIsPlaying() {
            Music m = mArr.get(mPosition);
            return m;
        }
    }

    public void connect(){
        Log.d("thinhav", "adadaw");
    }

    @Override
    public void onCreate() {
        mNotification = new NotificationMusic(getApplicationContext());
        mArr = new ArrayList<Music>();
        final String sortOder = MediaStore.Audio.Media.TITLE + " ASC";
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        ComponentName rec = new ComponentName(getPackageName(),
                MusicMediaButton.class.getName());
        audioManager.registerMediaButtonEventReceiver(rec);

//        final Cursor c = getApplication().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, sortOder);
//        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
//            Music m = new Music();
//            m.setPath(c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA)));
//            m.setName_singer(c.getString(c.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
//            m.setName_song(c.getString(c.getColumnIndex(MediaStore.Audio.Media.TITLE)));
//            m.setDuration(c.getLong(c.getColumnIndex(MediaStore.Audio.Media.DURATION)));
//            m.setAlbumId(c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
//            mArr.add(m);
//        }
//        c.close();
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            final String actionIntent = intent.getAction();
            switch (actionIntent) {
                case PLAY_SONG_FROM_LIST:
                    mArr.clear();
                    ArrayList<Music> arr = (ArrayList<Music>) intent.getSerializableExtra(DANH_SACH_NHAC);
                    mArr.addAll(arr);
                    if (mPlayer.isPlaying()) {
                        mPlayer.release();
                    }
                    mPosition = intent.getIntExtra(PLAY_SONG_FROM_LIST, 0);
                    playSong(mPosition);
                    break;
                case PAUSE:
                    finishUpdateSeekbar();
                    pauseSong();
                    sendBroastReceiver(UPDATE_BUTTON);
                    break;
                case PLAY_CONTINUES:
                    playContinues();
                    sendBroastReceiver(UPDATE_BUTTON);
                    break;
                case NEXT:
                    moveSong(true);
                    break;
                case PREVIOUS:
                    moveSong(false);
                    break;
                case TUA_NHANH:
                    finishUpdateSeekbar();
                    int time = intent.getIntExtra(VI_TRI, -1);
                    mPlayer.seekTo(time);
                    mPlayer.start();
                    updateSeekbar();
                    break;
                case TURN_OFF_NOTIFICATION:
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(NotificationMusic.ID_NOTIFICATION);
                    break;
                case BUTTON_HEADPHONE:
                    mCount ++;
                    if(mCount >0 && mCount %2 == 0){
                        if(mPlayer.isPlaying() == true){
                            finishUpdateSeekbar();
                            pauseSong();
                            sendBroastReceiver(UPDATE_BUTTON);
                        } else{
                            playContinues();
                            sendBroastReceiver(UPDATE_BUTTON);
                        }
                    }
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onLoadComplete(Loader<Cursor> loader, Cursor c) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // ham phat nhac tiep o vi tri dang dung
    private void playContinues() {
        if (mPlayer.isPlaying() == false) {
            mPlayer.start();
        }
    }

    // ham tam dung nhac
    private void pauseSong() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    // ham choi nhac tai vi tri position
    private void playSong(int position) {
        final String pathMusic = mArr.get(position).getPath();
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setVolume(1, 1);

        try {
            if (null != pathMusic) {
                mPlayer.setDataSource(pathMusic);
                mPlayer.prepare();
                mPlayer.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateSeekbar();
        sendBroastReceiver(THONG_TIN_BAI_HAT);
        sendBroastReceiver(UPDATE_BUTTON);
        ContinuesSong(mPlayer);
    }

    private void ContinuesSong(MediaPlayer media) {
        media.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                SharedPreferences sharedPreferences = getSharedPreferences(REPEAT_MODE, MODE_PRIVATE);
                mRepeatMode = sharedPreferences.getInt(REPEAT_MODE, REPEAT_NONE);
                if (mRepeatMode == RANDOM) {
                    Random r = new Random();
                    int randomSong = r.nextInt(mArr.size() - 1);
                    playSong(randomSong);
                } else{
                    repeatSong(mRepeatMode);
                }
            }
        });
    }

    private void repeatSong(int modeRepeat) {
        int positionSongNext = mPosition;
        if (modeRepeat == REPEAT_ONE) {
            positionSongNext = mPosition;
        } else if (modeRepeat == REPEAT_ALL) {
            positionSongNext = mPosition + 1;
            if (positionSongNext == mArr.size() - 1) {
                positionSongNext = 0;
            }
        } else if (modeRepeat == REPEAT_NONE) {
            positionSongNext = mPosition + 1;
            if (positionSongNext == mArr.size()) {
                mPlayer.stop();
                return;
            }
        }
        playSong(positionSongNext);
    }

    private void moveSong(boolean isNext) {
        mPlayer.release();
        finishUpdateSeekbar();
        if (isNext == true) {
            mPosition = mPosition + 1;
            if (mPosition == mArr.size()){
                mPosition = 0;
            }
        } else {
            mPosition = mPosition - 1;
            if (mPosition == -1){
                mPosition = mArr.size() -1;
            }
        }
        playSong(mPosition);
        sendBroastReceiver(UPDATE_BUTTON);
    }

    // gui broadcast di de cap nhat thong tin cac giao dien
    private void sendBroastReceiver(String action) {
        Intent i = new Intent(action);
        Music m = mArr.get(mPosition);
        mNotification.onCreate(m, mPlayer.isPlaying());
        if (THONG_TIN_BAI_HAT.equals(action)) {
            Bundle bun = new Bundle();
            bun.putSerializable(THONG_TIN_BAI_HAT, m);
            i.putExtra(THONG_TIN_BAI_HAT, bun);
        } else if (UPDATE_BUTTON.equals(action)) {
            if (mPlayer.isPlaying() == true) {
                i.putExtra(IS_PLAYING, true);
            } else {
                i.putExtra(IS_PLAYING, false);
            }
        }
        sendBroadcast(i);
    }

    // hàm cập nhật seekbar
    public void updateSeekbar() {
        if (mPlayer.isPlaying() == false) {
            mPlayer.start();
        }
        mThreadSeekbar = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i != -1 ; i++) {
                    try {
                        Thread.sleep(1000);
                        Intent intent_seekbar = new Intent(PROGRESS_SEEKBAR);
                        intent_seekbar.putExtra(TIME_CURRENT, mPlayer.getCurrentPosition());
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
        sendBroastReceiver(UPDATE_BUTTON);
    }

    // hàm kết thúc cập nhật seekbar
    public void finishUpdateSeekbar() {
        if (mThreadSeekbar != null && mThreadSeekbar.isAlive()) {
            mThreadSeekbar.interrupt();
        }
    }

    public int getAudioSessionId() {
        return mPlayer.getAudioSessionId();
    }

    public boolean checkPlaying(){
        return mPlayer.isPlaying();
    }
}
