package org.ithot.android.view.listener;

public abstract class AVBaseCallback implements IAVCallback {

    protected abstract int calRange(int _progress);

    public void call(int _progress) {
        int $progress = calRange(_progress);
        step($progress);
    }
}
