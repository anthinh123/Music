package com.example.anvanthinh.music.adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.anvanthinh.music.MusicService;
import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.ui.ScreenPlaySongFragment;

import java.util.ArrayList;

/**
 * Created by An Van Thinh on 4/23/2017.
 */

public class ImageSongAdapter extends PagerAdapter {
    private ArrayList<Long> mArr;
    private Context mContext;
    private Activity mActivity;
    private int mPosition;
    private boolean isFirst = true;

    public ImageSongAdapter(Activity a, Context context, ArrayList<Long> arr) {
        mArr = arr;
        mActivity = a;
        mContext = context;
        mPosition = 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public int getCount() {
        return mArr.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        if (!isFirst){
//            if (mPosition > position) {
//                ScreenPlaySongFragment.startService(mActivity, MusicService.NEXT);
//            } else {
//                ScreenPlaySongFragment.startService(mActivity, MusicService.PREVIOUS);
//            }
//        }
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.image_song_fragment, container, false);
        ImageView image = (ImageView) v.findViewById(R.id.image);
        final long abumId = mArr.get(position);
        ListAdapter.setImageAvatar(mContext, image, abumId);
        container.addView(v);
        return v;
    }

    @Override
    public int getItemPosition(Object object) {

        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    private void playMusic() {

    }
}
