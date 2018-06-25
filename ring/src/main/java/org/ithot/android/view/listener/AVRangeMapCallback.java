package org.ithot.android.view.listener;

import org.ithot.android.view.RingView;

public abstract class AVRangeMapCallback extends AVBaseCallback {

    private int max;
    private int min;

    protected AVRangeMapCallback(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int calRange(int _progress) {
        float unit = (max - min) / RingView.MAX_PROGRESS;
        return (int) (unit * _progress + min);
    }
}
