package com.babushka.slav_squad.ui.custom_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.View;

import com.babushka.slav_squad.MusicPlayer;
import com.babushka.slav_squad.MyApp;
import com.babushka.slav_squad.R;

/**
 * Created by iliyan on 11.11.17.
 */

public class VolumeButton extends AppCompatImageButton implements View.OnClickListener {
    @NonNull
    private final MusicPlayer mMusicPlayer;
    private boolean mIsVolumeOn;

    public VolumeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        mMusicPlayer = MyApp.getMusicPlayer();
        mIsVolumeOn = mMusicPlayer.isVolumeOn();
        setImageResource(mIsVolumeOn ? R.drawable.ic_volume_on
                : R.drawable.ic_volume_off);
    }

    public boolean isVolumeOn() {
        return mIsVolumeOn;
    }

    @Override
    public void onClick(View v) {
        if (mIsVolumeOn) {
            //volume is ON, turn in off
            mMusicPlayer.volumeOff();
            setImageResource(R.drawable.ic_volume_off);
        } else {
            //volume is OFF, turn it on
            mMusicPlayer.volumeOn();
            if (!mMusicPlayer.isPlaying()) {
                mMusicPlayer.play();
            }
            setImageResource(R.drawable.ic_volume_on);
        }
        mIsVolumeOn = !mIsVolumeOn;
    }
}
