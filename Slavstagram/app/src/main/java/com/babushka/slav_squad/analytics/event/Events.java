package com.babushka.slav_squad.analytics.event;

/**
 * Created by Iliyan on 14.07.17.
 */
public interface Events {
    String INCONSISTENT_DB = "inconsistent_db";
    String OPEN_SCREEN_ = "open_screen_";
    String BACK_SCREEN_ = "back_screen_";
    String HOME_SCREEN_ = "home_screen_";

    interface Landing {
        String LOGIN_WITH_FB = "login_with_fb";
        String LOGIN_WITH_FB_SUCCESS = "login_with_fb_success";
        String LOGIN_WITH_FB_CANCEL = "login_with_fb_cancel";
        String LOGIN_WITH_FB_ERROR = "login_with_fb_error";
        String LOGIN_WITH_GOOGLE = "login_with_google";
        String LOGIN_WITH_GOOGLE_SUCCESS = "login_with_google_success";
        String LOGIN_WITH_GOOGLE_ERROR = "login_with_google_error";
        String EMAIL_CLICK = "landing_email_click";
    }

    interface Main {
        String PLAY_MUSIC = "play_music_main";
        String PAUSE_MUSIC = "pause_music_main";
    }

    interface Music {
        String REPEAT = "music_repeat";
        String VOLUME_ON = "volume_on";
        String VOLUME_OFF = "volume_off";
    }
}
