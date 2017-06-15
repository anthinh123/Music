package com.example.anvanthinh.music.ui;

import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.anvanthinh.music.R;
import com.example.anvanthinh.music.adapter.ListAdapter;

/**
 * Created by An Van Thinh on 4/22/2017.
 */

public class ImageSongFragment extends Fragment {
    private static final String ALBUM_ID = "album id";
    private ImageView mImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.image_song_fragment, container,false);
        mImage = (ImageView) v.findViewById(R.id.image);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Bundle bun = getArguments();
//        if (bun != null){
//            final long abumId = bun.getLong(ALBUM_ID);
//            ListAdapter.setImageAvatar(getContext(), mImage, abumId);
//        }
    }

    public static void getInstance(long albumId){
        Bundle bun = new Bundle();
        bun.putLong(ALBUM_ID, albumId);
        ImageSongFragment f = new ImageSongFragment();
        f.setArguments(bun);
    }
}
