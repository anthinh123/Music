package com.example.anvanthinh.music.Controller;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.anvanthinh.music.ItemClickRecycleView;
import com.example.anvanthinh.music.MusicMediaButton;
import com.example.anvanthinh.music.MusicService;
import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.ui.ListSongFragment;
import com.example.anvanthinh.music.ui.MusicFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityController mController;
    private boolean isTablet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isTablet = this.getResources().getBoolean(R.bool.isTablet);
        if(isTablet == true){
            mController = new TwoPaneController(this);
        }else{
            mController = new OnePaneController(MainActivity.this);
        }
        mController.onCreate(savedInstanceState);
        // dang ki nhan su kien tu tai nghe
        IntentFilter filter = new IntentFilter(Intent.ACTION_MEDIA_BUTTON);
        MusicMediaButton r = new MusicMediaButton();
        registerReceiver(r, filter);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mController.onStop();
        Log.d("thinhavb", "MainActivity - stop");
    }

    public MusicFragment.OnNewSongPlayListener getOnNewSongPlayListener() {
        return mController;
    }

    public void replaceFragment(Fragment listSongFragment, int tabAlbum) {
        mController.replaceFragment(listSongFragment, tabAlbum);
    }

    @Override
    public void onBackPressed() {
        mController.onBackPressed();
    }
}
