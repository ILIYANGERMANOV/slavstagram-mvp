package com.babushka.slav_squad.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.babushka.slav_squad.MyApp;
import com.babushka.slav_squad.analytics.core.AnalyticsService;
import com.babushka.slav_squad.analytics.event.Event;
import com.babushka.slav_squad.analytics.event.EventBuilder;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by iliyan on 16.06.17.
 */

public abstract class BaseFragment extends Fragment {
    @Nullable
    private Unbinder mBind;

    @LayoutRes
    protected abstract int getContentView();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onReadArguments();
    }

    protected void onReadArguments() {
        //empty method
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View layoutView = inflater.inflate(getContentView(), container, false);
        mBind = ButterKnife.bind(this, layoutView);
        return layoutView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onSetupUI();
        onInitialized();
    }

    protected void onSetupUI() {
        //empty stub
    }

    protected void onInitialized() {
        //empty stub
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBind != null) {
            mBind.unbind();
        }
    }

    protected void logSimpleEvent(String eventName) {
        Event event = EventBuilder.simpleEvent(eventName);
        logEvent(event);
    }

    protected void logEvent(Event event) {
        getAnalytics().logEvent(event);
    }

    protected AnalyticsService getAnalytics() {
        return MyApp.getAnalytics();
    }
}