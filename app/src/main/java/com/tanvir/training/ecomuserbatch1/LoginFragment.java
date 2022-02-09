package com.tanvir.training.ecomuserbatch1;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tanvir.training.ecomuserbatch1.databinding.FragmentLoginBinding;
import com.tanvir.training.ecomuserbatch1.viewmodels.LoginViewModel;

import java.util.Arrays;
import java.util.List;

public class LoginFragment extends Fragment {
    private LoginViewModel loginViewModel;
    private FragmentLoginBinding binding;
    private boolean isLogin;
    private String phoneNumber;
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            result -> onSignInResult(result)
    );
    public LoginFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater);
        loginViewModel = new ViewModelProvider(requireActivity())
                .get(LoginViewModel.class);
        binding.loginBtn.setOnClickListener(v -> {
            isLogin = true;
            authenticate();
        });
        binding.registerBtn.setOnClickListener(v -> {
            isLogin = false;
            authenticate();
        });

        loginViewModel.getErrMsgLiveData()
                .observe(getViewLifecycleOwner(), errMsg -> {
                    binding.errMsgTV.setText(errMsg);
                });


        return binding.getRoot();
    }

    private void authenticate() {
        final String email = binding.emailInputET.getText().toString();
        final String password = binding.passwordInputET.getText().toString();

        if (isLogin) {
            loginViewModel.login(email, password);
        }else {
            //loginViewModel.register(email, password);
            if (phoneNumber == null) {
                createPhoneAuthFlow();
            }else {
                loginViewModel.register(email, password, phoneNumber);
            }

        }
    }

    private void createPhoneAuthFlow() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.PhoneBuilder().build());

// Create and launch sign-in intent
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // ...
            //now, check Users collection to see whether this phone
            //number is being used by another user or not
            phoneNumber = user.getPhoneNumber();

        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }
}