package com.babushka.slav_squad.ui.screens.register.view.fragment;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.babushka.slav_squad.R;
import com.babushka.slav_squad.session.data.UserDetails;
import com.babushka.slav_squad.ui.screens.register.view.RegisterSupport;
import com.babushka.slav_squad.ui.wizard.WizardFragment;
import com.babushka.slav_squad.util.KeyboardUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by iliyan on 16.06.17.
 */

public class RegisterFirstStepFragment extends WizardFragment<UserDetails, RegisterSupport> {
    @BindView(R.id.register_first_step_email_edit_text)
    EditText vEmailInput;
    @BindView(R.id.register_first_step_password_edit_text)
    EditText vPasswordInput;
    @BindView(R.id.register_first_step_display_name_edit_text)
    EditText vDisplayNameInput;
    @BindView(R.id.register_first_step_next_button)
    Button vNextButton;

    public static RegisterFirstStepFragment newInstance() {
        return new RegisterFirstStepFragment();
    }

    @Override
    protected void onNext(@NonNull RegisterSupport support, @NonNull UserDetails input) {
        support.onUserDetailsEntered(input);
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
                UserDetails input = getUserDetailsInput();
                vNextButton.setEnabled(input.validate());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        vEmailInput.addTextChangedListener(validityWatcher);
        vPasswordInput.addTextChangedListener(validityWatcher);
        vDisplayNameInput.addTextChangedListener(validityWatcher);
    }

    @NonNull
    private UserDetails getUserDetailsInput() {
        String email = vEmailInput.getText().toString();
        String password = vPasswordInput.getText().toString();
        String displayName = vDisplayNameInput.getText().toString();
        return new UserDetails(email, password, displayName);
    }

    @OnClick(R.id.register_first_step_next_button)
    public void onRegisterNextClicked() {
        KeyboardUtil.hideKeyboard(getActivity());
        UserDetails input = getUserDetailsInput();
        next(input);
    }
}
