package com.babushka.slav_squad.ui.listeners.editor;

import android.view.inputmethod.EditorInfo;

/**
 * Created by phyre on 03.04.17.
 */

public abstract class EditorGoListener extends EditorListener {
    @Override
    protected int getImeActionId() {
        return EditorInfo.IME_ACTION_GO;
    }
}
