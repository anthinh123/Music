package com.example.anvanthinh.music.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.anvanthinh.music.Animation.BlurBuilder;
import com.example.anvanthinh.music.Controller.MainActivity;
import com.example.anvanthinh.music.Controller.OnePaneController;
import com.example.anvanthinh.music.Music;
import com.example.anvanthinh.music.MusicService;
import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.adapter.ListAdapter;
import com.example.anvanthinh.music.adapter.ListAlbumAdapter;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by An Van Thinh on 2/19/2017.
 */

public class ListSongFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, OnePaneController.QuerySearch {
    public static final int ID_SONG_LOADER = 0;
    public static final int ID_SONGAlBUM_LOADER = 10;
    private static final int UPDATE_LISTVIEW = 1;
    private static final String QUERY_SEARCH = "query_search";
    public static final String NAME_SONG = "name song";
    public static final String ARISTS = "arists";
    public static final String DURATION = "duration";
    public static final String POSITION = "position";
    private ListAdapter mAdapter;
    private RecyclerView mList;
    private RecyclerView.LayoutManager mLayout;
    private BroadcastReceiver mReceiver;
    private boolean mIsPlaying;
    private String mNameQuery; // ten bai hat hoac ca si
    private Cursor mCursor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_song_fragment, null);

        mList = (RecyclerView) v.findViewById(R.id.list_song);
        SlideInUpAnimator animator = new SlideInUpAnimator(new OvershootInterpolator(1f));
        mList.setItemAnimator(animator);
        mList.getItemAnimator().setMoveDuration(1000);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mLayout = new LinearLayoutManager(getActivity());
        mList.setLayoutManager(mLayout);
        mList.setAdapter(mAdapter);

        Bundle bun = getArguments();
        if (bun != null) {
            int state = bun.getInt(ArsistListFragment.STATE);
            String selection = null;
            if (state == ListAlbumAdapter.STATE_ALBUM) {
                mNameQuery = bun.getString(AlbumListFragment.ID_ALBUM);
                selection = MediaStore.Audio.Media.ALBUM_ID + "=?";
            } else if (state == ListAlbumAdapter.STATE_ARSIST) {
                mNameQuery = bun.getString(ArsistListFragment.ID_ARTIST);
                selection = MediaStore.Audio.Media.ARTIST_ID + "=?";
            }
            final String finalSelection = selection;
            mList.post(new Runnable() {
                @Override
                public void run() {
                    final String sortOder = MediaStore.Audio.Media.TITLE + " ASC";
                    String[] selectionArgs = new String[]{mNameQuery + ""};
                    mCursor = getContext().getContentResolver().query(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, finalSelection,
                            selectionArgs, sortOder);
                    mAdapter = new ListAdapter(getActivity(), getContext(), mCursor);
                    mList.setAdapter(mAdapter);
                    Activity activity = getActivity();
                    if (activity instanceof MainActivity) {
                        MusicFragment.OnNewSongPlayListener listener = ((MainActivity) activity).getOnNewSongPlayListener();
                        mAdapter.setOnNewSongPlayListener(listener);
                    }
                }
            });
        } else {
            getActivity().getSupportLoaderManager().initLoader(ID_SONG_LOADER, null, this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() == MusicService.UPDATE_BUTTON) {
                    mIsPlaying = intent.getBooleanExtra(MusicService.IS_PLAYING, false);
                    if (mIsPlaying) {
                        mAdapter.startEliquazor();
                    } else {
                        mAdapter.stopEliquazor();
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(MusicService.BROADCAST);
        filter.addAction(MusicService.THONG_TIN_BAI_HAT);
        filter.addAction(MusicService.UPDATE_BUTTON);
        getActivity().registerReceiver(mReceiver, filter);

    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final String sortOder = MediaStore.Audio.Media.TITLE + " ASC";
        if (id == ID_SONG_LOADER) {
            CursorLoader cursor = new CursorLoader(getActivity(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, sortOder);
            return cursor;
        } else if (id == UPDATE_LISTVIEW) {
            String text = null;
            String[] projection = new String[]{MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.ARTIST};
            if (args != null) {
                text = args.getString(QUERY_SEARCH);
            }
            CursorLoader cursor = new CursorLoader(getActivity(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, text, projection, sortOder);
            return cursor;
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter = new ListAdapter(getActivity(), getContext(), data);
        mList.setAdapter(mAdapter);
        Activity activity = getActivity();
        if (activity instanceof MainActivity) {
            MusicFragment.OnNewSongPlayListener listener = ((MainActivity) activity).getOnNewSongPlayListener();
            mAdapter.setOnNewSongPlayListener(listener);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

    @Override
    public void updateListView(String text) {
        Bundle bun = new Bundle();
        bun.putString(QUERY_SEARCH, text);
        getActivity().getSupportLoaderManager().initLoader(UPDATE_LISTVIEW, bun, this);
    }


}























