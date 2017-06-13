package com.babushka.slav_squad.ui.listeners.editor;

import android.view.KeyEvent;
import android.widget.TextView;

/**
 * Created by phyre on 03.04.17.
 */

public abstract class EditorListener implements TextView.OnEditorActionListener {
    protected abstract int getImeActionId();

    /**
     * @return true to close the keyboard
     */
    protected abstract boolean onAction();

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        return actionId == getImeActionId() && !onAction();
    }
}
