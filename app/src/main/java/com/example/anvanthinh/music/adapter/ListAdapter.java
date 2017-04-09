package com.example.anvanthinh.music.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.anvanthinh.music.ListViewCallbacks;
import com.example.anvanthinh.music.MusicService;
import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.ui.InforMusicMini;
import com.example.anvanthinh.music.ui.ListSongFragment;

import java.util.ArrayList;

/**
 * Created by An Van Thinh on 3/29/2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>   {
    private CursorAdapter mCursorAdapter;
    private Context mContext;
    private int mPosition;
    private ArrayList<ListViewCallbacks> mObserver = new ArrayList<ListViewCallbacks>();

    public ListAdapter ( Context c, Cursor cursor ){
        mContext = c;
        mCursorAdapter = new CursorAdapter(mContext, cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.item_music, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
            }
        };
    }

    @Override
    public long getItemId(int position) {
        mPosition = position;
        return super.getItemId(position);
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
        final Cursor cursor = mCursorAdapter.getCursor();
        holder.mName.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
        holder.mSinger.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
        final long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        holder.mTime.setText(sdf.format(time));
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }



    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private  TextView mName;
        private LinearLayout mInforSong;
        private TextView mTime;
        private TextView mSinger;
        private ImageButton mMore;
        private LinearLayout mItemView;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mTime = (TextView) itemView.findViewById(R.id.time);
            mSinger = (TextView) itemView.findViewById(R.id.singer);
            mMore = (ImageButton) itemView.findViewById(R.id.more);
            mInforSong = (LinearLayout) itemView.findViewById(R.id.infor_song);
            mItemView = (LinearLayout) itemView.findViewById(R.id.itemview);
            mName.setOnClickListener(this);
            mTime.setOnClickListener(this);
            mSinger.setOnClickListener(this);
            mMore.setOnClickListener(this);
            mInforSong.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.more:
                    showPopupMenu();
                    break;
                case R.id.infor_song:
                case R.id.name:
                case R.id.singer:
                    playSong(this.getAdapterPosition());
                    showMiniInfor();
                    updateMiniInfor(this.getAdapterPosition());
                    mItemView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_item_music));
                    break;
            }
        }

        private void showPopupMenu() {
            final PopupMenu popup = new PopupMenu(mContext, mMore);
            popup.getMenuInflater().inflate(R.menu.menu_more_listview, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    return true;
                }
            });
            popup.show();
        }
    }

    private void updateMiniInfor(int postion) {
        Cursor c = (Cursor) mCursorAdapter.getItem(postion);
        sendNotify(c);
    }

    private void showMiniInfor() {
    }

    private void playSong(int postion) {
        Intent i = new Intent(mContext, MusicService.class);
        i.setAction(MusicService.PLAY_SONG_FROM_LIST);
        Cursor c = (Cursor) mCursorAdapter.getItem(postion);
        i.putExtra(MusicService.PLAY_SONG_FROM_LIST, c.getString(c.getColumnIndex(MediaStore.Audio.Media.DATA)));
        mContext.startService(i);
    }

    // ham them cac doi tuong vao danh sach nhan thong bao tu listView
    public void addObserver(ListViewCallbacks list){
        mObserver.add(list);
    }

    // ham gui thong bao den cac doi tuong khi co thay doi
    public void sendNotify(Cursor c){
        for (ListViewCallbacks observer : mObserver){
            observer.update(c);
        }
    }

}
