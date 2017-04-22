package com.example.anvanthinh.music.ui;

import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.adapter.SongPagerAdapter;
import com.example.anvanthinh.music.Animation.ZoomOutPageTransformer;
import com.ogaclejapan.smarttablayout.SmartTabLayout;


public class MusicFragment extends Fragment  {
    private SongPagerAdapter mAdapter;
    private ViewPager mViewPager;
    private InforMusicMini mMiniInfor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.song_fragment, container, false);
        mMiniInfor = (InforMusicMini)v.findViewById(R.id.mini_infor_fragmnet);
        mMiniInfor.setFragment(this);

        mAdapter = new SongPagerAdapter(getActivity().getSupportFragmentManager(), getContext());
        mViewPager = (ViewPager) v.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mAdapter);
        SmartTabLayout viewPagerTab = (SmartTabLayout) v.findViewById(R.id.viewpager_catalogies);
        viewPagerTab.setViewPager(mViewPager);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        return v;
    }

    public void showMiniInfor(){
        mMiniInfor.setVisibility(View.VISIBLE);
    }

    public void movePlaySong(Cursor cursor){
        ScreenPlaySongFragment f = new ScreenPlaySongFragment();
        final String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        final String arists = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        final long duration  = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        Bundle bun = new Bundle();
        bun.putString(ListSongFragment.NAME_SONG, name);
        bun.putString(ListSongFragment.ARISTS, arists);
        bun.putLong(ListSongFragment.DURATION, duration);
        f.setArguments(bun);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.pane_list_music, f).commit();
    }

    public interface OnNewSongPlayListener {
        void onUpdateMiniInfor(Cursor c);
    }
}
