package com.babushka.slav_squad.persistence.database;

/**
 * Created by iliyan on 28.05.17.
 */

public class Table {
    public static final String USERS_TABLE = "users";
    public static final String POSTS_TABLE = "posts";
    public static final String COMMENTS_TABLE = "comments";

    public static class User {
        public static final String DISPLAY_NAME = "display_name";
        public static final String PHOTO_URL = "photo_url";
        //TODO: Add security rule in Firebase to protect liked posts
        public static final String LIKED_POSTS = "liked_posts";
    }

    public static class Post {
        public static final String TITLE = "title";
        public static final String IMAGE_URL = "image_url";
        public static final String DESCRIPTION = "description";
        /**
         * Corresponds to USER_ID
         */
        public static final String AUTHOR_ID = "author_id";
        public static final String LIKES_COUNT = "likes_count";
        public static final String LIKES = "likes";
        public static final String COMMENTS = "comments";
        public static final String TIMESTAMP = "timestamp";
    }

    public static class Comment {
        public static final String MESSAGE = "message";
        /**
         * Corresponds to USER_ID
         */
        public static final String AUTHOR_ID = "author_id";
        public static final String TIMESTAMP = "timestamp";
    }
}
