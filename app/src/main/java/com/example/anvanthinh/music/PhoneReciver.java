package com.example.anvanthinh.music;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * Created by An Van Thinh on 6/15/2017.
 */

public class PhoneReciver extends BroadcastReceiver {
    private Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        mContext = context;
        MyPhoneStateListener phoneListener=new MyPhoneStateListener();
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        telephony.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    public class MyPhoneStateListener extends PhoneStateListener {
        public void onCallStateChanged(int state,String incomingNumber){
            switch(state){
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d("thinhavb", "IDLE");
                    Intent i2 = new Intent(mContext, MusicService.class);
                    i2.setAction(MusicService.PLAY_CONTINUES);
                    mContext.startService(i2);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d("thinhavb", "OFFHOOK");
                    Intent i1 = new Intent(mContext, MusicService.class);
                    i1.setAction(MusicService.PAUSE);
                    mContext.startService(i1);
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    Intent i = new Intent(mContext, MusicService.class);
                    i.setAction(MusicService.PAUSE);
                    mContext.startService(i);
                    break;
            }
        }
    }
}
