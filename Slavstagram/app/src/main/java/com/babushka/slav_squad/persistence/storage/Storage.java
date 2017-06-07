package com.babushka.slav_squad.persistence.storage;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.persistence.database.model.Post;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.UUID;

/**
 * Created by iliyan on 05.06.17.
 */

public class Storage {
    private static final String IMAGE_FOLDER = "image";
    private static Storage sStorage;

    @NonNull
    private final StorageReference mStorageRef;
    @NonNull
    private final StorageReference mImagesRef;


    private Storage() {
        mStorageRef = FirebaseStorage.getInstance().getReference();
        mImagesRef = mStorageRef.child(IMAGE_FOLDER);
    }

    public static Storage getInstance() {
        if (sStorage == null) {
            sStorage = new Storage();
        }
        return sStorage;
    }

    public void uploadImage(@NonNull Uri imageUri, @NonNull final UploadImageListener uploadListener) {
        final ImageSize imageSize = getImageSize(imageUri);

        StorageMetadata metadata = new StorageMetadata.Builder()
                .setContentType("image/png")
                .build();

        StorageReference imageRef = mImagesRef.child(generateUniqueImageFileName());
        UploadTask uploadTask = imageRef.putFile(imageUri, metadata);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                if (downloadUrl != null) {
                    Post.Image image = new Post.Image(downloadUrl.toString(),
                            imageSize.getWidth(), imageSize.getHeight());
                    uploadListener.onImageUploaded(image);
                } else {
                    uploadListener.onError(new RuntimeException("Download url is null"));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                uploadListener.onError(e);
            }
        });
    }

    @NonNull
    private String generateUniqueImageFileName() {
        String uniqueId = UUID.randomUUID().toString();
        return uniqueId + ".png";
    }

    private ImageSize getImageSize(@NonNull Uri imageUri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(imageUri.getPath()).getAbsolutePath(), options);
        return new ImageSize(options.outWidth, options.outHeight);
    }

    public interface UploadImageListener {
        void onImageUploaded(@NonNull Post.Image image);

        void onError(@NonNull Exception e);
    }

    public static class ImageSize {
        private final double mWidth;
        private final double mHeight;

        public ImageSize(double width, double height) {

            mWidth = width;
            mHeight = height;
        }

        public double getWidth() {
            return mWidth;
        }

        public double getHeight() {
            return mHeight;
        }
    }
}