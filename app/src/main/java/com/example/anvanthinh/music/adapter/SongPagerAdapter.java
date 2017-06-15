package com.example.anvanthinh.music.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.ui.AlbumListFragment;
import com.example.anvanthinh.music.ui.ListSongFragment;
import com.example.anvanthinh.music.ui.PlayListFragment;
import com.example.anvanthinh.music.ui.ArsistListFragment;

import java.util.ArrayList;

/**
 * Created by An Van Thinh on 3/29/2017.
 */

public class SongPagerAdapter extends FragmentStatePagerAdapter {
    public static final int TAB_LIST_SONG = 0;
    public static final int TAB_ALBUM = 1;
    public static final int TAB_ARTIST = 2;
    public static final int TAB_PLAYLIST = 3;
    private Context mContext;
    private Fragment[] mArrFragment;
    private boolean mIsChangeTab;

    public SongPagerAdapter(FragmentManager fm, Context c) {
        super(fm);
        mContext = c;
        mArrFragment = new Fragment[4];
        mArrFragment[0] = new ListSongFragment();
        mArrFragment[1] = new AlbumListFragment();
        mArrFragment[2] = new ArsistListFragment();
        mArrFragment[3] = new PlayListFragment();
    }

    @Override
    public Fragment getItem(int position) {
        return mArrFragment[position];
    }

    @Override
    public int getCount() {
        return 4;
    }
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title= mContext.getString(R.string.songs);
                break;
            case 1:
                title= mContext.getString(R.string.album);
                break;
            case 2:
                title= mContext.getString(R.string.singer);
                break;
            case 3:
                title= mContext.getString(R.string.play_list);
                break;
        }
        return title;
    }

    public void replaceFragment(Fragment f,  int tab){
        mArrFragment[tab] = f;
        mIsChangeTab = true;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        Log.d("thinhavb", "getItemPosition");
        return POSITION_NONE ;
    }

    public boolean getIsChangetab(){
        return mIsChangeTab;
    }

    public void restoreTab(){
        mIsChangeTab = false;
        mArrFragment[0] = new ListSongFragment();
        mArrFragment[1] = new AlbumListFragment();
        mArrFragment[2] = new ArsistListFragment();
        mArrFragment[3] = new PlayListFragment();
        notifyDataSetChanged();
    }

}
