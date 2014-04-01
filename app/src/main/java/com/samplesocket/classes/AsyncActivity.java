package com.samplesocket.classes;

import android.app.Activity;

/**
 * Created by nedu on 3/30/14.
 */
public abstract class AsyncActivity extends Activity {
    public abstract void progressUpdate(String... progress);
}
