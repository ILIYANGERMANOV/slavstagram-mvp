package com.babushka.slav_squad.analytics.event;

/**
 * Created by Iliyan on 17.07.17.
 */
public interface EventValues {
    String TRUE = "true";
    String FALSE = "false";
    String BUTTON = "button";
    String IME_ACTION = "ime_action";

    interface Screen {
        String ABOUT = "about";
        String UPLOAD_POST = "upl_post";
        String LOGIN = "login";
        String REGISTER = "register";
        String LIKES = "likes";
        String MAIN = "main";
        String PROFILE = "profile";
        String EDIT_PROFILE = "edit_prof";
        String LANDING = "landing";
        String COMMENTS = "comments";
        String POST_PREVIEW = "post_prev";
        String TERMS_AND_CONDS = "terms_and_conds";
    }
}
