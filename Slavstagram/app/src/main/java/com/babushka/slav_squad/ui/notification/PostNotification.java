package com.babushka.slav_squad.ui.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.babushka.slav_squad.ui.screens.main.view.MainActivity;
import com.babushka.slav_squad.ui.screens.splash.SplashActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by iliyan on 21.11.17.
 */

public class PostNotification {
    @NonNull
    protected final Context mContext;
    @NonNull
    protected final String mUserName;
    @NonNull
    protected final String mUserPhotoUrl;
    @NonNull
    protected final String mPostId;

    public PostNotification(@NonNull Context context, @NonNull String postId,
                            @NonNull String userName, @NonNull String userPhotoUrl) {
        mContext = context;
        mUserPhotoUrl = userPhotoUrl;
        mUserName = userName;
        mPostId = postId;
    }

    protected void buildPhotoActionAndShow(@NonNull NotificationCompat.Builder builder) {

        try {
            builder.setLargeIcon(downloadBitmapFromURL(mUserPhotoUrl));
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
