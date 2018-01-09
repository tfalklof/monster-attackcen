/*
 * Copyright (C) 2010 Tobias Falklöf
 */

package se.tfalklof.android.monsterattacken;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

/**
 * View that draws, takes input, etc.
 */
class MonsterAttackenView extends SurfaceView implements SurfaceHolder.Callback {

    /**
     * Pointer to the text view to display "Paused.." etc.
     */
    private TextView mStatusText;

    /**
     * The thread that actually draws the animation
     */
    private MonsterAttackenThread thread = null;
    private SurfaceHolder holder = null;
    private Context context = null;
    private Activity monsterAttackenActivity = null;

    public MonsterAttackenView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // register our interest in hearing about changes to our surface
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        this.holder = holder;
        this.context = context;

        setFocusable(true); // make sure we get key events
        setFocusableInTouchMode(true); // make sure we get touch events
    }

    /**
     * Fetches the animation thread corresponding to this MonsterAttackenView.
     *
     * @return the animation thread
     */
    public MonsterAttackenThread getThread() {
        if (thread != null) {
            return thread;
        }
        thread = new MonsterAttackenThread(holder, context, new Handler() {
            @Override
            public void handleMessage(Message m) {
                mStatusText.setVisibility(m.getData().getInt("viz"));
                mStatusText.setText(m.getData().getString("text"));
            }
        });
        thread.setMonsterAttackenActivity(monsterAttackenActivity);
        return thread;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return getThread().doTouchEvent(event);
    }

    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) {
            getThread().pause();
        }
    }

    /**
     * Installs a pointer to the text view used for messages.
     */
    public void setTextView(TextView textView) {
        mStatusText = textView;
    }

    /* Callback invoked when the surface dimensions change. */
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        getThread().setSurfaceSize(width, height);
    }

    /*
     * Callback invoked when the Surface has been created and is ready to be
     * used.
     */
    public void surfaceCreated(SurfaceHolder holder) {
        // start the thread here so that we don't busy-wait in run()
        // waiting for the surface to be created
        Log.w("ma_view", "surfaceCreated(): Starting ma_thread");
        getThread().setSurfaceSize(holder.getSurfaceFrame().width(), holder.getSurfaceFrame().height());
        getThread().setRunning(true);
        getThread().setState(MonsterAttackenThread.STATE_READY);
        try {
            getThread().start();
            System.err.println("MonsterAttackenThread started");
        } catch (IllegalThreadStateException e) {
            System.err.println("MonsterAttackenThread NOT started (in surfaceCreated method)");
            System.err.println("IllegalThreadStateException: " + e.getMessage());
        }

    }

    /*
     * Callback invoked when the Surface has been destroyed and must no longer
     * be touched. WARNING: after this method returns, the Surface/Canvas must
     * never be touched again!
     */
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.w("ma_view", "surfaceDestroyed(): Stopping ma_thread");
        // we have to tell thread to shut down & wait for it to finish, or else
        // it might touch the Surface after we return and explode
        boolean retry = true;
        getThread().setRunning(false);
        while (retry) {
            try {
                getThread().join();
                thread = null; // Force a new thread to be created at next surfaceCreated
                retry = false;
            } catch (InterruptedException e) {
                Log.w("ma_view", "MonsterAttackenThread NOT stopped yet (in surfaceDestroyed method)");
            }
        }
        Log.w("ma_view","MonsterAttackenThread stopped (in surfaceDestroyed method)");
    }

    public void setMonsterAttackenActivity(Activity monsterAttackenActivity) {
        this.monsterAttackenActivity = monsterAttackenActivity;
    }
}
