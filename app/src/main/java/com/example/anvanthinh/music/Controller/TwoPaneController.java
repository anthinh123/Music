package com.example.anvanthinh.music.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.anvanthinh.music.ui.ListSongFragment;
import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.ui.ScreenPlaySongFragment;

/**
 * Created by An Van Thinh on 2/19/2017.
 */

public class TwoPaneController extends ActivityController {

    public TwoPaneController(AppCompatActivity a){
        super(a);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity.setContentView(R.layout.two_pane_activity);
        ListSongFragment listSongFragment = new ListSongFragment();
        ScreenPlaySongFragment playSongFragment = new ScreenPlaySongFragment();
        mActivity.getSupportFragmentManager().beginTransaction().add(R.id.pane_list_music , listSongFragment).commit();
        mActivity.getSupportFragmentManager().beginTransaction().add(R.id.screen_play_song , playSongFragment).commit();
    }
}
