package com.example.anvanthinh.music;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

/**
 * Created by An Van Thinh on 4/6/2017.
 */

public interface ListViewCallbacks {
    // ham dung de update lai trang thai khi bam vao 1 bai tren listView
    void update(Cursor c);
}
