package com.babushka.slav_squad.analytics.event;

/**
 * Created by Iliyan on 14.07.17.
 */
public interface Events {
    String INCONSISTENT_DB = "inconsistent_db";

    interface Screen {
        String OPEN = "open";
        String HOME = "home";
        String BACK = "back";
    }


}
