package com.example.anvanthinh.music.ui;

import android.database.Cursor;
import android.os.Bundle;
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

import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.adapter.ListAdapter;

/**
 * Created by An Van Thinh on 2/19/2017.
 */

public class ListSongFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private final int ID_SONG_LOADER = 0;
    private ListAdapter mAdapter;
    private RecyclerView mList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_song_fragment , null);
        mList = (RecyclerView)v.findViewById(R.id.list_song);
        getActivity().getSupportLoaderManager().initLoader(ID_SONG_LOADER, null, this);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
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
        mAdapter = new ListAdapter(getContext(), data);
        mList.setAdapter(mAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


}























