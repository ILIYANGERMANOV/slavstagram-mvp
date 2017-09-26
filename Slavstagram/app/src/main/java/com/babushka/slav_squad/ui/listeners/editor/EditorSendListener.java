package com.babushka.slav_squad.ui.listeners.editor;

import android.view.inputmethod.EditorInfo;

/**
 * Created by iliyan on 26.09.17.
 */

public abstract class EditorSendListener extends EditorListener {
    @Override
    protected int getImeActionId() {
        return EditorInfo.IME_ACTION_SEND;
    }
}
