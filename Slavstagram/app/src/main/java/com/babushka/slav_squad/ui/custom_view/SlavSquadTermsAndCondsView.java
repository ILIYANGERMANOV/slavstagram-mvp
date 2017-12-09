package com.babushka.slav_squad.ui.custom_view;

import android.content.Context;
import android.util.AttributeSet;

import com.babushka.slav_squad.ui.screens.terms_and_conditions.TermsAndConditionsActivity;

/**
 * Created by iliyan on 09.12.17.
 */

public class SlavSquadTermsAndCondsView extends TermsAndConditionsTextView {
    public SlavSquadTermsAndCondsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setText("By signing in I accept Slav Squad terms and conditions");
        setHyperLinkStartIndex(34);
        setOnActionListener(new OnActionListener() {
            @Override
            public void onTextClicked() {

            }

            @Override
            public void onHyperLinkClicked() {
                TermsAndConditionsActivity.startScreen(getContext());
            }
        });
        init();
    }
}
