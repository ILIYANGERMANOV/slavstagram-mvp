package com.babushka.slav_squad.persistence.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by iliyan on 25.05.17.
 */

public class Database {
    private static Database sDatabase;
    private final DatabaseReference mDatabase;
    @Nullable
    private ChildEventListener mPostsEventListener;

    private Database() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public static Database getInstance() {
        if (sDatabase == null) {
            sDatabase = new Database();
        }
        return sDatabase;
    }

    public void saveUser(@NonNull String userId, @NonNull User user) {
        DatabaseReference currentUserRef = mDatabase.child(Table.USERS_TABLE).child(userId);
        currentUserRef.updateChildren(user.toCreationMap());
    }

    public void saveNewPost(@NonNull Post post, @NonNull final OperationListener listener) {
        String key = mDatabase.child(Table.POSTS_TABLE).push().getKey();
        post.setUid(key);
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put('/' + Table.POSTS_TABLE + '/' + key, postValues);
        //TODO: update connected tables
        //        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);
        mDatabase.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                listener.onError();
            }
        });
    }

    public void toggleLike(@NonNull Post post, @NonNull final String userId) {
        final DatabaseReference postRef = mDatabase.child(Table.POSTS_TABLE).child(post.getUid());
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post post = mutableData.getValue(Post.class);
                if (post == null) {
                    return Transaction.success(mutableData);
                }
                Map<String, Boolean> likes = post.getLikes();
                if (likes == null) {
                    //secure that likes are initialized
                    likes = new HashMap<>();
                    post.setLikes(likes);
                }
                if (likes.containsKey(userId)) {
                    //User has liked this post, unlike it
                    post.setLikesCount(post.getLikesCount() - 1);
                    likes.remove(userId);
                } else {
                    //User has NOT like this post, like it
                    post.setLikesCount(post.getLikesCount() + 1);
                    likes.put(userId, true);
                }
                // Set value and report transaction success
                mutableData.setValue(post);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                Timber.d("postTransaction:onComplete " + databaseError);
            }
        });
    }

    public void addPostsListener(@NonNull final PostsListener postsListener) {
        DatabaseReference postsRef = mDatabase.child(Table.POSTS_TABLE);
        mPostsEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Post post = dataSnapshot.getValue(Post.class);
                if (post != null) {
                    postsListener.onPostAdded(post);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Post post = dataSnapshot.getValue(Post.class);
                if (post != null) {
                    postsListener.onPostChanged(post);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);
                if (post != null) {
                    postsListener.onPostRemoved(post);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //TODO: Implement method
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null) {
                    postsListener.onError(databaseError);
                }
            }
        };
        //TODO: Consider add LIMIT [IMPORTANT]
        postsRef.addChildEventListener(mPostsEventListener);
    }

    public void removePostsListener() {
        if (mPostsEventListener != null) {
            mDatabase.child(Table.POSTS_TABLE).removeEventListener(mPostsEventListener);
        }
    }

    public interface OperationListener {
        void onSuccess();

        void onError();
    }
}
