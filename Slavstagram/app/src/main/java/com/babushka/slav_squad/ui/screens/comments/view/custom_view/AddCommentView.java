package com.babushka.slav_squad.ui.screens.comments.view.custom_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.babushka.slav_squad.GlideApp;
import com.babushka.slav_squad.R;
import com.babushka.slav_squad.persistence.database.model.Comment;
import com.babushka.slav_squad.persistence.database.model.User;
import com.babushka.slav_squad.session.SessionManager;
import com.babushka.slav_squad.ui.listeners.editor.EditorSendListener;
import com.babushka.slav_squad.util.KeyboardUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by iliyan on 26.09.17.
 */

public class AddCommentView extends LinearLayout {
    @BindView(R.id.add_comment_author_circle_image_view)
    CircleImageView vAuthorImage;
    @BindView(R.id.add_comment_comment_edit_text)
    EditText vCommentInput;

    @Nullable
    private Listener mListener;

    public AddCommentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.add_comment_view, this);
        ButterKnife.bind(this);
        setGravity(Gravity.CENTER_VERTICAL);
        vCommentInput.setOnEditorActionListener(new EditorSendListener() {
            @Override
            protected boolean onAction() {
                sendComment();
                return false;
            }
        });
    }

    public void focusInputField() {
        vCommentInput.requestFocus();
    }

    public void setListener(@Nullable Listener listener) {
        mListener = listener;
    }

    @OnClick(R.id.add_comment_send_image_button)
    public void onSendClick() {
        sendComment();
    }

    private void sendComment() {
        String commentText = vCommentInput.getText().toString();
        if (TextUtils.isEmpty(commentText.trim())) {
            Toast.makeText(getContext(), "Cannot send empty comments", Toast.LENGTH_LONG).show();
            return;
        }
        User author = SessionManager.getInstance().getCurrentUser();
        Comment comment = new Comment(author, commentText);
        if (mListener != null) {
            mListener.onSendComment(comment);
        }
        updateUIAfterCommentSent();
    }

    private void updateUIAfterCommentSent() {
        vCommentInput.setText("");
        KeyboardUtil.hideKeyboard(getContext(), this);
    }

    public void setup(@Nullable String authorImageUrl) {
        GlideApp.with(this)
                .load(authorImageUrl)
                .dontAnimate()
                .into(vAuthorImage);
    }

    public interface Listener {
        void onSendComment(@NonNull Comment comment);
    }
}
