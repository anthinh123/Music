package com.example.anvanthinh.music.Controller;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.example.anvanthinh.music.Animation.BlurBuilder;
import com.example.anvanthinh.music.ListViewCallbacks;
import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.ui.ListSongFragment;
import com.example.anvanthinh.music.ui.SongFragment;

/**
 * Created by An Van Thinh on 2/19/2017.
 */

public class OnePaneController extends ActivityController {
    private Toolbar mToolbar;
    private LinearLayout mBackground;

    public OnePaneController(AppCompatActivity a){
        super(a);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity.setContentView(R.layout.one_pane_activity);
        mBackground = (LinearLayout) mActivity.findViewById(R.id.one_pane);

        SongFragment songFragment = new SongFragment();
        mActivity.getSupportFragmentManager().beginTransaction().add(R.id.pane_list_music , songFragment).commit();

        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(mToolbar);
        mActivity.getSupportActionBar().setTitle(R.string.music);

        Bitmap originalBitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.background_list_song);
        Bitmap blurredBitmap = BlurBuilder.blur( mActivity, originalBitmap );
        mBackground.setBackgroundDrawable( new BitmapDrawable( mActivity.getResources(), blurredBitmap ) );
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mActivity.getMenuInflater().inflate(R.menu.listview_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = mActivity.getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
