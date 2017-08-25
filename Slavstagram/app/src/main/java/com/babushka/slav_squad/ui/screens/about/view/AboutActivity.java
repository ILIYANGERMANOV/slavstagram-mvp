package com.babushka.slav_squad.ui.screens.about.view;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.ui.BaseActionBarActivity;
import com.babushka.slav_squad.ui.screens.about.AboutContract;
import com.babushka.slav_squad.ui.screens.about.presenter.AboutPresenter;

/**
 * Created by iliyan on 25.08.17.
 */

public class AboutActivity extends BaseActionBarActivity<AboutContract.Presenter>
        implements AboutContract.View {

    public static void startScreen(@NonNull Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_about;
    }

    @NonNull
    @Override
    protected AboutContract.Presenter initializePresenter() {
        return new AboutPresenter(this);
    }
}
