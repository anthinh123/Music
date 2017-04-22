package com.example.anvanthinh.music.ui;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.anvanthinh.music.Music;
import com.example.anvanthinh.music.MusicService;
import com.example.anvanthinh.music.R;

import java.text.SimpleDateFormat;

/**
 * Created by An Van Thinh on 2/19/2017.
 */

public class ScreenPlaySongFragment extends Fragment implements View.OnClickListener {
    private TextView mSongTextView;
    private TextView mAristsTextView;
    private ImageButton mList;
    private ImageButton mLoop, mPlay, mNext, mPrevious, mRandom;
    private boolean isPlaying = true;
    private BroadcastReceiver mReceiver;
    private SeekBar mSeekbar;
    private String mName;
    private String mArisits;
    private long mDuration;
    private TextView mTimeEnd, mTimeStart;
    private ServiceConnection mServiceConnection ; // ket noi voi service
    private boolean mIsBinder = false ; // kiem tra xem co ket noi voi service khong

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.screen_play_song_fragment , null);
        mSongTextView = (TextView) v.findViewById(R.id.song);
        mAristsTextView = (TextView) v.findViewById(R.id.singer);
        mList = (ImageButton) v.findViewById(R.id.list);
        mPlay = (ImageButton) v.findViewById(R.id.play_pause);
        mLoop = (ImageButton) v.findViewById(R.id.loop);
        mNext = (ImageButton) v.findViewById(R.id.next);
        mPrevious = (ImageButton) v.findViewById(R.id.previous);
        mRandom = (ImageButton) v.findViewById(R.id.random);
        mTimeStart = (TextView) v.findViewById(R.id.time_start);
        mTimeEnd = (TextView) v.findViewById(R.id.time_end);
        mSeekbar = (SeekBar) v.findViewById(R.id.seekbar);

        mList.setOnClickListener(this);
        mPlay.setOnClickListener(this);
        mLoop.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mPrevious.setOnClickListener(this);
        mRandom.setOnClickListener(this);
        mSeekbar.setOnClickListener(this);

        mName = getArguments().getString(ListSongFragment.NAME_SONG);
        mArisits = getArguments().getString(ListSongFragment.ARISTS);
        mDuration = getArguments().getLong(ListSongFragment.DURATION);
        updateUI(mName, mArisits, mDuration);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        mServiceConnection = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                mIsBinder = true;
//                MusicService.MyBinded binded = (MusicService.MyBinded) service;
//                Music m = binded.getInforMusicIsPlay();
//                updateUI(m.getName_song(), m.getName_singer(), m.getDuration());
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//                mIsBinder = false;
//            }
//        };

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTimeStart.setText(changeTime(progress) );
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Intent i = new Intent(getActivity(), MusicService.class);
                i.setAction(MusicService.TUA_NHANH);
                i.putExtra(MusicService.VI_TRI, seekBar.getProgress());
                getActivity().startService(i);
                mTimeStart.setText(changeTime(seekBar.getProgress()) );
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction() == MusicService.THONG_TIN_BAI_HAT){
                    Bundle bun = intent.getBundleExtra(MusicService.THONG_TIN_BAI_HAT);
                    Music m = (Music) bun.getSerializable(MusicService.THONG_TIN_BAI_HAT);
                    updateUI(m.getName_song(), m.getName_singer(), m.getDuration());
                } else if (intent.getAction() == MusicService.PROGRESS_SEEKBAR){
                    final int time_length_song = intent.getIntExtra(ListSongFragment.DURATION , 0);
                    final int time_current_song = intent.getIntExtra(MusicService.TIME_CURRENT , 0);
                    mSeekbar.setMax(time_length_song);
                    mSeekbar.setProgress(time_current_song);
                    mTimeStart.setText(changeTime(time_current_song) );
                }
            }
        };

        IntentFilter filter = new IntentFilter(MusicService.BROADCAST);
        filter.addAction(MusicService.THONG_TIN_BAI_HAT);
        filter.addAction(MusicService.PROGRESS_SEEKBAR);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mReceiver);
        if(!mIsBinder){
            getActivity().unbindService(mServiceConnection);
            mIsBinder = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.list:
                MusicFragment f = new MusicFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.pane_list_music, f).commit();
                break;
            case R.id.play_pause:
                if(isPlaying){
                    startService(MusicService.PAUSE);
                    setImagePlayButton(R.drawable.ic_play_circle_outline_white_24dp);
                    isPlaying = false;
                }else{
                    startService(MusicService.PLAY_CONTINUES);
                    setImagePlayButton(R.drawable.ic_pause_circle_outline_white_24dp);
                    isPlaying = true;
                }
                break;
            case R.id.next:
                mSeekbar.setProgress(0);
                startService(MusicService.NEXT);
                break;
            case R.id.previous:
                mSeekbar.setProgress(0);
                startService(MusicService.PREVIOUS);
                break;
            case R.id.loop:
                break;
            case R.id.random:
                break;
            case R.id.seekbar:
                updateSeebar();
                break;
        }
    }

    // ham update lai giao dien man hinh choi nhac
    private void updateUI(String name, String arists, long duration){
        mSongTextView.setText(name);
        mAristsTextView.setText(arists);
        final SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        mTimeEnd.setText(sdf.format(duration));
        mTimeStart.setText("00:00");
    }

    private void setImagePlayButton(int drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPlay.setImageDrawable(getActivity().getDrawable(drawable));
        }else{
            mPlay.setImageResource(drawable);
        }
    }

    private void startService(String action){
        Intent i = new Intent(getActivity(), MusicService.class);
        i.setAction(action);
        getActivity().startService(i);
    }

    private void randomSong() {
        Intent i = new Intent(getActivity(), MusicService.class);
        i.setAction(MusicService.PLAY_CONTINUES);
        getActivity().startService(i);
    }

    // ham cap nhat seekbar
    private void updateSeebar(){
        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTimeStart.setText(changeTime(progress) );
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Intent i = new Intent(getActivity(), MusicService.class);
                i.setAction(MusicService.TUA_NHANH);
                i.putExtra(MusicService.VI_TRI, seekBar.getProgress());
                getActivity().startService(i);
                mTimeStart.setText(changeTime(seekBar.getProgress()) );
            }
        });
    }

    private String changeTime(long duration){
        final SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String time  = sdf.format(duration);
        return time;
    }

}
