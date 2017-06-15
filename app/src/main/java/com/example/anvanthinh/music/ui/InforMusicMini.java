package com.example.anvanthinh.music.ui;

import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anvanthinh.music.Music;
import com.example.anvanthinh.music.MusicService;
import com.example.anvanthinh.music.R;

/**
 * Created by An Van Thinh on 4/5/2017.
 */

public class InforMusicMini extends LinearLayout implements View.OnClickListener {
    public static final String SONG_NAME = "ten bai hat";
    public static final String ARISRT = "ten ca si";
    private ImageView mAvatar;
    private TextView mNameSong;
    private TextView mNameSinger;
    private ImageButton mPlayPause;
    private LinearLayout mInforSong;
    private boolean mIsPlaying;
    private MusicFragment mListSongFragment;
    private ObjectAnimator mAnimRotate; // animator xoay tron cho album
    private BroadcastReceiver mReceiver; // nhan thong tin bai hat trong service
    private long mSongDuration;

    public InforMusicMini(Context context) {
        super(context);
    }

    public InforMusicMini(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.infor_music_mini, this, true);
    }

    public void setFragment(MusicFragment f) {
        mListSongFragment = f;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mNameSong = (TextView) findViewById(R.id.song);
        mNameSinger = (TextView) findViewById(R.id.singer);
        mPlayPause = (ImageButton) findViewById(R.id.play_pause);
        mInforSong = (LinearLayout) findViewById(R.id.infor_song);

        mInforSong.setOnClickListener(this);
        mPlayPause.setOnClickListener(this);

        mAnimRotate = ObjectAnimator.ofFloat(mAvatar, "rotation", 0, 360);
        mAnimRotate.setDuration(10000);
        mAnimRotate.setRepeatCount(ObjectAnimator.INFINITE);
        mAnimRotate.setInterpolator(new LinearInterpolator());
        mAnimRotate.setRepeatMode(ObjectAnimator.INFINITE);
        mAnimRotate.start();

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == MusicService.THONG_TIN_BAI_HAT) {
                    Bundle bun = intent.getBundleExtra(MusicService.THONG_TIN_BAI_HAT);
                    Music m = (Music) bun.get(MusicService.THONG_TIN_BAI_HAT);
                    mSongDuration = m.getDuration();
                    updateSong(m);
                } else if (intent.getAction() == MusicService.UPDATE_BUTTON){
                    boolean isPlaying = intent.getBooleanExtra(MusicService.IS_PLAYING, false);
                    mIsPlaying = isPlaying;
                    updateButton(getContext(), mPlayPause, isPlaying);
                }
            }
        };

        IntentFilter filter = new IntentFilter(MusicService.THONG_TIN_BAI_HAT);
        filter.addAction(MusicService.THONG_TIN_BAI_HAT);
        filter.addAction(MusicService.UPDATE_BUTTON);
        getContext().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_pause:
                if (mIsPlaying == true) {
                    mAnimRotate.pause();
                    Intent i = new Intent(getContext(), MusicService.class);
                    i.setAction(MusicService.PAUSE);
                    getContext().startService(i);
                    mIsPlaying = false;
                } else {
                    mAnimRotate.resume();
                    Intent i = new Intent(getContext(), MusicService.class);
                    i.setAction(MusicService.PLAY_CONTINUES);
                    getContext().startService(i);
                    mIsPlaying = true;
                }
                break;
            case R.id.infor_song:
                Bundle bun = new Bundle();
                bun.putBoolean(MusicService.IS_PLAYING, mIsPlaying);
                bun.putString(SONG_NAME, mNameSong.getText()+"");
                bun.putString(ARISRT, mNameSinger.getText()+"");
                bun.putLong(ListSongFragment.DURATION, mSongDuration);
                mListSongFragment.moveScreenPlaySong(bun);
                break;
        }
    }

    public void update(Cursor cursor, int position) {
    }

    public void updateSong(Music m) {
        mNameSong.setText(m.getName_song());
        mNameSinger.setText(m.getName_singer());
    }

    public void updateButton(Context context, ImageButton playPause, boolean isPlaying) {
        if (isPlaying == true){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                playPause.setImageDrawable(context.getDrawable(R.drawable.ic_pause_white_48dp));
            } else {
                playPause.setBackgroundResource(R.drawable.ic_pause_white_48dp);
            }
            mAnimRotate.resume();
        } else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                playPause.setImageDrawable(context.getDrawable(R.drawable.ic_play_arrow_white_48dp));
            } else {
                playPause.setBackgroundResource(R.drawable.ic_play_arrow_white_48dp);
            }
            mAnimRotate.pause();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getContext().unregisterReceiver(mReceiver);
    }

}
