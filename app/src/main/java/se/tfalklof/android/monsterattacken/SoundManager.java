package se.tfalklof.android.monsterattacken;

import java.util.HashMap;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

class SoundManager {
    private  Context mContext;
    private  SoundPool mSoundPool; 
    private  HashMap<Integer, Integer> mSoundPoolMap; 
    private  AudioManager  mAudioManager;

    private static final int maxSounds = 10;

    SoundManager() {
    }

    void initSounds(Context context) {
         mContext = context;
         mSoundPool = new SoundPool(maxSounds, AudioManager.STREAM_MUSIC, 0); 
         mSoundPoolMap = new HashMap<>();
         mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);         
    } 

    void addSound(int soundIndex, int soundID)
    {
        mSoundPoolMap.put(soundIndex, mSoundPool.load(mContext, soundID, 1));
    }

    void playSound(int soundIndex) {

         int streamVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); 
         mSoundPool.play(mSoundPoolMap.get(soundIndex), streamVolume, streamVolume, 1, 0, 1f); 
    }
}
