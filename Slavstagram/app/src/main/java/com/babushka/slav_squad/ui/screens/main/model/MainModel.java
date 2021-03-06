package com.babushka.slav_squad.ui.screens.main.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.listeners.DatabaseListener;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.storage.Storage;
import com.babushka.slav_squad.ui.screens.BasePostsModel;
import com.babushka.slav_squad.ui.screens.GalleryResult;
import com.babushka.slav_squad.ui.screens.main.MainContract;
import com.babushka.slav_squad.ui.screens.util.FileUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;

import java.io.File;
import java.io.IOException;

import static com.babushka.slav_squad.persistence.database.Database.getValueSafe;

/**
 * Created by iliyan on 02.06.17.
 */

public class MainModel extends BasePostsModel implements MainContract.Model {
    @NonNull
    private final Context mContext;

    public MainModel(@NonNull Context context, @NonNull FirebaseUser user) {
        super(user.getUid());
        mContext = context;
    }

    @Override
    public void addPostsListener(@NonNull final DatabaseListener<Post> postsListener) {
        Database database = Database.getInstance();
        database.addPostsListener(buildIsLikedPostTransformator(postsListener));
    }


    @Override
    public void removePostsListener() {
        Database.getInstance().removePostsListener();
    }

    @Override
    public void downloadImageToFile(@NonNull String imageUrl, @NonNull final DownloadCallback downloadCallback) {
        try {
            final File imageFile = FileUtil.createPublicImageFile(mContext);
            Storage.getInstance().downloadToFile(imageFile, imageUrl, new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    downloadCallback.onImageDownloaded(imageFile);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            downloadCallback.onError();
        }
    }

    @Override
    public void retrievePost(@NonNull String postId, @NonNull final RetrievePostCallback callback) {
        Database.getInstance().retrievePost(postId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Post post = getValueSafe(dataSnapshot, Post.class);
                if (post != null) {
                    callback.onPostRetrieved(post);
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError();
            }
        });
    }

    @NonNull
    @Override
    public Uri getSelectedImageFromGallery(@NonNull Intent data)
            throws GalleryResult.SelectedImageNotFoundException {
        GalleryResult galleryResult = new GalleryResult(mContext, data);
        return galleryResult.getSelectedImageFromGallery();
    }
}
