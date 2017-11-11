package com.babushka.slav_squad;

import android.app.Application;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;

/**
 * Created by iliyan on 11.11.17.
 */

public class MusicPlayer {
    private static final float NO_VOLUME = 0.0f;
    private static final float MAX_VOLUME = 1.0f;

    @NonNull
    private final Application mApplication;
    @Nullable
    private MediaPlayer mPlayer;

    private boolean mIsVolumeOn;

    public MusicPlayer(@NonNull Application application) {
        mApplication = application;
        mIsVolumeOn = true;
    }

    public void load(@RawRes int music) {
        release();
        mPlayer = newMediaPlayer(music);
    }

    @NonNull
    private MediaPlayer newMediaPlayer(@RawRes int music) {
        //TODO: Optimize method to use one MediaPlayer instance
        MediaPlayer mediaPlayer = MediaPlayer.create(mApplication, music);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
        return mediaPlayer;
    }

    public boolean isPlaying() {
        return mPlayer != null && mPlayer.isPlaying();
    }

    public boolean isVolumeOn() {
        return mIsVolumeOn;
    }

    public void play() {
        if (mPlayer != null) {
            mPlayer.start();
        }
    }

    public void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
        }
    }

    public void volumeOn() {
        if (mPlayer != null) {
            mPlayer.setVolume(MAX_VOLUME, MAX_VOLUME);
        }
        mIsVolumeOn = true;
    }

    public void volumeOff() {
        if (mPlayer != null) {
            mPlayer.setVolume(NO_VOLUME, NO_VOLUME);
        }
        mIsVolumeOn = false;
    }

    public void release() {
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }
}
