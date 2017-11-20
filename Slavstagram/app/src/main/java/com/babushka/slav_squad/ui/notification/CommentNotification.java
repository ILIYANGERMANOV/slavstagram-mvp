package com.babushka.slav_squad.ui.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.ui.screens.main.view.MainActivity;
import com.babushka.slav_squad.ui.screens.splash.SplashActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by iliyan on 20.11.17.
 */

public class CommentNotification {
    @NonNull
    private Context mContext;
    @NonNull
    private String mPostId;
    @NonNull
    private String mAuthorName;
    @NonNull
    private String mAuthorPhotoUrl;

    public CommentNotification(@NonNull Context context, @NonNull String postId,
                               @NonNull String authorName, @NonNull String authorPhotoUrl) {
        mContext = context;
        mAuthorName = authorName;
        mAuthorPhotoUrl = authorPhotoUrl;
        mPostId = postId;
    }

    public void buildAndShow() {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setContentTitle("New comment")
                .setContentText(String.format("%s commented on your post.", mAuthorName))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);

        try {
            builder.setLargeIcon(downloadBitmapFromURL(mAuthorPhotoUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent resultIntent = buildResultIntent();
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        mContext,
                        0, //requestCode
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setContentIntent(resultPendingIntent);

        NotificationManager notifyMgr =
                (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        notifyMgr.notify(new Random().nextInt(1000), builder.build());
    }

    private Bitmap downloadBitmapFromURL(String strURL) throws IOException {
        URL url = new URL(strURL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        return BitmapFactory.decodeStream(input);
    }

    @NonNull
    private Intent buildResultIntent() {
        return SplashActivity.buildEntryPointIntent(mContext, MainActivity.postPreviewStart(mPostId));
    }
}
