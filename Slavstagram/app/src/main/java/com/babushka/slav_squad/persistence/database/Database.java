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
    @Nullable
    private ChildEventListener mUserPostsEventListener;

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

    public void saveNewPost(@NonNull String userId, @NonNull Post post,
                            @NonNull final OperationListener listener) {
        String key = mDatabase.child(Table.POSTS_TABLE).push().getKey();
        post.setUid(key);
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = getPostUpdateMap(userId, key, postValues);
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

    public void deletePost(@NonNull String userId, @NonNull String postId) {
        Map<String, Object> postDeleteMap = getPostUpdateMap(userId, postId, null);
        mDatabase.updateChildren(postDeleteMap);
    }

    @NonNull
    private Map<String, Object> getPostUpdateMap(@NonNull String userId, String postId, @Nullable Map<String, Object> postValues) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put('/' + Table.POSTS_TABLE + '/' + postId, postValues);
        childUpdates.put('/' + Table.USER_POSTS_TABLE + '/' + userId + "/" + postId, postValues);
        return childUpdates;
    }

    public void toggleLike(@NonNull Post post, @NonNull final String userId) {
        Transaction.Handler likeToggleHandler = new Transaction.Handler() {
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
        };
        String postId = post.getUid();
        final DatabaseReference postRef = mDatabase.child(Table.POSTS_TABLE).child(postId);
        postRef.runTransaction(likeToggleHandler);
        final DatabaseReference userPostRef = mDatabase.child(Table.USER_POSTS_TABLE)
                .child(userId)
                .child(postId);
        userPostRef.runTransaction(likeToggleHandler);
    }

    public void addPostsListener(@NonNull final PostsListener postsListener) {
        DatabaseReference postsRef = mDatabase.child(Table.POSTS_TABLE);
        mPostsEventListener = new DefaultChildEventListener(postsListener);
        //TODO: Consider add LIMIT [IMPORTANT]
        postsRef.addChildEventListener(mPostsEventListener);
    }

    public void addUserPostsListener(@NonNull String userId, @NonNull final PostsListener postsListener) {
        DatabaseReference userPostsRef = mDatabase.child(Table.USER_POSTS_TABLE).child(userId);
        mUserPostsEventListener = new DefaultChildEventListener(postsListener);
        userPostsRef.addChildEventListener(mUserPostsEventListener);
    }

    public void removePostsListener() {
        if (mPostsEventListener != null) {
            mDatabase.child(Table.POSTS_TABLE).removeEventListener(mPostsEventListener);
        }
    }

    public void removeUserPostsListener() {
        if (mUserPostsEventListener != null) {
            mDatabase.child(Table.POSTS_TABLE).removeEventListener(mUserPostsEventListener);
        }
    }

    public interface OperationListener {
        void onSuccess();

        void onError();
    }

    private static class DefaultChildEventListener implements ChildEventListener {
        @NonNull
        private final PostsListener mPostsListener;

        DefaultChildEventListener(@NonNull PostsListener postsListener) {
            mPostsListener = postsListener;
        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            Post post = dataSnapshot.getValue(Post.class);
            if (post != null) {
                mPostsListener.onPostAdded(post);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            Post post = dataSnapshot.getValue(Post.class);
            if (post != null) {
                mPostsListener.onPostChanged(post);
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            Post post = dataSnapshot.getValue(Post.class);
            if (post != null) {
                mPostsListener.onPostRemoved(post);
            }
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            //TODO: Implement method
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            if (databaseError != null) {
                mPostsListener.onError(databaseError);
            }
        }
    }
}
