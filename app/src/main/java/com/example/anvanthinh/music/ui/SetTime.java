package com.example.anvanthinh.music.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.example.anvanthinh.music.Controller.AlarmReceiver;
import com.example.anvanthinh.music.R;

/**
 * Created by An Van Thinh on 5/25/2017.
 */

public class SetTime extends Activity{
    private static final int ID_SET_TIME = 0;
    private CrystalSeekbar mSeekbar;
    private Button mButton;
    private TextView mStart, mEnd;
    private int mTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_time);
        mSeekbar = (CrystalSeekbar) findViewById(R.id.seekbar_set_time);
        mButton = (Button) findViewById(R.id.ok);
        mStart = (TextView) findViewById(R.id.start);
        mEnd = (TextView) findViewById(R.id.end);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();
            }
        });
        mSeekbar.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number value) {
                mStart.setText(value+"");
            }
        });


    }

    private void setTime(){
        int time  = Integer.parseInt(mStart.getText() + "");
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), ID_SET_TIME, intent, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (time * 60 * 1000), pendingIntent);
        Toast.makeText(this, getResources().getString(R.string.turn_off_music)
                + " " + time + " " + getResources().getString(R.string.minutes), Toast.LENGTH_SHORT).show();
        finish();
    }
}
