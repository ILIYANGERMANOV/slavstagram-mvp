package com.babushka.slav_squad.messaging.firebase;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.ui.notification.CommentNotification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by iliyan on 10.06.17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //TODO: Implement foreground notification displaying
        Map<String, String> data = remoteMessage.getData();
        if (data != null) {
            String type = data.get("type");
            if (type == null) {
                throw new IllegalArgumentException("FirebaseMessage does not provide type");
            }
            switch (type) {
                case "comment_on_post":
                    handleCommentOnPostMessage(data);
                    break;
            }
        }
    }

    private void handleCommentOnPostMessage(@NonNull Map<String, String> data) {
        String postId = data.get("post_id");
        String authorName = data.get("author_name");
        String authorPhotoUrl = data.get("author_photo_url");

        CommentNotification notification =
                new CommentNotification(this, postId, authorName, authorPhotoUrl);
        notification.buildAndShow();
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        //TODO: Read documentation and implement method [CORNER-CASE]
    }
}
