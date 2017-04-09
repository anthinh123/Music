package com.example.anvanthinh.music.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.anvanthinh.music.Animation.BlurBuilder;
import com.example.anvanthinh.music.R;

/**
 * Created by An Van Thinh on 3/28/2017.
 */

public class PlayListFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.play_list_fragment, container, false);
        View view = (LinearLayout) v.findViewById(R.id.playlist);
        Bitmap originalBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test_background);
        Bitmap blurredBitmap = BlurBuilder.blur( getActivity(), originalBitmap );
        view.setBackgroundDrawable( new BitmapDrawable( getResources(), blurredBitmap ) );
        return v;
    }
}
