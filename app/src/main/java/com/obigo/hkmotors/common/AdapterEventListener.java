package com.obigo.hkmotors.common;

import android.view.View;


/**
 * Adapter Event Listener interface for FavoriteExpendableListViewAdapter
 */
public interface AdapterEventListener {
    static public final int ON_CLICK = 0;

    abstract void onEvent(int eventType, int position, View v, String param);
}
