package com.example.anvanthinh.music.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.anvanthinh.music.ListViewCallbacks;

/**
 * Created by An Van Thinh on 2/19/2017.
 */

public abstract class ActivityController extends AppCompatActivity {
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
        return super.onOptionsItemSelected(item);
    }

}
