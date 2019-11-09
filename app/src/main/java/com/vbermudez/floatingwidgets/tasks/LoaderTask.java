package com.vbermudez.floatingwidgets.tasks;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

public class LoaderTask implements Runnable {

    private boolean show;
    private ProgressBar pb;
    private Runnable post;

    public LoaderTask(ProgressBar pb, boolean show) {
        assert  pb != null;
        this.show = show;
        this.pb = pb;
    }

    public LoaderTask(ProgressBar pb, boolean show, Runnable post) {
        assert  pb != null;
        this.show = show;
        this.pb = pb;
        this.post = post;
    }

    @Override
    public void run() {
        if(pb == null) {
            return;
        }

        int visibility = -1;
        if(show) {
            visibility = View.VISIBLE;
        } else {
            visibility = View.INVISIBLE;
        }

        pb.setVisibility(visibility);

        if(post != null) {
            post.run();
        }
    }

    public void setShow(boolean show) {
        this.show = show;
    }
}
