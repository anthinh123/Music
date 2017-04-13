package com.example.anvanthinh.music.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anvanthinh.music.ListViewCallbacks;
import com.example.anvanthinh.music.Music;
import com.example.anvanthinh.music.R;

/**
 * Created by An Van Thinh on 2/19/2017.
 */

public class ScreenPlaySongFragment extends Fragment  {
    private TextView mSong;
    private TextView mArists;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.screen_play_song_fragment , null);
        mSong = (TextView) v.findViewById(R.id.song);
        mArists = (TextView) v.findViewById(R.id.singer);
        final String name = getArguments().getString(ListSongFragment.NAME_SONG);
        final String arists = getArguments().getString(ListSongFragment.ARISTS);
        updateUI(name, arists);
        return v;
    }

    public void updateUI(String name, String arists){
        mSong.setText(name);
        mArists.setText(arists);
    }



}
