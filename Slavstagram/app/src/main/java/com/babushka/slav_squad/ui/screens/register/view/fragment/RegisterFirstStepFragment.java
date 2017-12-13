package com.babushka.slav_squad.ui.screens.register.view.fragment;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.analytics.event.EventValues;
import com.babushka.slav_squad.analytics.event.Events;
import com.babushka.slav_squad.session.data.LoginDetails;
import com.babushka.slav_squad.ui.custom_view.VolumeButton;
import com.babushka.slav_squad.ui.listeners.editor.EditorNextListener;
import com.babushka.slav_squad.ui.screens.register.view.RegisterSupport;
import com.babushka.slav_squad.ui.wizard.WizardFragment;
import com.babushka.slav_squad.util.KeyboardUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by iliyan on 16.06.17.
 */

public class RegisterFirstStepFragment extends WizardFragment<LoginDetails, RegisterSupport> {
    @BindView(R.id.register_first_step_volume_button)
    VolumeButton vVolumeButton;
    @BindView(R.id.register_first_step_email_edit_text)
    EditText vEmailInput;
    @BindView(R.id.register_first_step_password_edit_text)
    EditText vPasswordInput;
    @BindView(R.id.register_first_step_confirm_password_edit_text)
    EditText vConfirmPasswordInput;
    @BindView(R.id.register_first_step_next_button)
    Button vNextButton;

    public static RegisterFirstStepFragment newInstance() {
        return new RegisterFirstStepFragment();
    }

    @Override
    protected void onNext(@NonNull RegisterSupport support, @NonNull LoginDetails input) {
        support.onFirstStepCompleted(input);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_register_first_step;
    }

    @Override
    protected void onSetupUI() {
        TextWatcher validityWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                LoginDetails input = getLoginDetailsInput();
                vNextButton.setEnabled(isValidInput(input));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        vEmailInput.addTextChangedListener(validityWatcher);
        vPasswordInput.addTextChangedListener(validityWatcher);
        vConfirmPasswordInput.addTextChangedListener(validityWatcher);
        vConfirmPasswordInput.setOnEditorActionListener(new EditorNextListener() {
            @Override
            protected boolean onAction() {
                LoginDetails loginDetails = getLoginDetailsInput();
                if (isValidInput(loginDetails)) {
                    next(loginDetails);
                    return true;
                } else {
                    Toast.makeText(getContext(), "Input not valid!", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });
        vVolumeButton.setFromScreen(EventValues.Screen.REGISTER_FIRST_STEP);
    }

    @Override
    protected void onInitialized() {
        logSimpleEvent(Events.OPEN_SCREEN_ + EventValues.Screen.REGISTER_FIRST_STEP);
    }

    private boolean isValidInput(LoginDetails input) {
        return input.validate() &&
                vPasswordInput.getText().toString().equals(vConfirmPasswordInput.getText().toString());
    }

    @NonNull
    private LoginDetails getLoginDetailsInput() {
        String email = vEmailInput.getText().toString();
        String password = vPasswordInput.getText().toString();
        return new LoginDetails(email, password);
    }

    @OnClick(R.id.register_first_step_next_button)
    public void onRegisterNextClicked() {
        KeyboardUtil.hideKeyboard(getActivity());
        LoginDetails input = getLoginDetailsInput();
        next(input);
    }
}
