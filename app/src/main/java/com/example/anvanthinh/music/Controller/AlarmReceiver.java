package com.example.anvanthinh.music.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.anvanthinh.music.MusicService;

import static java.security.AccessController.getContext;

/**
 * Created by An Van Thinh on 5/25/2017.
 */

public class AlarmReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MusicService.class);
        i.setAction(MusicService.PAUSE);
        context.startService(i);
    }


}
