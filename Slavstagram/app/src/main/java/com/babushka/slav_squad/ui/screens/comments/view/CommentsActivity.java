package com.babushka.slav_squad.ui.screens.comments.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.BaseActionBarActivity;
import com.babushka.slav_squad.ui.screens.comments.CommentsContract;
import com.babushka.slav_squad.ui.screens.comments.model.CommentsModel;
import com.babushka.slav_squad.ui.screens.comments.presenter.CommentsPresenter;

/**
 * Created by iliyan on 10.06.17.
 */

public class CommentsActivity extends BaseActionBarActivity<CommentsContract.Presenter>
        implements CommentsContract.View {
    private static final String EXTRA_POST_ID = "post_id_extra";

    private String mPostId;

    public static void startScreen(@NonNull Context context, @NonNull Post post) {
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra(EXTRA_POST_ID, post.getId());
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_comments;
    }

    @Override
    protected void onReadArguments(@NonNull Intent intent) {
        mPostId = intent.getStringExtra(EXTRA_POST_ID);
        if (mPostId == null) {
            throw new IllegalArgumentException("CommentsActivity requires '" + EXTRA_POST_ID + "' to start!");
        }
    }

    @NonNull
    @Override
    protected CommentsContract.Presenter initializePresenter() {
        return new CommentsPresenter(this, new CommentsModel());
    }
}
