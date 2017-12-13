package com.babushka.slav_squad.analytics.event;

/**
 * Created by Iliyan on 14.07.17.
 */
public interface Events {
    String INCONSISTENT_DB = "inconsistent_db";
    String OPEN_SCREEN_ = "open_screen_";
    String BACK_SCREEN_ = "back_screen_";
    String HOME_SCREEN_ = "home_screen_";

    interface Screen {
        String OPEN = "open";
        String HOME = "home";
        String BACK = "back";
    }


}
