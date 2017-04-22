package com.example.anvanthinh.music.Controller;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.anvanthinh.music.ListViewCallbacks;
import com.example.anvanthinh.music.ui.MusicFragment;

/**
 * Created by An Van Thinh on 2/19/2017.
 */

public abstract class ActivityController extends AppCompatActivity implements MusicFragment.OnNewSongPlayListener {
    protected  AppCompatActivity mActivity;

    public ActivityController(AppCompatActivity _a){
        this.mActivity = _a;
    }
    public abstract void onCreate(Bundle save);

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

        }
        return super.onOptionsItemSelected(item);
    }

}
