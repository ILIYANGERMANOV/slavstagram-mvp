package com.babushka.slav_squad.persistence.database;

/**
 * Created by iliyan on 28.05.17.
 */

public class Table {
    public static final String USERS_TABLE = "users";
    public static final String POSTS_TABLE = "posts";
    public static final String COMMENTS_TABLE = "comments";
    public static final String USER_POSTS_TABLE = "user-posts";

    public static class User {
        public static final String UID = "uid";
        public static final String EMAIL = "email";
        public static final String DISPLAY_NAME = "display_name";
        public static final String PHOTO_URL = "photo_url";
    }

    public static class Post {
        public static final String UID = "uid";
        public static final String AUTHOR = "author";
        public static final String IMAGE = "image";
        public static final String DESCRIPTION = "description";
        public static final String LIKES_COUNT = "likes_count";
        public static final String LIKES = "likes";
        public static final String COMMENTS = "comments";
        public static final String TIMESTAMP = "timestamp";
        public static final String INVERTED_TIMESTAMP = "inverted_timestamp";

        public static class Image {
            public static final String IMAGE_URL = "image_url";
            public static final String WIDTH = "width";
            public static final String HEIGHT = "height";
        }
    }

    public static class Comment {
        public static final String UID = "uid";
        public static final String TEXT = "text";
        /**
         * Corresponds to USER_ID
         */
        public static final String AUTHOR = "author";
        public static final String TIMESTAMP = "timestamp";
        public static final String INVERTED_TIMESTAMP = "inverted_timestamp";
    }
}
