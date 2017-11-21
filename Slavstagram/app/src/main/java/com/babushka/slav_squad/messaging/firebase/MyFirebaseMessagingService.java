package com.babushka.slav_squad.messaging.firebase;

import android.support.annotation.NonNull;

import com.babushka.slav_squad.ui.notification.CommentNotification;
import com.babushka.slav_squad.ui.notification.LikeNotification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

/**
 * Created by iliyan on 10.06.17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String COMMENT_ON_POST = "comment_on_post";
    private static final String LIKE_ON_POST = "like_on_post";
    private static final String POST_ID = "post_id";

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
                case COMMENT_ON_POST:
                    handleCommentOnPost(data);
                    break;
                case LIKE_ON_POST:
                    handleLikeOnPost(data);
            }
        }
    }

    private void handleCommentOnPost(@NonNull Map<String, String> data) {
        String postId = data.get(POST_ID);
        String authorName = data.get("author_name");
        String authorPhotoUrl = data.get("author_photo_url");

        CommentNotification notification =
                new CommentNotification(this, postId, authorName, authorPhotoUrl);
        notification.buildAndShow();
    }

    private void handleLikeOnPost(@NonNull Map<String, String> data) {
        String postId = data.get(POST_ID);
        String likerName = data.get("liker_name");
        String likerPhotoUrl = data.get("liker_photo_url");

        LikeNotification notification =
                new LikeNotification(this, postId, likerName, likerPhotoUrl);
        notification.buildAndShow();
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        //TODO: Read documentation and implement method [CORNER-CASE]
    }
}
