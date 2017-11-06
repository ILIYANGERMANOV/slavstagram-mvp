package com.babushka.slav_squad.ui.screens.comments.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.widget.NestedScrollView;
import android.widget.ScrollView;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Comment;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.ui.BaseActionBarActivity;
import com.babushka.slav_squad.ui.screens.comments.CommentsContract;
import com.babushka.slav_squad.ui.screens.comments.model.CommentsModel;
import com.babushka.slav_squad.ui.screens.comments.presenter.CommentsPresenter;
import com.babushka.slav_squad.ui.screens.comments.view.custom_view.AddCommentView;
import com.babushka.slav_squad.ui.screens.comments.view.custom_view.CommentsContainer;
import com.google.gson.Gson;

import butterknife.BindView;

/**
 * Created by iliyan on 10.06.17.
 */

public class CommentsActivity extends BaseActionBarActivity<CommentsContract.Presenter>
        implements CommentsContract.View, AddCommentView.Listener {
    private static final String EXTRA_POST = "post_extra";

    @BindView(R.id.activity_comments)
    NestedScrollView vRootScroll;
    @BindView(R.id.comments_comments_container)
    CommentsContainer vCommentsContainer;
    @BindView(R.id.comments_add_comment_view)
    AddCommentView vAddComment;

    private Post mPost;

    public static void startScreen(@NonNull Context context, @NonNull Post post) {
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra(EXTRA_POST, new Gson().toJson(post));
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_comments;
    }

    @Override
    protected void onReadArguments(@NonNull Intent intent) {
        mPost = new Gson().fromJson(intent.getStringExtra(EXTRA_POST), Post.class);
        if (mPost == null) {
            throw new IllegalArgumentException("CommentsActivity requires '" + EXTRA_POST + "' to start!");
        }
    }

    @Override
    protected void onSetupUI() {
        super.onSetupUI();
        vCommentsContainer.setup(this);
        vAddComment.setListener(this);
    }

    @Override
    protected void onSetupFinished() {
        mPresenter.setupUI();
        mPresenter.displayCommentsInRealTime();
        //TODO: Remove this workaround
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollToBottom();
                vAddComment.focusInputField();
            }
        }, 1000);
    }

    @Override
    public void addComment(@NonNull Comment comment) {
        vCommentsContainer.add(comment);
    }

    @Override
    public void updateComment(@NonNull Comment comment) {
        vCommentsContainer.update(comment);
    }

    @Override
    public void removeComment(@NonNull Comment comment) {
        vCommentsContainer.remove(comment);
    }

    @Override
    public void setupAddCommentUI(@NonNull User currentUser) {
        vAddComment.setup(currentUser);
    }

    @Override
    public void onSendComment(@NonNull Comment comment) {
        mPresenter.addComment(comment);
        scrollToBottom();
    }

    private boolean scrollToBottom() {
        return vRootScroll.fullScroll(ScrollView.FOCUS_DOWN);
    }

    @NonNull
    @Override
    protected CommentsContract.Presenter initializePresenter() {
        return new CommentsPresenter(this, new CommentsModel(mPost));
    }
}
