package com.example.anvanthinh.music.Controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.Equalizer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.anvanthinh.music.Animation.BlurBuilder;
import com.example.anvanthinh.music.Music;
import com.example.anvanthinh.music.MusicService;
import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.ui.InforMusicMini;
import com.example.anvanthinh.music.ui.ListSongFragment;
import com.example.anvanthinh.music.ui.MusicFragment;
import com.example.anvanthinh.music.ui.SetTime;

/**
 * Created by An Van Thinh on 2/19/2017.
 */

public class OnePaneController extends ActivityController implements MusicFragment.OnNewSongPlayListener, SearchView.OnQueryTextListener {
    private Toolbar mToolbar;
    private LinearLayout mBackground;
    private SearchView mSearchView;
    private QuerySearch mQuerySearch;
    private MusicService mService;
    private boolean isBinded = false; // ket noi toi service chua
    private MusicFragment songFragment;
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MyBinded binder = (MusicService.MyBinded) service;
            mService = binder.getService();
            isBinded = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBinded = false;
        }
    };

    public OnePaneController(AppCompatActivity a) {
        super(a);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity.setContentView(R.layout.one_pane_activity);
        mBackground = (LinearLayout) mActivity.findViewById(R.id.one_pane);

        songFragment = new MusicFragment();
        mActivity.getSupportFragmentManager().beginTransaction().add(R.id.pane_list_music, songFragment).commit();

        mToolbar = (Toolbar) mActivity.findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.music);
        mToolbar.inflateMenu(R.menu.listview_menu);
        MenuItem itemSearch = mToolbar.getMenu().findItem(R.id.search);
        mSearchView = (SearchView) itemSearch.getActionView();
        mSearchView.setOnQueryTextListener(this);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.time:
                        startSetTime();
                        break;
                    case R.id.equalizor:
                        startEqualizor();
                        break;

                }
                return false;
            }
        });

        Bitmap originalBitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.background);
        Bitmap blurredBitmap = BlurBuilder.blur(mActivity, originalBitmap);
        mBackground.setBackgroundDrawable(new BitmapDrawable(mActivity.getResources(), blurredBitmap));
        // thinhav: dung de bo mau o status_bar
        mActivity.getWindow().
                setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        Intent intent = new Intent(mActivity, MusicService.class);
        mActivity.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void onStop() {
        Log.d("thinhavb", "onepane - stop");
        if (isBinded == true) {
            mActivity.unbindService(mServiceConnection);
            isBinded = false;
            Log.d("thinhavb", "da huy connect toi service");
        }
    }

    // ham goi den musicFX chinh am trong ROM
    private void startEqualizor() {
        final Intent effects = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
        int id;
        if (isBinded == true) {
            id = mService.getAudioSessionId();
        } else {
            id = -1;
        }
        effects.putExtra(AudioEffect.EXTRA_AUDIO_SESSION, id);
        try {
            // The google MusicFX apps need to be started using startActivityForResult
            mActivity.startActivityForResult(effects, 111);
        } catch (final ActivityNotFoundException notFound) {
            Toast.makeText(mActivity, "Equalizer not found", Toast.LENGTH_SHORT).show();
        }
    }

    // ham dung de hen gio
    private void startSetTime() {
        Intent intent = new Intent(mActivity, SetTime.class);
        mActivity.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if(songFragment.isChangeTab() == true){
            songFragment.restoreTabPager();
        }else{
            FragmentManager fm = mActivity.getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            } else {
//                /super.onBackPressed();
            }
        }
    }

    @Override
    public void onUpdateMiniInfor(Cursor cursor, int position) {
        InforMusicMini mini = (InforMusicMini) mActivity.findViewById(R.id.mini_infor_fragmnet);
        mini.setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mini.update(cursor, position);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mQuerySearch.updateListView(newText.toString().trim());
        return false;
    }

    // ham dang ki cac lop nhan su kien tu OnePane
    public void setQuerySearch(QuerySearch q) {
        mQuerySearch = q;
    }

    public interface QuerySearch {
        void updateListView(String text);
    }

    public void replaceFragment(Fragment item, int tab){
        songFragment.replaceFragmentPager(item, tab);
    }

}
