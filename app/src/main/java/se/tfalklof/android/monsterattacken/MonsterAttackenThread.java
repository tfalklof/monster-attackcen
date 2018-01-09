package se.tfalklof.android.monsterattacken;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnnecessaryLocalVariable")
public class MonsterAttackenThread extends Thread {

    private static final double DPAD_X_POS = 220;
    private static final double DPAD_Y_POS = 200;
    private static final double DPAD_WIDTH = 240;
    private static final double DPAD_HEIGHT = 200;
    private static final double HOPPKNAPP_X_POS = 200;
    private static final double HOPPKNAPP_Y_POS = 200;
    private static final double HOPPKNAPP_RADIUS = 200;
    private static final int SPELARE_Y_POS = 120;
    private static final int SPEED_FACTOR = 600;
    private static final long TOUCH_CONTROLS_GRACE_PERIOD_MILLIS = 500;
    private static final double RYMDSKEPP_SCALE_FACTOR = 0.35;


    /*
     * State-tracking constants
     */
    private static final int STATE_LOSE = 1;
    private static final int STATE_PAUSE = 2;
    static final int STATE_READY = 3;
    private static final int STATE_RUNNING = 4;
    private static final int STATE_WON_LEVEL = 5;
    private static final int STATE_WON_WORLD = 6;
    private static final int STATE_GAME_FINISHED = 7;

    /*
     * Game state variable keys Used when saving state to and loading state
     * from Bundle
     */
    private static final String KEY_MODE = "KEY_MODE";
    private static final String KEY_POS_X_SPELARE = "POS_X_SPELARE";
    private static final String KEY_POS_Y_SPELARE = "POS_Y_SPELARE";

    /**
     * The drawable to use as the background of the animation canvas
     */
    private Bitmap mBackgroundImage;
    private double mScaleFactor;

    /* Spelare */
    private RörligSpelsak spelare1;
    private RörligSpelsak spelare2;
    private RörligSpelsak nuvarandeSpelare;
    private boolean spelare_hoppar = false;

    private double hastighet_y;

    /**
     * Current height of the surface/canvas.
     *
     * @see #setSurfaceSize
     */
    private int mCanvasHeight = 1;

    /**
     * Current width of the surface/canvas.
     *
     * @see #setSurfaceSize
     */
    private int mCanvasWidth = 1;

    private int mPaintImageHeight;
    private int mPaintImageWidth;

    /**
     * Message handler used by thread to interact with TextView
     */
    private Handler mHandler;

    /**
     * Handle to the application context, used to e.g. fetch Drawables.
     */
    private Context mContext;

    /**
     * Used to figure out elapsed time between frames
     */
    private long mLastTime;

    /**
     * Paint to draw the player 1 horizontal analog dpad when pressed.
     */
    private Paint mHorizDPadPressedPaint;

    /**
     * Paint to draw the player 1 horizontal analog dpad when not pressed.
     */
    private Paint mHorizDPadUnpressedPaint;

    private Paint mHorizDPadBackgroundPaint;

    /**
     * Paint to draw the player 1 fire button.
     */
    private Paint mFireButtonPressedPaintPlayer1;

    /**
     * Paint to draw the player 1 fire button.
     */
    private Paint mFireButtonUnpressedPaintPlayer1;

    /**
     * The state of the game. One of READY, RUNNING, PAUSE, LOSE, or WIN
     */
    private int mMode;

    /**
     * Indicate whether the surface has been created & is ready to draw
     */
    private boolean mRun = false;

    /**
     * Scratch rect object.
     */
    private RectF mScratchRect;

    /**
     * Handle to the surface manager object we interact with
     */
    private final SurfaceHolder mSurfaceHolder;

    /**
     * Clip rectangle for game field i.e. the whole surface minus the score
     * panel
     */
    private Rect mClipRectGameField;
    /**
     * Clip rectangle for touch controls (normally the whole surface)
     */
    private Rect mClipRectTouchControls;

    private TouchControl[] touchControls = null;

    /**
     * Paint to draw empty black rectangles to clear screen to the sides (or top/bottom if very narrow screen...) of the background image.
     */
    private Paint mClearScreenPaint;
    private Paint mBackgroundColorPaint;

    private static final int SAMPLE_1_GET_READY = 1;
    private static final int SAMPLE_2_NORMAL_GUN_GREEN = 2;
    private static final int SAMPLE_2_NORMAL_GUN_BLUE = 3;
    private static final int SAMPLE_3_BUMP = 4;
    private static final int SAMPLE_4_CRASH_GREEN = 5;
    private static final int SAMPLE_4_CRASH_BLUE = 6;
    private static final int SAMPLE_5_OH_OOH = 7;
    private static final int SAMPLE_6_LIPS_FLAP = 8;

    private SoundManager mSoundManager;

    private Activity monsterAttackenActivty = null;

    private int mGameFieldXposLeft;
    private int mGameFieldXposRight;
    private int mGameFieldYposTop;
    private int mGameFieldYposBottom;
    private Bana nuvarandeBana;
    private final Banor _banor;
    private int nuvarandeVärldNummer;
    private int nuvarandeBanNummer;

    private int diamanter;
    private int stjärnor;

    private long touchControlsGracePeriodStart;

    private Paint runtBoundsPaint;
    private Paint runtBildenPaint;
    private boolean pauseButtonHasBeenReleased;

    MonsterAttackenThread(SurfaceHolder surfaceHolder, Context context, Handler handler) {
        // get handles to some important objects
        mSurfaceHolder = surfaceHolder;
        mContext = context;
        mHandler = handler;
        Resources res = context.getResources();
        _banor = new Banor(res);

        spelare1 = new RörligSpelsak(res.getDrawable(R.drawable.spelare1), 0.0, 0.0, 1.0, 0.0, 0.0);
        List<Rect> unscaledBounds = new ArrayList<>();
        Drawable rymdskepp = res.getDrawable(R.drawable.rymdskepp);
        unscaledBounds.add(new Rect((int) (-1 * rymdskepp.getIntrinsicWidth() / 2 * RYMDSKEPP_SCALE_FACTOR), (int) (-1 * rymdskepp.getIntrinsicHeight() / 2 * RYMDSKEPP_SCALE_FACTOR), (int) (rymdskepp.getIntrinsicWidth() / 2 * RYMDSKEPP_SCALE_FACTOR), (int) (rymdskepp.getIntrinsicHeight() / 2 * RYMDSKEPP_SCALE_FACTOR)));
        spelare2 = new RörligSpelsak(rymdskepp, 0.0, 0.0, 1.0, unscaledBounds, 0.0, 0.0);

        // load background image as a Bitmap instead of a Drawable b/c
        // we don't need to transform it and it's faster to draw this way
        mBackgroundImage = BitmapFactory.decodeResource(res, R.drawable.monsterattacken_splashscreen);

        initPaint();

        mScratchRect = new RectF(0, 0, 0, 0);

        initSound(context);

        nuvarandeVärldNummer = 2;
        nuvarandeBanNummer = 1;

        diamanter = 0;
        stjärnor = 3;
    }

    private void initPaint() {
        mHorizDPadPressedPaint = new Paint();
        mHorizDPadPressedPaint.setAntiAlias(true);
        mHorizDPadPressedPaint.setARGB(127, 222, 180, 222);

        mHorizDPadUnpressedPaint = new Paint();
        mHorizDPadUnpressedPaint.setAntiAlias(true);
        mHorizDPadUnpressedPaint.setARGB(127, 160, 100, 160);

        mHorizDPadBackgroundPaint = new Paint();
        mHorizDPadBackgroundPaint.setAntiAlias(true);
        mHorizDPadBackgroundPaint.setARGB(127, 100, 60, 100);

        mFireButtonPressedPaintPlayer1 = new Paint();
        mFireButtonPressedPaintPlayer1.setAntiAlias(true);
        mFireButtonPressedPaintPlayer1.setARGB(127, 200, 255, 200);

        mFireButtonUnpressedPaintPlayer1 = new Paint();
        mFireButtonUnpressedPaintPlayer1.setAntiAlias(true);
        mFireButtonUnpressedPaintPlayer1.setARGB(127, 127, 255, 127);

        mClearScreenPaint = new Paint();
        mClearScreenPaint.setAntiAlias(false);
        mClearScreenPaint.setARGB(255, 60, 0, 60);

        mBackgroundColorPaint = new Paint();
        mBackgroundColorPaint.setAntiAlias(false);
        mBackgroundColorPaint.setARGB(255, 60, 60, 60);

        runtBildenPaint = new Paint();
        runtBildenPaint.setStyle(Paint.Style.STROKE);
        runtBildenPaint.setARGB(255, 255, 255, 255);

        runtBoundsPaint = new Paint();
        runtBoundsPaint.setStyle(Paint.Style.STROKE);
        runtBoundsPaint.setARGB(255, 255, 255, 64);
    }

    private void initSound(Context context) {
        // initialize sound system
        mSoundManager = new SoundManager();
        mSoundManager.initSounds(context);

        // initialize sound effects
        mSoundManager.addSound(SAMPLE_1_GET_READY, R.raw.sample_1_get_ready);
        mSoundManager.addSound(SAMPLE_2_NORMAL_GUN_GREEN, R.raw.sample_2_normal_gun);
        mSoundManager.addSound(SAMPLE_2_NORMAL_GUN_BLUE, R.raw.sample_2_normal_gun);
        mSoundManager.addSound(SAMPLE_3_BUMP, R.raw.sample_3_bump);
        mSoundManager.addSound(SAMPLE_4_CRASH_GREEN, R.raw.sample_4_crash);
        mSoundManager.addSound(SAMPLE_4_CRASH_BLUE, R.raw.sample_4_crash);
        mSoundManager.addSound(SAMPLE_5_OH_OOH, R.raw.sample_5_oh_ooh);
        mSoundManager.addSound(SAMPLE_6_LIPS_FLAP, R.raw.sample_6_lips_flap);
    }

    /**
     * Starts the game
     */
    private void doStart() {
        synchronized (mSurfaceHolder) {

            if (nuvarandeVärldNummer == 1) {
                hastighet_y = 2.0;
                nuvarandeSpelare = spelare1;
            } else if (nuvarandeVärldNummer == 2) {
                nuvarandeSpelare = spelare2;
                hastighet_y = 4.0;
            }
            resetSpelare();

            Log.i("ma_thread", "doStart: nuvarandeSpelare=" + nuvarandeSpelare + ", hastighet_y=" + hastighet_y);

            nuvarandeBana = _banor.hämtaVärld(nuvarandeVärldNummer).hämtaBana(nuvarandeBanNummer);
            Log.i("ma_thread", "doStart: nuvarandeBana=" + nuvarandeBana);

            // Make sure 100 msecs pass after game start before
            // updatePhysics() go into action
            mLastTime = System.currentTimeMillis() + 100;

            initTouchControls();
            setState(STATE_RUNNING);
        }
    }

    private void initTouchControls() {
        Log.w("ma_thread", "initTouchControls() called.");
        // Create horizontal analog dpad for player 1
        HorizAnalogDPad dPad = new HorizAnalogDPad();
        dPad.setName("dpad");
        dPad.setWidth((int) (DPAD_WIDTH * mScaleFactor));
        dPad.setHeight((int) (DPAD_HEIGHT * mScaleFactor));
        dPad.setX((int) ((DPAD_X_POS * mScaleFactor)));
        dPad.setY((int) (mCanvasHeight - (DPAD_Y_POS * mScaleFactor)));

        // Create fire button for player 1
        CircularButton hoppknapp = new CircularButton();
        hoppknapp.setName("hoppknapp");
        hoppknapp.setWidth((int) (HOPPKNAPP_RADIUS * mScaleFactor));
        hoppknapp.setHeight((int) (HOPPKNAPP_RADIUS * mScaleFactor));
        hoppknapp.setX((int) (mCanvasWidth - (HOPPKNAPP_X_POS * mScaleFactor)));
        hoppknapp.setY((int) (mCanvasHeight - (HOPPKNAPP_Y_POS * mScaleFactor)));

        touchControls = new TouchControl[2];
        touchControls[0] = dPad;
        touchControls[1] = hoppknapp;

        for (int i = 0; i < touchControls.length; i++) {
            android.util.Log.i("ma_thread", "touchControls[" + i + "]: " + touchControls[i]);
        }

        touchControlsGracePeriodStart = System.currentTimeMillis();
    }

    /**
     * Pauses the physics update & animation.
     */
    void pause() {
        synchronized (mSurfaceHolder) {
            if (mMode == STATE_RUNNING) {
                setState(STATE_PAUSE);
            }
        }
    }

    /**
     * Restores game state from the indicated Bundle. Typically called when
     * the Activity is being restored after having been previously
     * destroyed.
     *
     * @param savedState Bundle containing the game state
     */
    synchronized void restoreState(Bundle savedState) {
        synchronized (mSurfaceHolder) {
            int mode = savedState.getInt(KEY_MODE);
            if(mode == STATE_RUNNING) {
                mode = STATE_PAUSE;
            }
            setState(mode);
//            nuvarandeSpelare.setPosX(savedState.getDouble(KEY_POS_X_SPELARE));
//            nuvarandeSpelare.setPosY(savedState.getDouble(KEY_POS_Y_SPELARE));

            nuvarandeSpelare.setHastX(0.0);
            nuvarandeSpelare.setHastY(0.0);
        }
    }

    @Override
    public void run() {
        while (mRun) {
            Canvas c = null;
            try {
                c = mSurfaceHolder.lockCanvas(null);
                synchronized (mSurfaceHolder) {
                    if (mMode == STATE_RUNNING) {
                        updatePhysics();
                    }
                    doDraw(c);
                }
            } finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                    mSurfaceHolder.unlockCanvasAndPost(c);
                }
            }
        }
    }

    /**
     * Dump game state to the provided Bundle. Typically called when the
     * Activity is being suspended.
     */
    void saveState(Bundle map) {
        synchronized (mSurfaceHolder) {
            if (map != null) {
                map.putInt(KEY_MODE, mMode);
//                map.putDouble(KEY_POS_X_SPELARE, nuvarandeSpelare.getPosX());
//                map.putDouble(KEY_POS_Y_SPELARE, nuvarandeSpelare.getPosY());
            }
        }
    }

    /**
     * Used to signal the thread whether it should be running or not.
     * Passing true allows the thread to run; passing false will shut it
     * down if it's already running. Calling start() after this was most
     * recently called with false will result in an immediate shutdown.
     *
     * @param b true to run, false to shut down
     */
    void setRunning(boolean b) {
        mRun = b;
    }

    /**
     * Sets the game mode. That is, whether we are running, paused, in the
     * failure state, in the victory state, etc.
     *
     * @param mode one of the STATE_* constants
     * @see #setState(int, CharSequence)
     */
    void setState(int mode) {
        synchronized (mSurfaceHolder) {
            setState(mode, null);
        }
    }

    /**
     * Sets the game mode. That is, whether we are running, paused, in the
     * failure state, in the victory state, etc.
     *
     * @param mode    one of the STATE_* constants
     * @param message string to add to screen or null
     */
    private void setState(int mode, CharSequence message) {
        /*
         * This method optionally can cause a text message to be displayed
		 * to the user when the mode changes. Since the View that actually
		 * renders that text is part of the main View hierarchy and not
		 * owned by this thread, we can't touch the state of that View.
		 * Instead we use a Message + Handler to relay commands to the main
		 * thread, which updates the user-text View.
		 */
        synchronized (mSurfaceHolder) {
            Log.i("mathread", "setState(): Changing state from "+mMode+" to: "+mode);
            mMode = mode;
            Bundle b = new Bundle();
            Message msg = mHandler.obtainMessage();
            if (mMode == STATE_RUNNING) {
                b.putString("text", "");
                b.putInt("viz", View.INVISIBLE);
            } else {
//                Resources res = mContext.getResources();
                CharSequence str = "";
                if (mMode == STATE_READY) {
                    str = "Värld " + nuvarandeVärldNummer + " Bana " + nuvarandeBanNummer + ": Gör dig redo!";
                    // play sample
                    try {
                        mSoundManager.playSound(SAMPLE_1_GET_READY);
                    } catch (IllegalStateException e) {
                        // Sample could not be played, but we don't care so do nothing
                    }
                } else if (mMode == STATE_PAUSE) {
                    str = "Spelet pausat!";
                } else if (mMode == STATE_LOSE) {
                    str = "Du förlorade. Försök igen!";
                } else if (mMode == STATE_WON_LEVEL) {
                    str = "Du klarade banan!";
                } else if (mMode == STATE_WON_WORLD) {
                    str = "Du klarade världen!";
                } else if (mMode == STATE_GAME_FINISHED) {
                    str = "Du klarade hela spelet, grattis!";
                }

                if (message != null) {
                    str = message + "\n" + str;
                }

                b.putString("text", str.toString());
                b.putInt("viz", View.VISIBLE);

                if (mMode != STATE_PAUSE) {
                    touchControlsGracePeriodStart = System.currentTimeMillis();
                }
            }
            msg.setData(b);
            mHandler.sendMessage(msg);
        }
    }

    /* Callback invoked when the surface dimensions change. */
    void setSurfaceSize(int width, int height) {
        // synchronized to make sure these all change atomically
        synchronized (mSurfaceHolder) {
            mCanvasWidth = width;
            mCanvasHeight = height;

            // don't forget to resize the background image
            double mBackgroundImageUnscaledWidth = 1600.0;
            double mBackgroundImageUnscaledHeight = 900.0;
            double mBackgroundImageRatio = mBackgroundImageUnscaledWidth / mBackgroundImageUnscaledHeight;
            double mCanvasRatio = (double) width / (double) height;
            if (mBackgroundImageRatio >= mCanvasRatio) {
                mPaintImageWidth = width;
                mPaintImageHeight = (int) ((double) width / mBackgroundImageRatio);
            } else {
                mPaintImageWidth = (int) (mBackgroundImageRatio * (double) height);
                mPaintImageHeight = height;
            }

            mBackgroundImage = Bitmap.createScaledBitmap(mBackgroundImage,
                    mPaintImageWidth, mPaintImageHeight, true);

            mScaleFactor = (double) mPaintImageWidth / mBackgroundImageUnscaledWidth;
            Utils.setScaleFactor(mScaleFactor);
            _banor.setScaleFactor(mScaleFactor);
            _banor.skapaVärldar();
            spelare1.scaleBounds(mScaleFactor);
            spelare2.scaleBounds(mScaleFactor);
            Log.w("mathread", "mScaleFactor=" + mScaleFactor);

            int bgImageXpos = (mCanvasWidth - mPaintImageWidth) / 2;
            int bgImageYpos = (mCanvasHeight - mPaintImageHeight) / 2;
            mGameFieldXposLeft = bgImageXpos;
            mGameFieldXposRight = mCanvasWidth - bgImageXpos;
            mGameFieldYposTop = bgImageYpos;
            mGameFieldYposBottom = mCanvasHeight - bgImageYpos;
            mClipRectGameField = new Rect(mGameFieldXposLeft, mGameFieldYposTop, mGameFieldXposRight, mGameFieldYposBottom);
            mClipRectTouchControls = new Rect(0, 0, mCanvasWidth, mCanvasHeight);
        }
        Log.i("ma_thread", "setSurfaceSize(): mCanvasWidth=" + mCanvasWidth + ", mCanvasHeight=" + mCanvasHeight + ", mGameFieldXposLeft=" + mGameFieldXposLeft + ", mGameFieldXposRight=" + mGameFieldXposRight + ", mGameFieldYposTop=" + mGameFieldYposTop + ", mGameFieldYposBottom=" + mGameFieldYposBottom);
    }

    /**
     * Resumes from a pause.
     */
    void unpause() {
        // Move the real time clock up to now
        synchronized (mSurfaceHolder) {
            mLastTime = System.currentTimeMillis() + 100;
            if (mMode == STATE_PAUSE) {
                initTouchControls();
                setState(STATE_RUNNING);
            }
        }
    }

    /**
     * Draws the ship, fuel/speed bars, and background to the provided
     * Canvas.
     */

    private void doDraw(Canvas canvas) {

        if (canvas == null) {
            return;
        }

//        Log.i("ma_thread", "doDraw(): mMode="+mMode);

//        if(mMode == STATE_WON_WORLD || mMode == STATE_WON_LEVEL || mMode == STATE_LOSE || mMode == STATE_PAUSE || mMode == STATE_READY) {
//            return;
//        }

        int bgImageXpos = (mCanvasWidth - mPaintImageWidth) / 2;
        int bgImageYpos = (mCanvasHeight - mPaintImageHeight) / 2;

        // Clear play field
        mScratchRect.set(bgImageXpos, bgImageYpos, mCanvasWidth - bgImageXpos, mCanvasHeight - bgImageYpos);
        canvas.drawRect(mScratchRect, mBackgroundColorPaint);

        int clearSidesWidth = bgImageXpos;
        int clearTopAndBottomHeight = bgImageYpos;
        if (clearSidesWidth > 0) {
            // Clear screen to the sides of the background image (play field)
            mScratchRect.set(0, 0, clearSidesWidth, mCanvasHeight);
            canvas.drawRect(mScratchRect, mClearScreenPaint);
            mScratchRect.set(mCanvasWidth - clearSidesWidth, 0, mCanvasWidth, mCanvasHeight);
            canvas.drawRect(mScratchRect, mClearScreenPaint);
        }
        if (clearTopAndBottomHeight > 0) {
            // Clear screen above and below the background image (play field)
            mScratchRect.set(0, 0, mCanvasWidth, clearTopAndBottomHeight);
            canvas.drawRect(mScratchRect, mClearScreenPaint);
            mScratchRect.set(0, mCanvasHeight - clearTopAndBottomHeight, mCanvasWidth, mCanvasHeight);
            canvas.drawRect(mScratchRect, mClearScreenPaint);
        }

        if (mMode == STATE_READY) {
            canvas.drawBitmap(mBackgroundImage, (mCanvasWidth - mPaintImageWidth) / 2, (mCanvasHeight - mPaintImageHeight) / 2, null);
            return;
        }

        // Rita nuvarande spelare och bana
        canvas.clipRect(mClipRectGameField);
        ritaSpelare(canvas);
        ritaHinder(canvas);
        ritaDiamanter(canvas);
        ritaStjärnor(canvas);
        ritaMonster(canvas);
        ritaMålgång(canvas);

        // Rita knappar att styra spelaren med
        canvas.clipRect(mClipRectTouchControls, Region.Op.REPLACE);
        ritaDPad(canvas);
        ritaHoppaKnapp(canvas);
    }

    private void ritaSpelare(Canvas canvas) {
        ritaCentreratDips(nuvarandeSpelare, canvas);
    }

    private void ritaHinder(Canvas canvas) {
        for (Spelsak hinder : nuvarandeBana.allaHinder()) {
            ritaCentreratDips(hinder, canvas);
        }
    }

    private void ritaDiamanter(Canvas canvas) {
        for (Spelsak diamant : nuvarandeBana.allaDiamanter()) {
            ritaCentreratDips(diamant, canvas);
        }
    }

    private void ritaStjärnor(Canvas canvas) {
        for (Spelsak stjärna : nuvarandeBana.allaStjärnor()) {
            ritaCentreratDips(stjärna, canvas);
        }
    }

    private void ritaMonster(Canvas canvas) {
        for (RörligSpelsak monster : nuvarandeBana.allaMonster()) {
            ritaCentreratDips(monster, canvas);
        }
    }

    private void ritaMålgång(Canvas canvas) {
        Spelsak målgång = nuvarandeBana.målgång();
        ritaCentreratDips(målgång, canvas);
    }

    private void ritaDPad(Canvas canvas) {
        if (touchControls != null && touchControls[0] != null) {
            HorizAnalogDPad horizDPad = (HorizAnalogDPad) touchControls[0];
            // Draw background
            mScratchRect.set(horizDPad.getX() - horizDPad.getWidth() / 2, horizDPad.getY() - horizDPad.getHeight() / 2, horizDPad.getX() + horizDPad.getWidth() / 2, horizDPad.getY() + horizDPad.getHeight() / 2);
            if (horizDPad.isPressed()) {
                canvas.drawRect(mScratchRect, mHorizDPadPressedPaint);
            } else {
                canvas.drawRect(mScratchRect, mHorizDPadUnpressedPaint);
            }
            // Draw circle
            mScratchRect.set(horizDPad.getX() + (float) (horizDPad.getWidth() * horizDPad.getPressedPosition() / 2 - horizDPad.getHeight() / 2), horizDPad.getY() - horizDPad.getHeight() / 2, horizDPad.getX() + (float) (horizDPad.getWidth() * horizDPad.getPressedPosition() / 2 + horizDPad.getHeight() / 2), horizDPad.getY() + horizDPad.getHeight() / 2);
            canvas.drawArc(mScratchRect, 0, 360, true, mHorizDPadBackgroundPaint);
        }
    }

    private void ritaHoppaKnapp(Canvas canvas) {
        if (touchControls != null && touchControls[1] != null) {
            CircularButton jumpButton = (CircularButton) touchControls[1];
            // Draw background
            mScratchRect.set(jumpButton.getX() - jumpButton.getWidth() / 2, jumpButton.getY() - jumpButton.getHeight() / 2, jumpButton.getX() + jumpButton.getWidth() / 2, jumpButton.getY() + jumpButton.getHeight() / 2);
//            android.util.Log.i("ma_thread", "doDraw(): jumpButton=" + jumpButton + ", mScratchRect=" + mScratchRect);
            if (jumpButton.isPressed()) {
                canvas.drawArc(mScratchRect, 0, 360, true, mFireButtonPressedPaintPlayer1);
            } else {
                canvas.drawArc(mScratchRect, 0, 360, true, mFireButtonUnpressedPaintPlayer1);
            }
        }
    }

    private void ritaCentreratDips(Spelsak spelsak, Canvas canvas) {
        ritaCentrerat(spelsak.getBild(), (int) (spelsak.getPosX() * mScaleFactor), (int) (spelsak.getPosY() * mScaleFactor), spelsak.getScaledBounds(), canvas);
    }

    private void ritaCentrerat(Drawable drawable, int x, int y, List<Rect> bounds, Canvas canvas) {
        int left = x - Utils.getWidth(bounds) / 2;
        int top = mGameFieldYposBottom - (y + Utils.getHeight(bounds) / 2);
        int right = left + Utils.getWidth(bounds);
        int bottom = top + Utils.getHeight(bounds);
        drawable.setBounds(left, top, right, bottom);
        drawable.draw(canvas);

        // Vit rektangel runt bilden
//        mScratchRect.set(left, top, right, bottom);
//        canvas.drawRect(mScratchRect, runtBildenPaint);

        // Gul rektangel runt alla bound rektanglar
        for (Rect boundRect : bounds) {
            mScratchRect.set(boundRect.left, mGameFieldYposBottom - boundRect.bottom, boundRect.right, mGameFieldYposBottom - boundRect.top);
            mScratchRect.offset(x, -1 * y);
            canvas.drawRect(mScratchRect, runtBoundsPaint);
        }
    }

    private void updatePhysics() {
        long now = System.currentTimeMillis();

        // Do nothing if mLastTime is in the future.
        // This allows the game-start to delay the start of the physics
        // by 100ms or whatever.
        if (mLastTime >= now) {
            return;
        }

        double elapsed = (now - mLastTime) / 1000.0;

        // Kolla om spelaren gått i mål
        Spelsak målgång = nuvarandeBana.målgång();
        if (kolliderar(nuvarandeSpelare, målgång, mScaleFactor)) {
            synchronized (mSurfaceHolder) {
                levelWon();
            }
        }
        målgång.minusPosY(hastighet_y);

        // Kolla om nuvarandeSpelare kolliderat med hinder
        for (Spelsak hinder : nuvarandeBana.allaHinder()) {
            if (kolliderar(nuvarandeSpelare, hinder, mScaleFactor)) {
                synchronized (mSurfaceHolder) {
                    levelLost();
                }
                return;
            }
        }

        // Kolla om nuvarandeSpelare kolliderat med monster
        for (Spelsak monster : nuvarandeBana.allaMonster()) {
            if (kolliderar(nuvarandeSpelare, monster, mScaleFactor)) {
                synchronized (mSurfaceHolder) {
                    levelLost();
                }
                return;
            }
        }

        // Kolla om nuvarandeSpelare plockar diamant
        for (Spelsak diamant : nuvarandeBana.allaDiamanter()) {
            if (kolliderar(nuvarandeSpelare, diamant, mScaleFactor)) {
                synchronized (mSurfaceHolder) {
                    plockaDiamant(diamant);
                }
                return;
            }
        }

        // Kolla om nuvarandeSpelare plockar stjärna
        for (Spelsak stjärna : nuvarandeBana.allaStjärnor()) {
            if (kolliderar(nuvarandeSpelare, stjärna, mScaleFactor)) {
                synchronized (mSurfaceHolder) {
                    plockaStjärna(stjärna);
                }
                return;
            }
        }

        nuvarandeSpelare.plusPosX(nuvarandeSpelare.getHastX() * elapsed * SPEED_FACTOR);
        //Log.i("ma_thread", "updatePhysics: hastighet_x_spelare=" + hastighet_x_spelare + ", elapsed=" + elapsed + ", pos_x_spelare=" + pos_x_spelare);

        // Stoppa spelaren från att åka ut från skärmen till vänster
        if (nuvarandeSpelare.getPosX() < Utils.getWidth(nuvarandeSpelare.getBild()) / 2) {
            nuvarandeSpelare.setPosX(Utils.getWidth(nuvarandeSpelare.getBild()) / 2);
        }

        // Stoppa spelaren från att åka ut från skärmen till höger
        if (nuvarandeSpelare.getPosX() > 1600 - Utils.getWidth(nuvarandeSpelare.getBild()) / 2) {
            nuvarandeSpelare.setPosX(1600 - Utils.getWidth(nuvarandeSpelare.getBild()) / 2);
        }

        for (Spelsak h : nuvarandeBana.allaHinder()) {
            h.minusPosY(hastighet_y);
        }

        for (Spelsak h : nuvarandeBana.allaStjärnor()) {
            h.minusPosY(hastighet_y);
        }

        for (Spelsak h : nuvarandeBana.allaDiamanter()) {
            h.minusPosY(hastighet_y);
        }

        for (RörligSpelsak monster : nuvarandeBana.allaMonster()) {
            monster.minusPosY(hastighet_y);
            monster.plusPosX((monster.getHastX()));
            if (monster.getPosX() > 1500) {
                monster.setHastX(monster.getHastX() * -1);
                monster.plusPosX((monster.getHastX()));
            }
            if (monster.getPosX() < 100) {
                monster.setHastX(monster.getHastX() * -1);
                monster.plusPosX((monster.getHastX()));
            }
        }

        mLastTime = now;
    }

    private void plockaDiamant(Spelsak diamant) {
        nuvarandeBana.plockaDiamant(diamant);
        diamanter++;
    }

    private void plockaStjärna(Spelsak stjärna) {
        nuvarandeBana.plockaStjärna(stjärna);
        stjärnor++;
    }

    private void resetTouchControls() {
        for (TouchControl tc : touchControls) {
            tc.reset();
        }
    }

    private void resetNuvarandeBana() {
        _banor.skapaBana(nuvarandeVärldNummer, nuvarandeBanNummer);
    }

    private void resetSpelare() {
        nuvarandeSpelare.setPosX(800.0);
        nuvarandeSpelare.setPosY(SPELARE_Y_POS * mScaleFactor);
        nuvarandeSpelare.setHastX(0.0);
        nuvarandeSpelare.setHastY(0.0);
    }

    private void levelWon() {
        if (nuvarandeBanNummer < _banor.hämtaVärld(nuvarandeVärldNummer).antalBanor()) {
            nuvarandeBanNummer++;
            setState(STATE_WON_LEVEL);
        } else {
            nuvarandeBanNummer = 1;
            if (nuvarandeVärldNummer < _banor.antalVärldar()) {
                nuvarandeVärldNummer++;
                setState(STATE_WON_WORLD);
            } else {
                setState(STATE_GAME_FINISHED);
            }
        }
    }

    private void levelLost() {
        setState(STATE_LOSE);
        resetTouchControls();
        resetSpelare();
        resetNuvarandeBana();
    }

    boolean doTouchEvent(MotionEvent event) {
        synchronized (mSurfaceHolder) {
            boolean okStart = false;

            //android.util.Log.i("ma_thread", "mMode=" + mMode);
            if (mMode != STATE_RUNNING) {

                StringBuilder sb = new StringBuilder("ME: STATE!=RUNNING: ");

                int pointerCount = event.getPointerCount();
                sb.append("#p=").append(pointerCount).append(" ");
                for (int i = 0; i < pointerCount; i++) {
                    int pIdentifier = event.getPointerId(i);
                    sb.append("[pId=").append(pIdentifier).append(" x=").append((int) event.getX(i)).append(" y=").append((int) event.getY(i)).append("] ");
                }

                // Get identifier of pointer for which the action occured
                int aIdx = event.getActionIndex();
                int pIdentifier = event.getPointerId(aIdx); // NB!!! This only works for UP and DOWN, NOT for MOVE!!!
                sb.append(" pId=").append(pIdentifier);
                //str += " a="+event.getAction();
                sb.append(" x=").append((int) event.getX(aIdx)).append(" y=").append((int) event.getY(aIdx));
                int actionMasked = event.getActionMasked();
                sb.append(" action=").append(printMotionEventActionCode(actionMasked));
                //android.util.Log.i("ma_thread", sb.toString());

                // Get identifier of pointer for which the action occured
                if (getActionType(event.getActionMasked()) == ME_ACTION_UP) {
                    okStart = true;
                    //android.util.Log.i("ma_thread", "okStart=true");
                }
            }

            long now = System.currentTimeMillis();
            if (mMode != STATE_RUNNING && now - TOUCH_CONTROLS_GRACE_PERIOD_MILLIS < touchControlsGracePeriodStart) {
                return true;
            }

            if (okStart && (mMode == STATE_WON_LEVEL || mMode == STATE_WON_WORLD || mMode == STATE_LOSE)) {
                // won level or won world or lose level -> ready-to-start
                setState(STATE_READY);
            } else if (okStart && (mMode == STATE_READY)) {
                // ready-to-start -> start
                doStart();
            } else if (mMode == STATE_RUNNING || mMode == STATE_PAUSE) {
                StringBuilder sb = new StringBuilder("ME: ");

                int pointerCount = event.getPointerCount();
                sb.append("#p=").append(pointerCount).append(" ");
                for (int i = 0; i < pointerCount; i++) {
                    int pIdentifier = event.getPointerId(i);
                    sb.append("[pId=").append(pIdentifier).append(" x=").append((int) event.getX(i)).append(" y=").append((int) event.getY(i)).append("] ");
                }

                // Get identifier of pointer for which the action occured
                int aIdx = event.getActionIndex();
                int pIdentifier = event.getPointerId(aIdx); // NB!!! This only works for UP and DOWN, NOT for MOVE!!!
                sb.append(" pId=").append(pIdentifier);
                //str += " a="+event.getAction();
                sb.append(" x=").append((int) event.getX(aIdx)).append(" y=").append((int) event.getY(aIdx));
                int actionMasked = event.getActionMasked();
                sb.append(" action=").append(printMotionEventActionCode(actionMasked));
                //android.util.Log.i("adfview", sb.toString());

                int actionType = getActionType(actionMasked);
                if (actionType == ME_ACTION_MOVE) {
                    for (int i = 0; i < touchControls.length; i++) {
                        for (int j = 0; j < event.getPointerCount(); j++) {
                            touchControls[i].actionMoveTo(event.getPointerId(j), (int) event.getX(j), (int) event.getY(j));
                        }
                        //android.util.Log.i("adfview", "ME_ACTION_MOVE: dpad[" + i + "]: " + touchControls[i]);
                    }

                } else if (actionType == ME_ACTION_DOWN) {
                    for (int i = 0; i < touchControls.length; i++) {
                        touchControls[i].actionDownAt(pIdentifier, (int) event.getX(aIdx), (int) event.getY(aIdx));
                        //android.util.Log.i("adfview", "ME_ACTION_DOWN: dpad[" + i + "]: " + touchControls[i]);
                    }
                } else if (actionType == ME_ACTION_UP) {
                    for (int i = 0; i < touchControls.length; i++) {
                        touchControls[i].actionUpAt(pIdentifier, (int) event.getX(aIdx), (int) event.getY(aIdx));
                        //android.util.Log.i("adfview", "ME_ACTION_UP: dpad[" + i + "]: " + touchControls[i]);
                    }
                }

                if (mMode == STATE_RUNNING) {
                    // Get pressed position of player 1 horizontal analog dpad
                    HorizAnalogDPad dPad = (HorizAnalogDPad) touchControls[0];
                    if (dPad.isPressed()) {
                        nuvarandeSpelare.setHastX(dPad.getPressedPosition());
                    } else {
                        nuvarandeSpelare.setHastX(0.0);
                    }
                }

                CircularButton pauseKnapp = (CircularButton) touchControls[1];
                if (pauseKnapp.isPressed() && pauseButtonHasBeenReleased) {
                    Log.w("mathread", "!!!! pauseKnapp.isPressed() == TRUE !!!!");
                    if (mMode == STATE_RUNNING) {
                        Log.w("pause", "doTouchEvent(): PAUSING");
                        setState(STATE_PAUSE);
                        pauseButtonHasBeenReleased = false;
                    } else if (mMode == STATE_PAUSE) {
                        Log.w("pause", "doTouchEvent(): UNPAUSING");
                        mLastTime = System.currentTimeMillis();
                        setState(STATE_RUNNING);
                        pauseButtonHasBeenReleased = false;
                    }
                }

                if (!pauseKnapp.isPressed() && !pauseButtonHasBeenReleased) {
                    Log.w("pause", "doTouchEvent(): RELEASING PAUSE BUTTON");
                    pauseButtonHasBeenReleased = true;
                }
            }
        }
        return true;
    }

    private final int ME_ACTION_MOVE = 1;
    private final int ME_ACTION_DOWN = 2;
    private final int ME_ACTION_UP = 3;
    @SuppressWarnings("FieldCanBeLocal")
    private final int ME_ACTION_UNKNOWN = -1;

    private int getActionType(int motionEventActionCode) {
        switch (motionEventActionCode) {
            case MotionEvent.ACTION_MOVE:
                return ME_ACTION_MOVE;
            case MotionEvent.ACTION_DOWN:
                return ME_ACTION_DOWN;
            case MotionEvent.ACTION_UP:
                return ME_ACTION_UP;
            case MotionEvent.ACTION_POINTER_1_DOWN:
                return ME_ACTION_DOWN;
            case MotionEvent.ACTION_POINTER_1_UP:
                return ME_ACTION_UP;
            case MotionEvent.ACTION_POINTER_2_DOWN:
                return ME_ACTION_DOWN;
            case MotionEvent.ACTION_POINTER_2_UP:
                return ME_ACTION_UP;
            case MotionEvent.ACTION_POINTER_3_DOWN:
                return ME_ACTION_DOWN;
            case MotionEvent.ACTION_POINTER_3_UP:
                return ME_ACTION_UP;
            default:
                return ME_ACTION_UNKNOWN;
        }
    }

    private String printMotionEventActionCode(int motionEventActionCode) {
        switch (motionEventActionCode) {
            /*
            case MotionEvent.ACTION_MOVE: return "ACTION_MOVE";
			case MotionEvent.ACTION_DOWN: return "ACTION_DOWN";
			case MotionEvent.ACTION_UP: return "ACTION_UP";
			case MotionEvent.ACTION_POINTER_1_DOWN: return "ACTION_POINTER_1_DOWN";
			case MotionEvent.ACTION_POINTER_1_UP: return "ACTION_POINTER_1_UP";
			case MotionEvent.ACTION_POINTER_2_DOWN: return "ACTION_POINTER_2_DOWN";
			case MotionEvent.ACTION_POINTER_2_UP: return "ACTION_POINTER_2_UP";
			case MotionEvent.ACTION_POINTER_3_DOWN: return "ACTION_POINTER_3_DOWN";
			case MotionEvent.ACTION_POINTER_3_UP: return "ACTION_POINTER_3_UP";
			default: return "UNKNOWN_ACTION";
			*/
            case MotionEvent.ACTION_MOVE:
                return "MOVE";
            case MotionEvent.ACTION_DOWN:
                return "DOWN";
            case MotionEvent.ACTION_UP:
                return "UP";
            case MotionEvent.ACTION_POINTER_1_DOWN:
                return "DOWN";
            case MotionEvent.ACTION_POINTER_1_UP:
                return "UP";
            case MotionEvent.ACTION_POINTER_2_DOWN:
                return "DOWN";
            case MotionEvent.ACTION_POINTER_2_UP:
                return "UP";
            case MotionEvent.ACTION_POINTER_3_DOWN:
                return "DOWN";
            case MotionEvent.ACTION_POINTER_3_UP:
                return "UP";
            default:
                return "UNKNOWN_ACTION";
        }
    }

    void setMonsterAttackenActivity(Activity alienDogfightActivity) {
        this.monsterAttackenActivty = alienDogfightActivity;
    }

    private static Rect scratchRectSpelsak1 = new Rect();
    private static Rect scratchRectSpelsak2 = new Rect();

    public static boolean kolliderar(Spelsak spelsak1, Spelsak spelsak2, double scaleFactor) {
        for (Rect spelsak1boundrect : spelsak1.getScaledBounds()) {
            for (Rect spelsak2boundrect : spelsak2.getScaledBounds()) {
                scratchRectSpelsak1.set(spelsak1boundrect);
                scratchRectSpelsak1.offset((int) (spelsak1.getPosX() * scaleFactor), (int) (spelsak1.getPosY() * scaleFactor));
                scratchRectSpelsak2.set(spelsak2boundrect);
                scratchRectSpelsak2.offset((int) (spelsak2.getPosX() * scaleFactor), (int) (spelsak2.getPosY() * scaleFactor));
                if (scratchRectSpelsak1.intersects(scratchRectSpelsak2.left, scratchRectSpelsak2.top, scratchRectSpelsak2.right, scratchRectSpelsak2.bottom)) {
                    Log.w("spelsak", "kolliderarMed(): scratchRectSpelsak1=" + scratchRectSpelsak1 + ", scratchRectSpelsak2=" + scratchRectSpelsak2);
                    return true;
                }
            }
        }
        return false;
    }
}
