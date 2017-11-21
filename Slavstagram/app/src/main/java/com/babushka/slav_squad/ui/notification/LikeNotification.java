package com.babushka.slav_squad.ui.notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.babushka.slav_squad.R;

/**
 * Created by iliyan on 21.11.17.
 */

public class LikeNotification extends PostNotification {
    public LikeNotification(@NonNull Context context, @NonNull String postId,
                            @NonNull String userName, @NonNull String userPhotoUrl) {
        super(context, postId, userName, userPhotoUrl);
    }

    public void buildAndShow() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setContentTitle("Post liked")
                .setContentText(String.format("%s squated on your post.", mUserName))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);
        buildPhotoActionAndShow(builder);
    }
}

