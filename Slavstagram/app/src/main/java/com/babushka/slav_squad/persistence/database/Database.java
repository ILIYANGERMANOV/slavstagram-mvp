package com.babushka.slav_squad.persistence.database;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.babushka.slav_squad.persistence.database.listeners.CommentsListener;
import com.babushka.slav_squad.persistence.database.listeners.PostsListener;
import com.babushka.slav_squad.persistence.database.model.Comment;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.persistence.storage.Storage;
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
    //TODO: Fix possible issues with concurrency and failed transactions
    private static Database sDatabase;
    private final DatabaseReference mDatabase;
    @Nullable
    private ChildEventListener mPostsEventListener;
    @Nullable
    private ChildEventListener mUserPostsEventListener;
    @Nullable
    private ChildEventListener mCommentsEventListener;

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

    public void updateUserNotificationToken(@NonNull String userId, @NonNull String token) {
        DatabaseReference currentUserRef = mDatabase.child(Table.USERS_TABLE).child(userId);
        currentUserRef.child(Table.User.NOTIFICATION_TOKEN).setValue(token);
    }

    public void saveNewPost(@NonNull Post post, @NonNull final OperationListener listener) {
        String key = mDatabase.child(Table.POSTS_TABLE).push().getKey();
        String authorId = post.getAuthor().getUid();
        post.setUid(key);
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = getPostUpdateMap(authorId, key, postValues);
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

    public void deletePost(@NonNull Post post) {
        String authorId = post.getAuthor().getUid();
        Map<String, Object> postDeleteMap = getPostUpdateMap(authorId, post.getId(), null);
        mDatabase.updateChildren(postDeleteMap);
        Storage.getInstance().deleteImage(post.getImage().getImageUrl());
        deleteCommentsOnPost(post);
    }

    @NonNull
    private Map<String, Object> getPostUpdateMap(@NonNull String authorId, String postId, @Nullable Map<String, Object> postValues) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put('/' + Table.POSTS_TABLE + '/' + postId, postValues);
        childUpdates.put('/' + Table.USER_POSTS_TABLE + '/' + authorId + '/' + postId, postValues);
        return childUpdates;
    }

    private void deleteCommentsOnPost(@NonNull Post post) {
        DatabaseReference commentsOnPostRef = mDatabase.child(Table.COMMENTS_TABLE)
                .child(post.getUid());
        commentsOnPostRef.setValue(null);
    }

    public void toggleLike(@NonNull Post post, @NonNull User user) {
        String postId = post.getUid();
        String authorId = post.getAuthor().getUid();

        DatabaseReference postRef = mDatabase.child(Table.POSTS_TABLE)
                .child(postId);
        DatabaseReference userPostRef = mDatabase.child(Table.USER_POSTS_TABLE)
                .child(authorId)
                .child(postId);

        postRef.runTransaction(buildLikeToggleHandler(user));
        userPostRef.runTransaction(buildLikeToggleHandler(user));
    }

    @NonNull
    private Transaction.Handler buildLikeToggleHandler(@NonNull final User user) {
        return new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post post = mutableData.getValue(Post.class);
                if (post == null) {
                    return Transaction.success(mutableData);
                }
                Map<String, User> likes = post.getLikes();
                if (likes == null) {
                    //secure that likes are initialized
                    likes = new HashMap<>();
                    post.setLikes(likes);
                }

                String userId = user.getUid();
                if (likes.containsKey(userId)) {
                    //User has liked this post, unlike it
                    post.setLikesCount(post.getLikesCount() - 1);
                    likes.remove(userId);
                } else {
                    //User has NOT like this post, like it
                    post.setLikesCount(post.getLikesCount() + 1);
                    likes.put(userId, user);
                }

                mutableData.setValue(post);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                if (databaseError != null) {
                    Timber.e("togglePostLikeTransaction:onError: %s " + databaseError.getMessage());
                }
            }
        };
    }

    public void addComment(@NonNull Post post, @NonNull Comment comment) {
        //TODO: Consider error handling
        //Add comment in COMMENTS table
        DatabaseReference newCommentRef = mDatabase.child(Table.COMMENTS_TABLE)
                .child(post.getUid())
                .push();
        comment.setUid(newCommentRef.getKey());
        newCommentRef.setValue(comment);

        //Increment post's comments_count in POSTS AND USER-POSTS tables
        updateCommentsCount(post, true);
    }

    public void deleteComment(@NonNull Post post, @NonNull Comment comment) {
        mDatabase.child(Table.COMMENTS_TABLE)
                .child(post.getId())
                .child(comment.getId())
                .setValue(null);
        updateCommentsCount(post, false);
    }

    private void updateCommentsCount(@NonNull Post post, boolean increment) {
        String postId = post.getUid();
        String postAuthorId = post.getAuthor().getUid();

        DatabaseReference postCommentsCountRef = mDatabase.child(Table.POSTS_TABLE)
                .child(postId)
                .child(Table.Post.COMMENTS_COUNT);
        DatabaseReference userPostCommentsCountRef = mDatabase.child(Table.USER_POSTS_TABLE)
                .child(postAuthorId)
                .child(postId)
                .child(Table.Post.COMMENTS_COUNT);

        postCommentsCountRef.runTransaction(buildCommentsCountHandler(increment));
        userPostCommentsCountRef.runTransaction(buildCommentsCountHandler(increment));
    }

    private Transaction.Handler buildCommentsCountHandler(final boolean increment) {
        return new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Integer commentsCount = mutableData.getValue(Integer.class);
                if (commentsCount == null) {
                    return Transaction.success(mutableData);
                }

                int newCommentsCount = increment ? commentsCount + 1 : commentsCount - 1;
                mutableData.setValue(newCommentsCount);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b,
                                   DataSnapshot dataSnapshot) {
                // Transaction completed
                if (databaseError != null) {
                    Timber.e("incrementPostCommentsCountTransaction:onError: %s" + databaseError.getMessage());
                }
            }
        };
    }

    public void addPostsListener(@NonNull final PostsListener postsListener) {
        DatabaseReference postsRef = mDatabase.child(Table.POSTS_TABLE);
        mPostsEventListener = new DefaultPostEventListener(postsListener);
        //TODO: Consider add LIMIT [IMPORTANT]
        postsRef.addChildEventListener(mPostsEventListener);
    }

    public void addUserPostsListener(@NonNull String userId, @NonNull final PostsListener listener) {
        DatabaseReference userPostsRef = mDatabase
                .child(Table.USER_POSTS_TABLE)
                .child(userId);
        mUserPostsEventListener = new DefaultPostEventListener(listener);
        userPostsRef.addChildEventListener(mUserPostsEventListener);
    }

    public void addCommentsListener(@NonNull Post post, @NonNull final CommentsListener listener) {
        DatabaseReference postCommentsRef = mDatabase.child(Table.COMMENTS_TABLE)
                .child(post.getUid());
        mCommentsEventListener = buildCommentsEventListener(listener);
        postCommentsRef.addChildEventListener(mCommentsEventListener);
    }

    @NonNull
    private ChildEventListener buildCommentsEventListener(@NonNull final CommentsListener listener) {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                if (comment != null) {
                    listener.onCommentAdded(comment);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                if (comment != null) {
                    listener.onCommentChanged(comment);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Comment comment = dataSnapshot.getValue(Comment.class);
                if (comment != null) {
                    listener.onCommentRemoved(comment);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                //TODO: Implement method
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (databaseError != null) {
                    listener.onError(databaseError);
                }
            }
        };
    }

    public void removePostsListener() {
        if (mPostsEventListener != null) {
            mDatabase.child(Table.POSTS_TABLE)
                    .removeEventListener(mPostsEventListener);
        }
    }

    public void removeUserPostsListener(@NonNull String userId) {
        if (mUserPostsEventListener != null) {
            mDatabase.child(Table.USER_POSTS_TABLE)
                    .child(userId)
                    .removeEventListener(mUserPostsEventListener);
        }
    }

    public void removeCommentsEventListener(@NonNull String postId) {
        if (mCommentsEventListener != null) {
            mDatabase.child(Table.COMMENTS_TABLE)
                    .child(postId)
                    .removeEventListener(mCommentsEventListener);
        }
    }

    public interface OperationListener {
        void onSuccess();

        void onError();
    }

    private static class DefaultPostEventListener implements ChildEventListener {
        @NonNull
        private final PostsListener mPostsListener;

        DefaultPostEventListener(@NonNull PostsListener postsListener) {
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
