/*
 * Copyright (C) 2010 Tobias Falklöf
 */

package se.tfalklof.android.monsterattacken;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;

/**
 * This is the MonsterAttackenActivity activity that houses a single MonsterAttackenView.
 */
public class MonsterAttackenActivity extends Activity {

    /**
     * A handle to the View in which the game is running.
     */
    private MonsterAttackenView mMonsterAttackenView;

    /**
     * Invoked when the user selects an item from the Menu.
     *
     * @param item the Menu entry which was selected
     * @return true if the Menu item was legit (and we consumed it), false
     * otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    /**
     * Invoked when the Activity is created.
     *
     * @param savedInstanceState a Bundle containing state saved from a previous
     *                           execution, or null if this is a new execution
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(this.getClass().getName(), "onCreate() called. savedInstanceState="+savedInstanceState);

        // turn off the window's title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // tell system to use the layout defined in our XML file
        setContentView(R.layout.monsterattacken_layout);

        // let volume buttons always control the MEDIA audio stream
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        // get handle to the MonsterAttackenView from XML
        mMonsterAttackenView = findViewById(R.id.monsterattacken_appview);
        mMonsterAttackenView.setMonsterAttackenActivity(this);

        // give the MonsterAttackenView a handle to the TextView used for messages
        mMonsterAttackenView.setTextView((TextView) findViewById(R.id.monsterattacken_textview));

//        if (savedInstanceState == null) {
        // we were just launched: set up a new game
        mMonsterAttackenView.getThread().setState(MonsterAttackenThread.STATE_READY);
//        Log.w(this.getClass().getName(), "SIS is null");
//        } else {
        // we are being restored: resume a previous game
//        	mMonsterAttackenView.getThread().restoreState(savedInstanceState);
//            Log.w(this.getClass().getName(), "SIS is nonnull");
//        }
    }

    /**
     * Invoked when the Activity loses user focus.
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.w(this.getClass().getName(), "onPause() called.");
        mMonsterAttackenView.getThread().pause(); // pause game when Activity pauses
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(this.getClass().getName(), "onResume() called.");
        mMonsterAttackenView.getThread().unpause(); // pause game when Activity pauses
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w(this.getClass().getName(), "onStart() called.");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.w(this.getClass().getName(), "onRestart() called.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w(this.getClass().getName(), "onStop() called.");
    }


    /**
     * Notification that something is about to happen, to give the Activity a
     * chance to save state.
     *
     * @param outState a Bundle into which this Activity should save its state
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // just have the View's thread save its state into our Bundle
        super.onSaveInstanceState(outState);
        Log.w(this.getClass().getName(), "onSaveInstanceState() called.");
//        mMonsterAttackenView.getThread().saveState(outState);
    }
}
