package com.example.anvanthinh.music.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anvanthinh.music.ListViewCallbacks;
import com.example.anvanthinh.music.MusicService;
import com.example.anvanthinh.music.R;

/**
 * Created by An Van Thinh on 4/5/2017.
 */

public class InforMusicMini extends LinearLayout implements View.OnClickListener, ListViewCallbacks {
    private ImageView mAvatar;
    private TextView mNameSong;
    private TextView mNameSinger;
    private ImageButton mPlayPause;
    private LinearLayout mInforSong;
    private  boolean isPlaying;

    public  InforMusicMini(Context context) {
        super(context);
    }

    public InforMusicMini(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.infor_music_mini, this, true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mNameSong = (TextView)findViewById(R.id.song);
        mNameSinger = (TextView)findViewById(R.id.singer);
        mPlayPause = (ImageButton)findViewById(R.id.play_pause);
        mInforSong = (LinearLayout) findViewById(R.id.infor_song);
        mPlayPause.setImageDrawable(getContext().getDrawable(R.drawable.ic_play_arrow_black_24dp));
        mNameSong.setOnClickListener(this);
        mNameSinger.setOnClickListener(this);
        mInforSong.setOnClickListener(this);
        mPlayPause.setOnClickListener(this);
        Animation rotateAnimation  = AnimationUtils.loadAnimation(getContext(), R.anim.android_rotate_animation);
        mAvatar.startAnimation(rotateAnimation);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.play_pause:
                if(isPlaying == true){
                    Intent i = new Intent(getContext(), MusicService.class);
                    i.setAction(MusicService.PAUSE);
                    mPlayPause.setImageDrawable(getContext().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                    getContext().startService(i);
                    isPlaying = false;
                } else{
                    Intent i = new Intent(getContext(), MusicService.class);
                    i.setAction(MusicService.PLAY_CONTINUES);
                    getContext().startService(i);
                    mPlayPause.setImageDrawable(getContext().getDrawable(R.drawable.ic_pause_white_24dp));
                    isPlaying = true;
                }
                break;
            case R.id.infor_song:
                break;
        }
    }

    @Override
    public void update(Cursor cursor) {
        if (cursor != null){
            mNameSong.setText("" + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            mNameSinger.setText("" + cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            mPlayPause.setImageDrawable(getContext().getDrawable(R.drawable.ic_pause_white_24dp));
            isPlaying = true;
        }

    }
}
