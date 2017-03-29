package com.example.anvanthinh.music.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.adapter.SongPagerAdapter;
import com.example.anvanthinh.music.adapter.ZoomOutPageTransformer;
import com.ogaclejapan.smarttablayout.SmartTabLayout;


public class SongFragment extends Fragment  {
    private SongPagerAdapter mAdapter;
    private ViewPager mViewPager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.song_fragment, container, false);
        mAdapter = new SongPagerAdapter(getActivity().getSupportFragmentManager(), getContext());
        mViewPager = (ViewPager) v.findViewById(R.id.viewpager);
        mViewPager.setAdapter(mAdapter);
        SmartTabLayout viewPagerTab = (SmartTabLayout) v.findViewById(R.id.viewpager_catalogies);
        viewPagerTab.setViewPager(mViewPager);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        return v;
    }
}
