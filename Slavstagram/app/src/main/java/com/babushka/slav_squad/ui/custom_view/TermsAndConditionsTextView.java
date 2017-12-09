package com.babushka.slav_squad.ui.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;

import com.babushka.slav_squad.R;

/**
 * Created by iliyan on 09.12.17.
 */

public class TermsAndConditionsTextView extends AppCompatTextView {
    private int mHyperLinkStartIndex;

    private boolean mWasHyperLinkClicked = false;

    @Nullable
    private OnActionListener mOnActionListener;

    //TODO: Refactor this shitty initialization
    public TermsAndConditionsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TermsAndConditionsTextView);
        try {
            mHyperLinkStartIndex = ta.getInteger(R.styleable.TermsAndConditionsTextView_hyperLinkStartIndex, -1);
        } finally {
            ta.recycle();
        }
    }

    public void setHyperLinkStartIndex(int hyperLinkStartIndex) {
        mHyperLinkStartIndex = hyperLinkStartIndex;
    }

    public void setOnActionListener(@NonNull OnActionListener onActionListener) {
        mOnActionListener = onActionListener;
    }

    protected void init() {
        SpannableString spannableString = buildTermsAndConditionsSpannableString();
        setText(spannableString);
        setMovementMethod(LinkMovementMethod.getInstance());
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mWasHyperLinkClicked && mOnActionListener != null) {
                    mOnActionListener.onTextClicked();
                }
                mWasHyperLinkClicked = false;
            }
        });
    }

    @NonNull
    private SpannableString buildTermsAndConditionsSpannableString() {
        CharSequence text = getText();
        if (text == null) {
            throw new IllegalArgumentException("TermsAndConditionsTextView: text cannot be null");
        }
        String message = text.toString();
        SpannableString spannableString = new SpannableString(message);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                if (mOnActionListener != null) {
                    mOnActionListener.onHyperLinkClicked();
                }
                mWasHyperLinkClicked = true;
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };
        spannableString.setSpan(clickableSpan, mHyperLinkStartIndex,
                spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableString;
    }

    public interface OnActionListener {
        void onTextClicked();

        void onHyperLinkClicked();
    }
}