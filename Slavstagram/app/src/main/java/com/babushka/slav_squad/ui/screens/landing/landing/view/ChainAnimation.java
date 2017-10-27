package com.babushka.slav_squad.ui.screens.landing.landing.view;

import android.content.Context;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iliyan on 26.10.17.
 */

public class ChainAnimation {
    //TODO: Refactor
    private final List<ViewAnimPair> mAnims;

    private ChainAnimation(@NonNull List<ViewAnimPair> anims) {
        mAnims = anims;
    }

    public void run() {
        ViewAnimPair first = mAnims.get(0);
        Animation firstAnim = first.getAnim();
        firstAnim.setAnimationListener(new ChainListener(0));
        first.getView().startAnimation(firstAnim);
    }

    public static class Builder {
        @NonNull
        private final Context mContext;
        @NonNull
        private final List<ViewAnimPair> mAnims;

        public Builder(@NonNull Context context) {
            mContext = context;
            mAnims = new ArrayList<>();
        }

        public Builder add(@NonNull View view, @AnimRes int animRes) {
            Animation animation = AnimationUtils.loadAnimation(mContext, animRes);
            mAnims.add(new ViewAnimPair(view, animation));
            return this;
        }

        public ChainAnimation build() {
            if (mAnims.isEmpty()) {
                throw new IllegalStateException("at least one animation must be added");
            }
            return new ChainAnimation(mAnims);
        }
    }

    public static class ViewAnimPair {
        @NonNull
        private final View mView;

        @NonNull
        private final Animation mAnim;

        ViewAnimPair(@NonNull View view, @NonNull Animation anim) {
            mView = view;
            mAnim = anim;
        }

        @NonNull
        public View getView() {
            return mView;
        }

        @NonNull
        public Animation getAnim() {
            return mAnim;
        }

    }

    private class ChainListener implements Animation.AnimationListener {
        private int mIndex;

        public ChainListener(int index) {
            mIndex = index;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            ViewAnimPair pair = mAnims.get(mIndex);
            pair.getView().setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            int nextId = mIndex + 1;
            if (nextId == mAnims.size()) return;
            ViewAnimPair nextPair = mAnims.get(nextId);
            Animation nextPairAnim = nextPair.getAnim();
            nextPairAnim.setAnimationListener(new ChainListener(nextId));
            nextPair.getView().startAnimation(nextPairAnim);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
