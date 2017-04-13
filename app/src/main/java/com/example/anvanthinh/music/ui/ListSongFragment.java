package com.example.anvanthinh.music.ui;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.anvanthinh.music.Animation.BlurBuilder;
import com.example.anvanthinh.music.ListViewCallbacks;
import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.adapter.ListAdapter;

/**
 * Created by An Van Thinh on 2/19/2017.
 */

public class ListSongFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final int ID_SONG_LOADER = 0;
    public static final String NAME_SONG = "name song";
    public static final String ARISTS = "arists";
    private ListAdapter mAdapter;
    private RecyclerView mList;
    private RecyclerView.LayoutManager mLayout;
    private InforMusicMini mMiniInfor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_song_fragment , null);
        mList = (RecyclerView)v.findViewById(R.id.list_song);
        mMiniInfor = (InforMusicMini)v.findViewById(R.id.mini_infor);
        mMiniInfor.setFragment(this);
        getActivity().getSupportLoaderManager().initLoader(ID_SONG_LOADER, null, this);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLayout = new LinearLayoutManager(getActivity());
        mList.setLayoutManager(mLayout);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final String sortOder = MediaStore.Audio.Media.TITLE + " ASC";
        if (id == ID_SONG_LOADER){
            CursorLoader cursor = new CursorLoader( getActivity(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, sortOder );
            return  cursor;
        }else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter = new ListAdapter( getContext(), data, this);
        mAdapter.addObserver(mMiniInfor);
        mList.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void showMiniInfor(){
        mMiniInfor.setVisibility(View.VISIBLE);
    }

    public void movePlaySong(Cursor cursor){
        ScreenPlaySongFragment f = new ScreenPlaySongFragment();
        final String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
        final String arists = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        Bundle bun = new Bundle();
        bun.putString(NAME_SONG, name);
        bun.putString(ARISTS, name);
        f.setArguments(bun);
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.pane_list_music, f).commit();
    }

}























