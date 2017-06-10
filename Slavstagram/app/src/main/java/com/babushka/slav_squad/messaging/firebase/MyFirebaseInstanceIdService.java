package com.babushka.slav_squad.messaging.firebase;

import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.session.SessionManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import timber.log.Timber;

/**
 * Created by iliyan on 10.06.17.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Timber.d("Refreshed token: %s", refreshedToken);

        //It is guaranteed notification token to be registered here
        sendNotificationTokenToServer(refreshedToken);
    }

    private void sendNotificationTokenToServer(String refreshedToken) {
        String userId = SessionManager.getInstance().getCurrentUser().getUid();
        Database.getInstance().updateUserNotificationToken(userId, refreshedToken);
    }
}
