package com.babushka.slav_squad.session;

/**
 * Created by iliyan on 23.05.17.
 */

public interface FacebookLoginCallback extends FirebaseLoginCallback {
    void onCancel();
}
