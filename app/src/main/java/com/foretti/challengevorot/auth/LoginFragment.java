package com.foretti.challengevorot.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import com.foretti.challengevorot.network.WebSocketManager;
import com.google.android.material.textfield.TextInputEditText;
import com.foretti.challengevorot.R;
import com.foretti.challengevorot.network.AuthRepository;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {

    private TextInputEditText etEmail;
    private TextInputEditText etPassword;
    private Button btnLogin;
    private TextView tvGoToRegistration;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupListeners();
    }

    private void initViews(View view) {
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnLogin = view.findViewById(R.id.btnLogin);
        tvGoToRegistration = view.findViewById(R.id.tvGoToRegistration);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> attemptLogin());

        tvGoToRegistration.setOnClickListener(v -> navigateToRegistration());
    }

    private void attemptLogin() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        //if (TextUtils.isEmpty(email)) {
            etEmail.setError("Введите email");
            //return;
        //}

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Введите пароль");
            return;
        }

        if (password.length() < 6) {
            etPassword.setError("Пароль должен содержать минимум 6 символов");
            return;
        }

        btnLogin.setEnabled(false);
        Toast.makeText(requireContext(), "Вход...", Toast.LENGTH_SHORT).show();

        AuthRepository.getInstance().login(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String id = response.getString("id");
                    boolean inRoom = response.getBoolean("inroom");

                    // Подключаемся к WebSocket с callback
                    WebSocketManager.getInstance().connect(id, new WebSocketManager.ConnectCallback() {
                        @Override
                        public void onConnected() {
                            // Соединение установлено - переходим к фрагменту
                            Log.d("ConnectingWebSocket", "Connected");
                            requireActivity().runOnUiThread(() -> {
                                btnLogin.setEnabled(true);
                                Toast.makeText(requireContext(), "Вход выполнен!", Toast.LENGTH_SHORT).show();

                                Bundle args = new Bundle();
                                args.putString("userId", id);

                                NavController navController = Navigation.findNavController(requireView());
                                if (inRoom) {
                                    navController.navigate(R.id.ProfileWithRoomFragment, args);
                                } else {
                                    navController.navigate(R.id.ProfileWithoutRoomFragment, args);
                                }
                            });
                        }

                        @Override
                        public void onError(String error) {
                            requireActivity().runOnUiThread(() -> {
                                btnLogin.setEnabled(true);
                                Toast.makeText(requireContext(), "WS Error: " + error, Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                } catch (JSONException e) {
                    // обработка ошибки
                }
            }

            @Override
            public void onError(String error) {
                // обработка ошибки
            }
        });
    }

    private void navigateToRegistration() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.RegistrationFragment);
    }
}
