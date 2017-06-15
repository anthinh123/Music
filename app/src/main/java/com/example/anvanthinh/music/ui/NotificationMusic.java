package com.example.anvanthinh.music.ui;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.example.anvanthinh.music.Controller.MainActivity;
import com.example.anvanthinh.music.Music;
import com.example.anvanthinh.music.MusicService;
import com.example.anvanthinh.music.R;

/**
 * Created by An Van Thinh on 4/12/2017.
 */

public class NotificationMusic {
    public static final int ID_NOTIFICATION = 100;
    private Context mContext;
    private RemoteViews mRemoteViews;

    public NotificationMusic(Context context) {
        mContext = context;
    }

    public PendingIntent getPendingIntent(String action) {
        Intent i = new Intent(mContext, MusicService.class);
        i.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(mContext, 0, i, 0);
        return pendingIntent;
    }

    public void onCreate(Music m, boolean isPlaying){
        mRemoteViews = new RemoteViews(mContext.getPackageName(), R.layout.notification_music);
        mRemoteViews.setTextViewText(R.id.name, m.getName_song());
        mRemoteViews.setOnClickPendingIntent(R.id.next, getPendingIntent(MusicService.NEXT));
        mRemoteViews.setOnClickPendingIntent(R.id.previous, getPendingIntent(MusicService.PREVIOUS));
        mRemoteViews.setOnClickPendingIntent(R.id.cancel, getPendingIntent(MusicService.TURN_OFF_NOTIFICATION));
        if (isPlaying) {
            mRemoteViews.setOnClickPendingIntent(R.id.play_pause, getPendingIntent(MusicService.PAUSE));
            mRemoteViews.setImageViewResource(R.id.play_pause, R.drawable.ic_pause_white_48dp);
        } else {
            mRemoteViews.setOnClickPendingIntent(R.id.play_pause, getPendingIntent(MusicService.PLAY_CONTINUES));
            mRemoteViews.setImageViewResource(R.id.play_pause, R.drawable.ic_play_arrow_white_48dp);
        }
        showNotification();
    }

    public void showNotification() {
        Intent resultIntent = new Intent(mContext, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext).setSmallIcon(R.drawable.ic_library_music_white24dp).setContent(mRemoteViews);
        mBuilder.setCustomBigContentView(mRemoteViews).setAutoCancel(false);
        mBuilder.setContentIntent(pIntent);
        mBuilder.setOngoing(true);

        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(ID_NOTIFICATION, mBuilder.build());
    }
}
