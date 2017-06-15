package com.example.anvanthinh.music.adapter;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anvanthinh.music.ItemClickRecycleView;
import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.ui.ListSongFragment;

import java.util.ArrayList;

/**
 * Created by An Van Thinh on 5/2/2017.
 */

public class ListAlbumAdapter extends RecyclerView.Adapter<ListAlbumAdapter.ViewHolder> {
    public static final int STATE_ALBUM = 0;
    public static final int STATE_ARSIST = 1;
    public static final int STATE_PLAYLIST = 2;
    private int mState;
    private CursorAdapter mCursorAdapter;
    private Context mContext;
    private Activity mActivity;
    private ItemClickRecycleView mItemClick;
    private String mIdAlbum;
    private ArrayList<String> mArrIdAlbum;
    private ArrayList<String> mArrIdArtist;

    public ListAlbumAdapter(Activity activity, Context c, Cursor cursor, int state, ItemClickRecycleView itemClick) {
        mState = state;
        mContext = c;
        mActivity = activity;
        mItemClick = itemClick;
        mArrIdAlbum = new ArrayList<String>();
        mArrIdArtist = new ArrayList<String>();
        mCursorAdapter = new CursorAdapter(mContext, cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.item_album, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
            }
        };

        if (state == STATE_ALBUM) {
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                mArrIdAlbum.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums._ID)));
            }
        } else if(state == STATE_ARSIST){
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                mArrIdArtist.add(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists._ID)));
            }
        }
    }

    @Override
    public ListAlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new ListAlbumAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListAlbumAdapter.ViewHolder holder, final int position) {
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
        final Cursor cursor = mCursorAdapter.getCursor();
        if (mState == STATE_ALBUM) {
            holder.mName.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));
        } else if (mState == STATE_ARSIST) {
            holder.mName.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));
        } else if (mState == STATE_PLAYLIST) {
        }
        holder.mName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mState == STATE_ALBUM ){
                    mItemClick.onItemClick(position, mArrIdAlbum.get(position), mState);
                } else if(mState == STATE_ARSIST){
                    mItemClick.onItemClick(position, mArrIdArtist.get(position), mState);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
            }

        }
    }
}
