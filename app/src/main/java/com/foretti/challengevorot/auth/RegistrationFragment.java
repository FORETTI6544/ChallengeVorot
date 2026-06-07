package com.foretti.challengevorot.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.foretti.challengevorot.R;
import com.foretti.challengevorot.network.AuthRepository;

import org.json.JSONObject;

public class RegistrationFragment extends Fragment {

    private TextInputEditText etName;
    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private Button btnRegister;
    private TextView tvGoToLogin;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupListeners();
    }

    private void initViews(View view) {
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnRegister = view.findViewById(R.id.btnRegister);
        tvGoToLogin = view.findViewById(R.id.tvGoToLogin);
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> attemptRegistration());

        tvGoToLogin.setOnClickListener(v -> navigateToLogin());
    }

    private void attemptRegistration() {
        String name = etName.getText() != null ? etName.getText().toString().trim() : "";
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        if (TextUtils.isEmpty(name)) {
            etName.setError("Введите имя");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Введите email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Введите пароль");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Пароль должен содержать минимум 6 символов");
            return;
        }

        btnRegister.setEnabled(false);
        Toast.makeText(requireContext(), "Регистрация...", Toast.LENGTH_SHORT).show();
        AuthRepository.getInstance().register(name, email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                requireActivity().runOnUiThread(() -> {
                    btnRegister.setEnabled(true);
                    Toast.makeText(requireContext(), "Регистрация выполнена!", Toast.LENGTH_SHORT).show();
                    navigateToLogin();
                });
            }

            @Override
            public void onError(String error) {
                requireActivity().runOnUiThread(() -> {
                    btnRegister.setEnabled(true);
                    Toast.makeText(requireContext(), "Ошибка: " + error, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void navigateToLogin() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.LoginFragment);
    }
}
