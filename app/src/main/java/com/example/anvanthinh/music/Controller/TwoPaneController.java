package com.example.anvanthinh.music.Controller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.example.anvanthinh.music.Animation.BlurBuilder;
import com.example.anvanthinh.music.ui.ListSongFragment;
import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.ui.ScreenPlaySongFragment;
import com.example.anvanthinh.music.ui.SongFragment;

/**
 * Created by An Van Thinh on 2/19/2017.
 */

public class TwoPaneController extends ActivityController {
    private LinearLayout mBackground;

    public TwoPaneController(AppCompatActivity a){
        super(a);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity.setContentView(R.layout.two_pane_activity);
        mBackground = (LinearLayout) mActivity.findViewById(R.id.two_pane);

        SongFragment listSongFragment = new SongFragment();
        ScreenPlaySongFragment playSongFragment = new ScreenPlaySongFragment();
        mActivity.getSupportFragmentManager().beginTransaction().add(R.id.pane_list_music , listSongFragment).commit();
        mActivity.getSupportFragmentManager().beginTransaction().add(R.id.screen_play_song , playSongFragment).commit();

        Bitmap originalBitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.background_list_song);
        Bitmap blurredBitmap = BlurBuilder.blur( mActivity, originalBitmap );
        mBackground.setBackgroundDrawable( new BitmapDrawable( mActivity.getResources(), blurredBitmap ) );
    }
}
