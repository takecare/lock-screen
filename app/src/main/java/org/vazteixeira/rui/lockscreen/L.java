package org.vazteixeira.rui.lockscreen;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class L {

    public static void d(Context context, String tag, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        Log.d(tag, msg);
    }

    public static void d(Context context, String msg) {
        d(context, "LockScreen", msg);
    }
}
