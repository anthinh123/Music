package com.example.anvanthinh.music.adapter;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.anvanthinh.music.R;

/**
 * Created by An Van Thinh on 3/29/2017.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>  {
    private CursorAdapter mCursorAdapter;
    private Context mContext;

    public ListAdapter (Context c, Cursor cursor ){
        mContext = c;
        mCursorAdapter = new CursorAdapter(mContext, cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.list_adapter, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
            }
        };
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
        Cursor cursor = mCursorAdapter.getCursor();
        holder.mName.setText(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.name);
            mName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id){
                case R.id.name:

                    break;
            }
        }
    }

}
