package com.example.anvanthinh.music.Controller;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.ui.ListSongFragment;
import com.example.anvanthinh.music.ui.MusicFragment;

/**
 * Created by An Van Thinh on 2/19/2017.
 */

public abstract class ActivityController extends AppCompatActivity implements MusicFragment.OnNewSongPlayListener {
    protected AppCompatActivity mActivity;

    public ActivityController(AppCompatActivity _a) {
        this.mActivity = _a;
    }

    public abstract void onCreate(Bundle save);

    public abstract void onStop();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.time:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdateMiniInfor(Cursor c, int position) {

    }

    public void replaceFragment(Fragment listSongFragment, int tabAlbum) {

    }

    @Override
    public void onBackPressed() {
    }
}
