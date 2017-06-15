package com.example.anvanthinh.music.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.anvanthinh.music.Music;
import com.example.anvanthinh.music.MusicService;
import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.adapter.ImageSongAdapter;
import com.example.anvanthinh.music.adapter.ListAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by An Van Thinh on 2/19/2017.
 */

public class ScreenPlaySongFragment extends Fragment implements View.OnClickListener, View.OnTouchListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int URL_IMAGE = 0;
    private static final int SWIPE_VELOCITY = 100;
    private static final int SWIPE_LONG = 100;
    private static final long DURATION_ALPHA = 5000;
    private TextView mSongTextView;
    private TextView mAristsTextView;
    private ImageButton mList;
    private ImageButton mLoop, mPlay, mNext, mPrevious, mRandom;
    private boolean mIsPlaying;
    private BroadcastReceiver mReceiver;
    private GestureDetector mGesture;
    private SeekBar mSeekbar;
    private TextView mTimeEnd, mTimeStart;
    private ImageView mImageAlbum;
    // private ViewPager mViewPager; // viewpager dung de hien thi noi dung anh cho moi bai hat
    private ImageSongAdapter mImageAdapter; // adapter cho viewpager
    private int mPosition; // vi tri bai hat dang choi
    private ArrayList<Long> mArrAlbumId;
    private long mAlbumId;
    private ObjectAnimator mAnimRotate; // animator xoauy tron cho anh album
    private MusicService mService;
    private boolean isBinded = false; // ket noi toi service chua

    private ServiceConnection mServiceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MyBinded binder = (MusicService.MyBinded) service;
            mService = binder.getService();
            Music m = binder.getSongIsPlaying();
            updateImageAlbum(m.getAlbumId());
            isBinded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBinded = false;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(getActivity(), MusicService.class);
        getActivity().bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.screen_play_song_fragment, null);
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
        mImageAlbum = (ImageView) v.findViewById(R.id.image_album);
        // mViewPager = (ViewPager) v.findViewById(R.id.viewpager_song);
        //mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mGesture = new GestureDetector(getContext(), new ChangeImageGesture());

        mImageAlbum.setOnTouchListener(this);
        mList.setOnClickListener(this);
        mPlay.setOnClickListener(this);
        mLoop.setOnClickListener(this);
        mNext.setOnClickListener(this);
        mPrevious.setOnClickListener(this);
        mRandom.setOnClickListener(this);
        mSeekbar.setOnClickListener(this);

        mAnimRotate = ObjectAnimator.ofFloat(mImageAlbum, "rotation", 0, 360);
        mAnimRotate.setDuration(20000);
        mAnimRotate.setRepeatCount(ObjectAnimator.INFINITE);
        mAnimRotate.setInterpolator(new LinearInterpolator());
        mAnimRotate.setRepeatMode(ObjectAnimator.INFINITE);
        mAnimRotate.start();

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle bundle) {
        super.onActivityCreated(bundle);
        mArrAlbumId = new ArrayList<Long>(); // danh sach id album
        Bundle bun = getArguments();
        if (bun != null) {
            mIsPlaying = bun.getBoolean(MusicService.IS_PLAYING);
            long durattion = bun.getLong(ListSongFragment.DURATION);
            final SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
            mTimeEnd.setText(sdf.format(durattion));
            mSeekbar.setMax((int) durattion);
            updateText(bun.getString(InforMusicMini.SONG_NAME), bun.getString(InforMusicMini.ARISRT));
            updateButton(bun.getBoolean(MusicService.IS_PLAYING));
        }
        getActivity().getSupportLoaderManager().initLoader(URL_IMAGE, null, this);

        mSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mTimeStart.setText(changeTime(progress));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Intent i = new Intent(getActivity(), MusicService.class);
                i.setAction(MusicService.TUA_NHANH);
                int a = seekBar.getProgress();
                i.putExtra(MusicService.VI_TRI, seekBar.getProgress());
                getActivity().startService(i);
                mTimeStart.setText(changeTime(seekBar.getProgress()));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == MusicService.THONG_TIN_BAI_HAT) {
                    Bundle bun = intent.getBundleExtra(MusicService.THONG_TIN_BAI_HAT);
                    Music m = (Music) bun.getSerializable(MusicService.THONG_TIN_BAI_HAT);
                    updateUI(m.getName_song(), m.getName_singer(), m.getDuration());
                    mAlbumId = m.getAlbumId();
                    updateImageAlbum(mAlbumId);
                    mSeekbar.setMax((int) m.getDuration());
                    startAnimationAlbumView();
                } else if (intent.getAction() == MusicService.PROGRESS_SEEKBAR) {
                    final int time_length_song = intent.getIntExtra(ListSongFragment.DURATION, 0);
                    final int time_current_song = intent.getIntExtra(MusicService.TIME_CURRENT, 0);
                    updateSeebar(time_length_song, time_current_song);
                } else if (intent.getAction() == MusicService.UPDATE_BUTTON) {
                    mIsPlaying = intent.getBooleanExtra(MusicService.IS_PLAYING, false);
                    updateButton(mIsPlaying);
                }
            }
        };

        IntentFilter filter = new IntentFilter(MusicService.BROADCAST);
        filter.addAction(MusicService.THONG_TIN_BAI_HAT);
        filter.addAction(MusicService.UPDATE_BUTTON);
        filter.addAction(MusicService.PROGRESS_SEEKBAR);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mReceiver);
        if (isBinded == true) {
            getActivity().unbindService(mServiceConnection);
            isBinded = false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.list:
                MusicFragment f = new MusicFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.pane_list_music, f).commit();
                break;
            case R.id.play_pause:
                if (mIsPlaying == true) {
                    startService(getActivity(), MusicService.PAUSE);
                    mIsPlaying = false;
                } else {
                    startService(getActivity(), MusicService.PLAY_CONTINUES);
                    mIsPlaying = true;
                }
                updateButton(mIsPlaying);
                break;
            case R.id.next:
                mSeekbar.setProgress(0);
                startService(getActivity(), MusicService.NEXT);
                mPosition = mPosition + 1;
                break;
            case R.id.previous:
                mSeekbar.setProgress(0);
                startService(getActivity(), MusicService.PREVIOUS);
                mPosition = mPosition - 1;
                break;
            case R.id.loop:
                repeatSong();
                break;
            case R.id.random:
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MusicService.SHUFFLE_MODE, Context.MODE_PRIVATE);
                boolean shuffMode = sharedPreferences.getBoolean(MusicService.SHUFFLE_MODE, false);
                updateShuff(shuffMode);
                break;
        }
    }

    private void updateShuff(boolean shuffMode) {
        if (shuffMode){
            mRandom.setImageResource(R.drawable.ic_shuffle_orrange_24dp);
        } else{
            mRandom.setImageResource(R.drawable.ic_shuffle_white_48dp);
        }
    }

    private void repeatSong() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MusicService.REPEAT_MODE, Context.MODE_PRIVATE);
        int repeatMode = sharedPreferences.getInt(MusicService.REPEAT_MODE, MusicService.REPEAT_NONE);
        updateRepeatButton(repeatMode);
    }

    private void updateRepeatButton(int repeatMode) {
        if (repeatMode == MusicService.REPEAT_NONE) {
            mLoop.setImageResource(R.drawable.ic_repeat_white_24dp);
        } else if (repeatMode == MusicService.REPEAT_ALL) {
            mLoop.setImageResource(R.drawable.ic_repeat__all_orange_24dp);
        } else if (repeatMode == MusicService.REPEAT_ONE) {
            mLoop.setImageResource(R.drawable.ic_repeat_one_orange_24dp);
        }
    }

    // ham update lai giao dien man hinh choi nhac
    private void updateUI(String name, String arists, long duration) {
        updateText(name, arists);
        final SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        mTimeEnd.setText(sdf.format(duration));
        mTimeStart.setText("00:00");
    }

    // ham update anh album
    private void updateImageAlbum(long mAlbumId) {
        ListAdapter.setImageAvatar(getContext(), mImageAlbum, mAlbumId);
    }

    private void updateText(String name, String arists) {
        mSongTextView.setText(name);
        mAristsTextView.setText(arists);
    }

    public static void startService(Activity activity, String action) {
        Intent i = new Intent(activity, MusicService.class);
        i.setAction(action);
        activity.startService(i);
    }

    private void randomSong() {
        Intent i = new Intent(getActivity(), MusicService.class);
        i.setAction(MusicService.PLAY_CONTINUES);
        getActivity().startService(i);
    }

    // ham cap nhat seekbar
    private void updateSeebar(int longTime, int currentTime) {
        mSeekbar.setProgress(currentTime);
        mTimeStart.setText(changeTime(currentTime));
    }

    private String changeTime(long duration) {
        final SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String time = sdf.format(duration);
        return time;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final String sortOder = MediaStore.Audio.Media.TITLE + " ASC";
        final String[] projection = new String[]{MediaStore.Audio.Media.ALBUM_ID};
        if (id == URL_IMAGE) {
            CursorLoader cursorLoader = new CursorLoader(getActivity(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOder);
            return cursorLoader;
        } else {
            return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        for (data.moveToFirst(); !data.isAfterLast(); data.moveToNext()) {
            mArrAlbumId.add(data.getLong(data.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void updateButton(boolean isPlaying) {
        if (isPlaying == true) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mPlay.setImageDrawable(getContext().getDrawable(R.drawable.ic_pause_circle_outline_white_24dp));
            } else {
                mPlay.setBackgroundResource(R.drawable.ic_pause_circle_outline_white_24dp);
            }
            mAnimRotate.resume();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mPlay.setImageDrawable(getContext().getDrawable(R.drawable.ic_play_circle_outline_white_24dp));
            } else {
                mPlay.setBackgroundResource(R.drawable.ic_play_circle_outline_white_24dp);
            }
            mAnimRotate.pause();
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.image_album) {
            mGesture.onTouchEvent(event);
        }
        return true;
    }

    // su dung animation khi chuyen anh bai hat
    private void startAnimationAlbumView() {
        mAnimRotate.end();
//        ObjectAnimator fadeIn =  ObjectAnimator.ofFloat(mImageAlbum, View.ALPHA, 1f, 0f);
//        fadeIn.setDuration(DURATION_ALPHA);
//        fadeIn.start();
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(mImageAlbum, View.ALPHA, 0f, 1f);
        fadeOut.setDuration(DURATION_ALPHA);
        fadeOut.start();
        mAnimRotate.start();
    }

    class ChangeImageGesture extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // vuot tu phai sang trai
            if (e1.getX() - e2.getX() > SWIPE_LONG && Math.abs(velocityX) > SWIPE_VELOCITY) {
                startService(getActivity(), MusicService.NEXT);
            } else if (e2.getX() - e1.getX() > SWIPE_LONG && Math.abs(velocityX) > SWIPE_VELOCITY) { //vuot tu trai sang phai
                startService(getActivity(), MusicService.PREVIOUS);
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
}



























