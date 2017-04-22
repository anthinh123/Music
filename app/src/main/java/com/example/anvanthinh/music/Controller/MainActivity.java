package com.example.anvanthinh.music.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.anvanthinh.music.R;
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
    }

    public MusicFragment.OnNewSongPlayListener getOnNewSongPlayListener() {
        return mController;
    }
}
