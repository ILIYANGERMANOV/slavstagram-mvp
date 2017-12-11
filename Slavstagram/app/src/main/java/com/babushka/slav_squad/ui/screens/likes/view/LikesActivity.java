package com.babushka.slav_squad.ui.screens.likes.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Post;
import com.babushka.slav_squad.persistence.database.model.UserBase;
import com.babushka.slav_squad.ui.BaseActionBarActivity;
import com.babushka.slav_squad.ui.screens.likes.LikesContract;
import com.babushka.slav_squad.ui.screens.likes.model.LikesModel;
import com.babushka.slav_squad.ui.screens.likes.presenter.LikesPresenter;
import com.babushka.slav_squad.ui.screens.likes.view.custom_view.LikesContainer;
import com.google.gson.Gson;

import butterknife.BindView;

/**
 * Created by iliyan on 09.11.17.
 */

public class LikesActivity extends BaseActionBarActivity<LikesContract.Presenter>
        implements LikesContract.View {
    private static final String EXTRA_POST = "post_extra";

    @BindView(R.id.likes_likes_container)
    LikesContainer vLikesContainer;
    private Post mPost;

    public static void startScreen(@NonNull Context context, @NonNull Post post) {
        Intent intent = new Intent(context, LikesActivity.class);
        intent.putExtra(EXTRA_POST, new Gson().toJson(post));
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_likes;
    }

    @Override
    protected void onReadArguments(@NonNull Intent intent) {
        mPost = new Gson().fromJson(intent.getStringExtra(EXTRA_POST), Post.class);
        if (mPost == null) {
            throw new IllegalArgumentException("LikesActivity requires '" + EXTRA_POST + "' to start!");
        }
    }

    @Override
    protected void onSetupUI() {
        super.onSetupUI();
        vLikesContainer.setup(this);
    }

    @Override
    protected void onSetupFinished() {
        mPresenter.displayLikes();
    }

    @NonNull
    @Override
    protected LikesContract.Presenter initializePresenter() {
        return new LikesPresenter(this, mPost, new LikesModel());
    }

    @Override
    public void showToast(@NonNull String message) {
        super.showToast(message);
    }

    @Override
    public void onLikeAdded(@NonNull UserBase user) {
        vLikesContainer.add(user);
    }

    @Override
    public void onLikeChanged(@NonNull UserBase user) {
        vLikesContainer.update(user);
    }

    @Override
    public void onLikeRemoved(@NonNull UserBase user) {
        vLikesContainer.remove(user);
    }
}
