package com.example.anvanthinh.music.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.ui.AlbumListFragment;
import com.example.anvanthinh.music.ui.ListSongFragment;
import com.example.anvanthinh.music.ui.PlayListFragment;
import com.example.anvanthinh.music.ui.SingerListFragment;

/**
 * Created by An Van Thinh on 3/29/2017.
 */

public class SongPagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;

    public SongPagerAdapter(FragmentManager fm, Context c) {
        super(fm);
        mContext = c;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = null;
        switch (position){
            case 0:
                f = new ListSongFragment();
                break;
            case 1:
                f = new AlbumListFragment();
                break;
            case 2:
                f = new SingerListFragment();
                break;
            case 3:
                f = new PlayListFragment();
                break;
        }
        return f;
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

}
