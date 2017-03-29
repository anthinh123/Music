package com.example.anvanthinh.music.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by An Van Thinh on 2/19/2017.
 */

public abstract class ActivityController extends AppCompatActivity {
    protected  AppCompatActivity mActivity;
    public ActivityController(AppCompatActivity _a){
        this.mActivity = _a;
    }
    public abstract void onCreate(Bundle save);
}
