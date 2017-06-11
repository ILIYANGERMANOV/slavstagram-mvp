package com.babushka.slav_squad.ui.screens.comments.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Comment;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.BaseActionBarActivity;
import com.babushka.slav_squad.ui.screens.comments.CommentsContract;
import com.babushka.slav_squad.ui.screens.comments.model.CommentsModel;
import com.babushka.slav_squad.ui.screens.comments.presenter.CommentsPresenter;
import com.babushka.slav_squad.ui.screens.comments.view.custom_view.CommentsContainer;
import com.babushka.slav_squad.util.KeyboardUtil;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by iliyan on 10.06.17.
 */

public class CommentsActivity extends BaseActionBarActivity<CommentsContract.Presenter>
        implements CommentsContract.View {
    private static final String EXTRA_POST = "post_extra";

    @BindView(R.id.activity_comments)
    NestedScrollView vRootScroll;
    @BindView(R.id.comments_comments_container)
    CommentsContainer vCommentsContainer;
    @BindView(R.id.comments_author_circle_image_view)
    CircleImageView vAuthorImage;
    @BindView(R.id.comments_comment_edit_text)
    EditText vCommentInput;

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
    }

    @Override
    protected void onSetupFinished() {
        mPresenter.displayCurrentUser();
        mPresenter.displayCommentsInRealTime();
    }

    @OnClick(R.id.comments_send_image_button)
    public void onSendComment() {
        String commentText = vCommentInput.getText().toString();
        if (TextUtils.isEmpty(commentText)) {
            Toast.makeText(this, "Cannot send empty comments", Toast.LENGTH_LONG).show();
            return;
        }
        User author = SessionManager.getInstance().getCurrentUser();
        Comment comment = new Comment(author, commentText);
        updateUIAfterCommentSent();
        mPresenter.addComment(comment);

    }

    private void updateUIAfterCommentSent() {
        vCommentInput.setText("");
        KeyboardUtil.hideKeyboard(this);
        vRootScroll.fullScroll(ScrollView.FOCUS_DOWN);
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
    public void displayAuthorImage(@Nullable String imageUrl) {
        GlideApp.with(this)
                .load(imageUrl)
                .dontAnimate()
                .into(vAuthorImage);
    }

    @NonNull
    @Override
    protected CommentsContract.Presenter initializePresenter() {
        return new CommentsPresenter(this, new CommentsModel(mPost));
    }
}
