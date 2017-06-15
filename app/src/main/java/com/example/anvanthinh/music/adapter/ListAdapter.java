package com.example.anvanthinh.music.adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.anvanthinh.music.Controller.MainActivity;
import com.example.anvanthinh.music.Music;
import com.example.anvanthinh.music.MusicService;
import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.ui.MusicFragment;
import com.example.anvanthinh.music.ui.NotificationMusic;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import es.claucookie.miniequalizerlibrary.EqualizerView;

/**
 * Created by An Van Thinh on 3/29/2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private CursorAdapter mCursorAdapter;
    private Context mContext;
    private Activity mActivity;
    private MusicFragment.OnNewSongPlayListener mOnNewSongPlayListener;
    private EqualizerView mEqualizer;
    private ArrayList<Music> mArrSong;

    public ListAdapter(Activity a, Context c, Cursor cursor) {
        mContext = c;
        mActivity = a;
        mArrSong = new ArrayList<Music>();
        mCursorAdapter = new CursorAdapter(mContext, cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.item_music, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
            }
        };
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            Music m = new Music();
            m.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
            m.setName_singer(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            m.setName_song(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            m.setDuration(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            m.setAlbumId(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID)));
            mArrSong.add(m);
        }
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ListAdapter.ViewHolder holder, int position) {
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
        final Cursor cursor = mCursorAdapter.getCursor();

        holder.mName.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
        holder.mSinger.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));

        final long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
        SimpleDateFormat sdf = null;
        sdf = new SimpleDateFormat("mm:ss");
        holder.mTime.setText(sdf.format(time));

        final Long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
        setImageAvatar(mContext, holder.mAvatar, albumId);

//        mAvatar.setVisibility(View.GONE);
//        mEqualizer.setVisibility(View.VISIBLE);
//        mEqualizer.animateBars();
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mName;
        private LinearLayout mInforSong;
        private TextView mTime;
        private TextView mSinger;
        private ImageButton mMore;
        private LinearLayout mItemView;
        private ImageView mAvatar;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mTime = (TextView) itemView.findViewById(R.id.time);
            mSinger = (TextView) itemView.findViewById(R.id.singer);
            mMore = (ImageButton) itemView.findViewById(R.id.more);
            mInforSong = (LinearLayout) itemView.findViewById(R.id.infor_song);
            mItemView = (LinearLayout) itemView.findViewById(R.id.itemview);
            mAvatar = (ImageView) itemView.findViewById(R.id.image);
            mEqualizer = (EqualizerView) itemView.findViewById(R.id.equalizer_view);

            mName.setOnClickListener(this);
            mTime.setOnClickListener(this);
            mSinger.setOnClickListener(this);
            mMore.setOnClickListener(this);
            mInforSong.setOnClickListener(this);
            mAvatar.setOnClickListener(this);
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.more:
                    showPopupMenu();
                    break;
                case R.id.infor_song:
                case R.id.name:
                case R.id.singer:
                    playSong(this.getAdapterPosition());
                    mItemView.setBackground(mContext.getResources().getDrawable(R.drawable.custom_item_music));
                    if (mOnNewSongPlayListener != null) {
                        Cursor c = (Cursor) mCursorAdapter.getItem(this.getAdapterPosition());
                        mOnNewSongPlayListener.onUpdateMiniInfor(c, this.getAdapterPosition());
                    }
                    //onBindViewHolder(this, getAdapterPosition());
                    break;
            }
        }

        private void showPopupMenu() {
            final PopupMenu popup = new PopupMenu(mActivity, mMore);
            popup.getMenuInflater().inflate(R.menu.menu_more_listview, popup.getMenu());
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    return true;
                }
            });
            popup.show();
        }
    }

    private void playSong(int postion) {
        Intent i = new Intent(mContext, MusicService.class);
        i.setAction(MusicService.PLAY_SONG_FROM_LIST);
        i.putExtra(MusicService.PLAY_SONG_FROM_LIST, postion);
        i.putExtra(MusicService.DANH_SACH_NHAC, mArrSong);
        mContext.startService(i);
    }

    public void setOnNewSongPlayListener(MusicFragment.OnNewSongPlayListener onNewSongPlayListener) {
        mOnNewSongPlayListener = onNewSongPlayListener;
    }

    //animation  xoa 1 bai hat
    public void remove(int position) {
        //mCursorAdapter.(position);
        notifyItemRemoved(position);
    }

    public static void setImageAvatar(final Context context, final ImageView imageView, long albumId) {
        final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        final Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, albumId);
        Glide.with(context).load(albumArtUri).asBitmap().centerCrop().placeholder(R.drawable.album_art)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);
                    }
                });
    }

    public void stopEliquazor() {
        // mEqualizer.stopBars();
    }

    public void startEliquazor() {
        // mEqualizer.animateBars();
    }

}
