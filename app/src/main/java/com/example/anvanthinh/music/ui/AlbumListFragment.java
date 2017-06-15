package com.example.anvanthinh.music.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.anvanthinh.music.Controller.MainActivity;
import com.example.anvanthinh.music.Controller.OnePaneController;
import com.example.anvanthinh.music.ItemClickRecycleView;
import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.adapter.ListAdapter;
import com.example.anvanthinh.music.adapter.ListAlbumAdapter;
import com.example.anvanthinh.music.adapter.SongPagerAdapter;

import static com.example.anvanthinh.music.ui.ArsistListFragment.STATE;

/**
 * Created by An Van Thinh on 3/28/2017.
 */

public class AlbumListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, ItemClickRecycleView {
    public static final int ID_ALBUM_LOADER = 1;
    public static final String ID_ALBUM = "id album";
    private RecyclerView mList;
    private ListAlbumAdapter mAdapter;
    private RecyclerView.LayoutManager mLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_song_fragment, container, false);
        mList = (RecyclerView) v.findViewById(R.id.list_song);
        getActivity().getSupportLoaderManager().initLoader(ID_ALBUM_LOADER, null, this);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLayout = new LinearLayoutManager(getActivity());
        mList.setLayoutManager(mLayout);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final String sortOder = MediaStore.Audio.Albums.ALBUM + " ASC";
        String[] projection = new String[]{
                "DISTINCT " + MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM + " _id", MediaStore.Audio.Media.ALBUM_ID
        };
        String[] columns = { android.provider.MediaStore.Audio.Albums._ID,
                android.provider.MediaStore.Audio.Albums.ALBUM };

        if (id == ID_ALBUM_LOADER) {
            final CursorLoader cursor = new CursorLoader(getActivity(), MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, columns, null, null, sortOder);
            return cursor;
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter = new ListAlbumAdapter(getActivity(), getContext(), data, ListAlbumAdapter.STATE_ALBUM, this);
        mList.setAdapter(mAdapter);
        Activity activity = getActivity();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onItemClick(int position, String id, int state) {
        ListSongFragment listSongFragment = new ListSongFragment();
        Bundle bun = new Bundle();
        bun.putString(ID_ALBUM, id);
        bun.putInt(STATE, state);
        listSongFragment.setArguments(bun);
        ((MainActivity)getActivity()).replaceFragment(listSongFragment, SongPagerAdapter.TAB_ALBUM);
    }
}























