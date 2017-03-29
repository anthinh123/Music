package com.example.anvanthinh.music.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.anvanthinh.music.ListSongFragment;
import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.SongFragment;

/**
 * Created by An Van Thinh on 2/19/2017.
 */

public class OnePaneController extends ActivityController {
    private Toolbar mToolbar;

    public OnePaneController(AppCompatActivity a){
        super(a);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity.setContentView(R.layout.one_pane_activity);
        SongFragment songFragment = new SongFragment();
        mActivity.getSupportFragmentManager().beginTransaction().add(R.id.pane_list_music , songFragment).commit();
        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setTitle(R.string.music);
     }
}
