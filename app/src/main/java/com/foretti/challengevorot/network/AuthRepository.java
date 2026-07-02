package com.foretti.challengevorot.network;

import android.util.Log;

import androidx.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

public class AuthRepository {
    String Url = "http://185.254.158.107:8888";

    private static final String TAG = "AuthRepository";
    private static AuthRepository instance;
    private final OkHttpClient client;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    public interface AuthCallback {
        void onSuccess(JSONObject response);
        void onError(String error);
    }

    private AuthRepository() {
        client = new OkHttpClient();
    }

    public static synchronized AuthRepository getInstance() {
        if (instance == null) {
            instance = new AuthRepository();
        }
        return instance;
    }

    // POST запрос для логина
    public void login(String email, String password, AuthCallback callback) {
        String url = Url + "/posts/login";
        String json = "{\"user\":\"" + email + "\",\"password\":\"" + password + "\"}";
        Log.d("JSON", json);
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "Login failed: " + e.getMessage());
                if (callback != null) {
                    callback.onError(e.getMessage() != null ? e.getMessage() : "Ошибка сети");
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body() != null ? response.body().string() : "";
                Log.d(TAG, "Login response: " + response.code() + " " + responseBody);
                
                if (callback != null) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject json = new JSONObject(responseBody);
                            callback.onSuccess(json);
                        } catch (JSONException e) {
                            callback.onError("Ошибка парсинга JSON: " + e.getMessage());
                        }
                    } else {
                        callback.onError("Ошибка: " + response.code() + " " + responseBody);
                    }
                }
            }
        });
    }

    // POST запрос для регистрации
    public void register(String name, String email, String password, AuthCallback callback) {
        String url = Url + "/posts/register";
        String json = "{\"username\":\"" + name + "\",\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        Log.d("JSON", json);

        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e(TAG, "Register failed: " + e.getMessage());
                if (callback != null) {
                    callback.onError(e.getMessage() != null ? e.getMessage() : "Ошибка сети");
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseBody = response.body() != null ? response.body().string() : "";
                Log.d(TAG, "Register response: " + response.code() + " " + responseBody);

                if (callback != null) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject json = new JSONObject(responseBody);
                            callback.onSuccess(json);
                        } catch (JSONException e) {
                            callback.onError("Ошибка парсинга JSON: " + e.getMessage());
                        }
                    } else {
                        callback.onError("Ошибка: " + response.code() + " " + responseBody);
                    }
                }
            }
        });
    }
}
