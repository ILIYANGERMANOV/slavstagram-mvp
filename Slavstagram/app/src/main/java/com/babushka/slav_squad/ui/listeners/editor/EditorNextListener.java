package com.babushka.slav_squad.ui.listeners.editor;

import android.view.inputmethod.EditorInfo;

/**
 * Created by phyre on 30.03.17.
 */

public abstract class EditorNextListener extends EditorListener {
    @Override
    protected int getImeActionId() {
        return EditorInfo.IME_ACTION_NEXT;
    }
}
