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

    interface Login {
        String LOGIN_WITH_EMAIL = "login_with_email";
        String LOGIN_WITH_EMAIL_SUCCESS = "login_with_email_success";
        String LOGIN_WITH_EMAIL_ERROR = "login_with_email_error";
        String LOGIN_REGISTER_CLICK = "login_register_click";
    }

    interface Register {
        String REG_WIZARD_START = "reg_wizard_start";
        String FIRST_STEP_COMPLETE = "first_step_complete";
        String REG_WITH_EMAIL = "reg_with_email";
        String REG_WITH_EMAIL_SUCCESS = "reg_with_email_success";
        String REG_WITH_EMAIL_ERROR = "reg_with_email_error";
        String REG_WITH_EMAIL_SELECT_IMAGE = "reg_email_select_img";
        String REG_WITH_EMAIL_SELECT_START_CROP = "reg_email_select_img_start_crop";
        String REG_WITH_EMAIL_IMAGE_SELECTED = "reg_email_img_selected";
        String REG_WITH_EMAIL_SELECT_IMAGE_CANCEL = "reg_email_select_img_cancel";
    }

    interface Main {
        String PLAY_MUSIC = "play_music_main";
        String PAUSE_MUSIC = "pause_music_main";
        String FEED_SCROLL_UP = "feed_scroll_up";
        String FEED_SCROLL_DOWN = "feed_scroll_down";
        String POST_LIKE = "post_like";
        String POST_LIKE_DOUBLE_TAP = "post_like_double_tap";
        String POST_LIKES_COUNT_CLICK = "post_likes_count_click";
        String POST_UNLIKE = "post_unlike";
        String POST_COMMENT_CLICK = "post_comment_click";
        String POST_SHARE_CLICK = "post_share_click";
        String POST_DOWNLOAD = "post_download";
        String POST_OPEN_PROFILE = "post_open_profile";
        String POST_PREVIEW = "post_preview";
        String POST_IMAGE_LONG_CLICK = "post_image_long_click";
        String UPLOAD_POST_FAB_CLICK = "main_upload_post_fab_click";
        String UPLOAD_POST_CAMERA_CLICK = "main_upload_camera_click";
        String UPLOAD_POST_GALLERY_CLICK = "main_upload_galley_click";
        String UPLOAD_POST_CANCEL = "main_upload_post_cancel";
        String MAIN_LOGO_CLICK = "main_logo_click";
        String UPLOAD_POST_BACK = "main_upload_post_back";
        String NAV_DRAWER_OPEN = "nav_drawer_open";
        String NAV_DRAWER_CLOSE = "nav_drawer_close";
        String NAV_DRAWER_BACK = "nav_drawer_back";
        String NAV_DRAWER_HEADER_CLICK = "nav_drawer_header_click";
        String NAV_DRAWER_SHARE_APP = "nav_drawer_share_app";
        String NAV_DRAWER_FEEDBACK = "nav_drawer_feedback";
        String NAV_DRAWER_ABOUT = "nav_drawer_about_us";
        String NAV_DRAWER_LOGOUT = "nav_drawer_logout";
    }

    interface Permission {
        String READ_STORAGE_GRANTED = "read_storage_granted";
        String READ_STORAGE_DENY = "read_storage_deny";
        String READ_STORAGE_RATIONALE = "read_storage_rationale";
        String READ_STORAGE_NEVER_ASK = "read_storage_never_ask";
        String WRITE_STORAGE_GRANTED = "write_storage_granted";
        String WRITE_STORAGE_DENY = "write_storage_deny";
        String WRITE_STORAGE_RATIONALE = "write_storage_rationale";
        String WRITE_STORAGE_NEVER_ASK = "write_storage_never_ask";
        String CAMERA_GRANTED = "camera_granted";
        String CAMERA_DENY = "camera_deny";
        String CAMERA_RATIONALE = "camera_rationale";
        String CAMERA_NEVER_ASK = "camera_never_ask";
    }

    interface Music {
        String REPEAT = "music_repeat";
        String VOLUME_ON = "volume_on";
        String VOLUME_OFF = "volume_off";
    }
}
