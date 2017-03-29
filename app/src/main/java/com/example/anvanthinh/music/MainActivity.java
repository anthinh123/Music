package com.example.anvanthinh.music;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.anvanthinh.music.Controller.ActivityController;
import com.example.anvanthinh.music.Controller.OnePaneController;
import com.example.anvanthinh.music.Controller.TwoPaneController;

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
}
