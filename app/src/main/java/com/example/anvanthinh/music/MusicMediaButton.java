package com.example.anvanthinh.music;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.IBinder;
import android.telecom.ConnectionService;
import android.util.Log;
import android.view.KeyEvent;

import static android.view.KeyEvent.KEYCODE_HEADSETHOOK;

/**
 * Created by An Van Thinh on 4/24/2017.
 */

public class MusicMediaButton extends BroadcastReceiver {
    private static final int MSG_LONGPRESS_TIMEOUT = 1;
    private static final int LONG_PRESS_DELAY = 500;
    private boolean isNext = false;
    private boolean isPlaying = false;
    private MusicService mService;

    private static boolean mDown = false;
    private static long mLastClickTime = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        int mCount = 0;
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intentAction)) {
            Log.d("thinhavb:", "rut tai nghe");
            Intent i = new Intent(context, MusicService.class);
            i.setAction(MusicService.PAUSE);
            context.startService(i);
        } else if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            Log.d("thinhavb:", mCount + "");
            Intent i2 = new Intent(context, MusicService.class);
            i2.setAction(MusicService.BUTTON_HEADPHONE);
            context.startService(i2);
        }
//        else if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
//            Log.d("thinhavb:", "bam nut tai nghe");
//            KeyEvent event = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
//
//            if (event == null) {
//                return;
//            }
//
//            int keycode = event.getKeyCode();
//            int action = event.getAction();
//            long eventtime = event.getEventTime();
////
////            // single quick press: pause/resume.
////            // double press: next track
////            // long press: previous
////
//            String command = null;
//            switch (keycode) {
//                case KeyEvent.KEYCODE_MEDIA_STOP:
//                    command = MusicService.PAUSE;
//                    Log.d("thinhavb:", "STOP");
//                    break;
//                case KEYCODE_HEADSETHOOK:
//                case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
//                    Log.d("thinhavb:", "PLAY_PAUSE");
////                    command = MusicService.PAUSE;
//                    break;
//                case KeyEvent.KEYCODE_MEDIA_NEXT:
//                    command = MusicService.NEXT;
//                    Log.d("thinhavb:", "NEXT");
////                    Intent i = new Intent(context, MusicService.class);
////                    i.setAction(MusicService.NEXT);
////                    context.startService(i);
//                    break;
//                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
//                    command = MusicService.PREVIOUS;
//                    Log.d("thinhavb:", "PREVIOUS");
//                    break;
//                case KeyEvent.KEYCODE_MEDIA_PAUSE:
//                    command = MusicService.PAUSE;
//                    Log.d("thinhavb:", "PAUSE");
//                    break;
//                case KeyEvent.KEYCODE_MEDIA_PLAY:
//                    command = MusicService.PLAY_CONTINUES;
//                    Log.d("thinhavb:", "PLAY_CONTINUES");
////                    Intent i3 = new Intent(context, MusicService.class);
////                    i3.setAction(MusicService.PLAY_CONTINUES);
////                    context.startService(i3);
//                    break;
//            }
////
////            if (command != null) {
////                if (action == KeyEvent.ACTION_DOWN) {
////                    if (mDown) {
////                        if ((MusicService.PAUSE.equals(command) ||
////                                MusicService.PLAY_CONTINUES.equals(command))
////                                && mLastClickTime != 0
////                                && eventtime - mLastClickTime > LONG_PRESS_DELAY) {
////                        }
////                    } else if (event.getRepeatCount() == 0) {
////                        // only consider the first event in a sequence, not the repeat events,
////                        // so that we don't trigger in cases where the first event went to
////                        // a different app (e.g. when the user ends a phone call by
////                        // long pressing the headset button)
////
////                        // The service may or may not be running, but we need to send it
////                        // a command.
////                        Intent i = new Intent(context, MusicService.class);
////                        if (keycode == KeyEvent.KEYCODE_HEADSETHOOK &&
////                                eventtime - mLastClickTime < 300) {
////                            i.setAction(MusicService.NEXT);
////                            context.startService(i);
////                            mLastClickTime = 0;
////                        } else {
////                            context.startService(i);
////                            mLastClickTime = eventtime;
////                        }
////                        mDown = true;
////                    }
////                } else {
////                    mDown = false;
////                }
////                if (isOrderedBroadcast()) {
////                    abortBroadcast();
////                }
////            }
//        }
    }


}
