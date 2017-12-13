package com.babushka.slav_squad.ui.custom_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.View;

import com.babushka.slav_squad.MusicPlayer;
import com.babushka.slav_squad.MyApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.analytics.event.EventBuilder;
import com.babushka.slav_squad.analytics.event.EventParamKeys;
import com.babushka.slav_squad.analytics.event.Events;

/**
 * Created by iliyan on 11.11.17.
 */

public class VolumeButton extends AppCompatImageButton implements View.OnClickListener {
    @NonNull
    private final MusicPlayer mMusicPlayer;
    private boolean mIsVolumeOn;

    @Nullable
    private String mFromScreen;

    public VolumeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
        mMusicPlayer = MyApp.getMusicPlayer();
        mIsVolumeOn = mMusicPlayer.isVolumeOn();
        setImageResource(mIsVolumeOn ? R.drawable.ic_volume_on
                : R.drawable.ic_volume_off);
    }

    public void setFromScreen(@Nullable String fromScreen) {
        mFromScreen = fromScreen;
    }

    public boolean isVolumeOn() {
        return mIsVolumeOn;
    }

    @Override
    public void onClick(View v) {
        if (mIsVolumeOn) {
            //volume is ON, turn in off
            volumeOff(true);
        } else {
            //volume is OFF, turn it on
            volumeOnAndPlay();
        }
    }

    public void volumeOff(boolean logEvent) {
        mMusicPlayer.volumeOff();
        setImageResource(R.drawable.ic_volume_off);
        mIsVolumeOn = false;
        if (logEvent) {
            MyApp.logEvent(new EventBuilder()
                    .setEventName(Events.Music.VOLUME_OFF)
                    .addParam(EventParamKeys.FROM_SCREEN, mFromScreen)
                    .build());
        }
    }

    private void volumeOnAndPlay() {
        volumeOn();
        if (!mMusicPlayer.isPlaying()) {
            mMusicPlayer.play();
        }
    }

    public void volumeOn() {
        mMusicPlayer.volumeOn();
        setImageResource(R.drawable.ic_volume_on);
        mIsVolumeOn = true;
        MyApp.logEvent(new EventBuilder()
                .setEventName(Events.Music.VOLUME_ON)
                .addParam(EventParamKeys.FROM_SCREEN, mFromScreen)
                .build());
    }
}
