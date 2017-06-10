package com.babushka.slav_squad.messaging.firebase;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by iliyan on 10.06.17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //TODO: Implement foreground notification displaying
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        //TODO: Read documentation and implement method [CORNER-CASE]
    }
}
