package com.babushka.slav_squad.ui.screens.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.Database;
import com.babushka.slav_squad.persistence.database.PostsListener;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.screens.splash.SplashActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.main_info_text_view)
    TextView vInfoText;
    private Post mPost;
    private FirebaseUser mUser;

    public static void startScreen(@NonNull Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setup();
    }

    private void setup() {
        ButterKnife.bind(this);
        setupUI();
        Database.getInstance().addPostsListener(new PostsListener() {
            @Override
            public void onPostAdded(@NonNull Post post) {
                Timber.d("onPostAdded: %s", post.toString());
            }

            @Override
            public void onPostChanged(@NonNull Post post) {
                Timber.d("onPostChanged: %s", post.toString());
            }

            @Override
            public void onPostRemoved(@NonNull Post post) {
                Timber.d("onPostRemoved: %s", post.toString());
            }

            @Override
            public void onError(@NonNull DatabaseError databaseError) {
                Timber.d("onError: %s", databaseError.getMessage());
            }
        });
    }

    private void setupUI() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String name = currentUser.getDisplayName();
            if (name == null) {
                name = currentUser.getEmail();
            }
            if (name == null) {
                name = "Anonymous";
            }
            vInfoText.setText("Logged as: " + name);
        }
    }

    @OnClick(R.id.main_logout_button)
    public void logout() {
        SessionManager.getInstance().logout();
        SplashActivity.startScreenAsEntryPoint(this);
    }

    @OnClick(R.id.main_add_post_button)
    public void addPost() {
        String imageUrl = "https://i.ytimg.com/vi/x6lv7FACT-o/hqdefault.jpg";
        mUser = SessionManager.getInstance().getCurrentUser();
        mPost = new Post(new User(mUser), "Awesome squatting slav", imageUrl);
        Database.getInstance().saveNewPost(mPost);

    }

    @OnClick(R.id.main_toggle_like_button)
    public void toggleLike() {
        Database.getInstance().toggleLike(mPost, mUser.getUid());
    }

    @OnClick(R.id.main_remove_listener_button)
    public void removeListener() {
        Database.getInstance().removePostsListener();
    }
}
