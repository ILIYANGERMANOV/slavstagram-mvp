package com.babushka.slav_squad.ui.screens.preview_post.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.widget.ScrollView;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Comment;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.ui.BaseActionBarActivity;
import com.babushka.slav_squad.ui.screens.comments.view.custom_view.AddCommentView;
import com.babushka.slav_squad.ui.screens.comments.view.custom_view.CommentsContainer;
import com.babushka.slav_squad.ui.screens.preview_post.PreviewPostContract;
import com.babushka.slav_squad.ui.screens.preview_post.model.PreviewPostModel;
import com.babushka.slav_squad.ui.screens.preview_post.presenter.PreviewPostPresenter;
import com.google.gson.Gson;

import butterknife.BindView;

/**
 * Created by iliyan on 26.09.17.
 */

public class PreviewPostActivity extends BaseActionBarActivity<PreviewPostContract.Presenter>
        implements PreviewPostContract.View, AddCommentView.Listener {
    private static final String EXTRA_POST = "post_extra";
    @BindView(R.id.activity_preview_post)
    NestedScrollView vRootScroll;
    @BindView(R.id.preview_post_comments_container)
    CommentsContainer vCommentsContainer;
    @BindView(R.id.preview_post_add_comment_view)
    AddCommentView vAddComment;
    private Post mPost;

    public static void startScreen(@NonNull Context context, @NonNull Post post) {
        Intent intent = new Intent(context, PreviewPostActivity.class);
        intent.putExtra(EXTRA_POST, new Gson().toJson(post));
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_preview_post;
    }

    @Override
    protected void onReadArguments(@NonNull Intent intent) {
        mPost = new Gson().fromJson(intent.getStringExtra(EXTRA_POST), Post.class);
        if (mPost == null) {
            throw new IllegalArgumentException("PreviewPostActivity requires '" + EXTRA_POST + "' to start!");
        }
    }

    @NonNull
    @Override
    protected PreviewPostContract.Presenter initializePresenter() {
        return new PreviewPostPresenter(this, new PreviewPostModel(mPost));
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
    public void setupAddCommentUI(@Nullable String authorImageUrl) {
        vAddComment.setup(authorImageUrl);
    }

    @Override
    public void onSendComment(@NonNull Comment comment) {
        mPresenter.addComment(comment);
        vRootScroll.fullScroll(ScrollView.FOCUS_DOWN);
    }
}
