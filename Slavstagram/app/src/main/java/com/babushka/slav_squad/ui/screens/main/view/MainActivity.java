package com.babushka.slav_squad.ui.screens.main.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.ui.screens.BaseActivity;
import com.babushka.slav_squad.ui.screens.main.MainContract;
import com.babushka.slav_squad.ui.screens.main.presenter.MainPresenter;
import com.babushka.slav_squad.ui.screens.main.view.custom_view.PostsContainer;

import butterknife.BindView;

public class MainActivity extends BaseActivity<MainPresenter>
        implements MainContract.View {
    @BindView(R.id.main_posts_container)
    PostsContainer mPostsContainer;

    public static void startScreen(@NonNull Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void onSetupUI() {
        mPostsContainer.setup(this);
    }

    @NonNull
    @Override
    protected MainPresenter initializePresenter() {
        return new MainPresenter(this);
    }
}
