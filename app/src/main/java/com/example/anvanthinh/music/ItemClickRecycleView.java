package com.example.anvanthinh.music;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

/**
 * Created by An Van Thinh on 4/6/2017.
 */

public interface ItemClickRecycleView {
    void onItemClick(int position, String id, int state);
}
