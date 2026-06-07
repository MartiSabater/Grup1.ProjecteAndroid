package com.example.sigmadsa.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sigmadsa.R;
import com.example.sigmadsa.api.ApiClient;
import com.example.sigmadsa.api.ApiService;
import com.example.sigmadsa.api.LoginRequest;
import com.example.sigmadsa.api.LoginResponse;
import com.example.sigmadsa.api.RegisterRequest;
import com.example.sigmadsa.api.RegisterResponse;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadingActivity extends AppCompatActivity {

    public static final String EXTRA_ACTION = "extra_action";
    public static final String EXTRA_USERNAME = "extra_username";
    public static final String EXTRA_USER_ID = "extra_user_id";
    public static final String EXTRA_PASSWORD = "extra_password";
    public static final String EXTRA_EMAIL = "extra_email";
    public static final String EXTRA_NAME = "extra_name";

    public static final String ACTION_LOGIN = "action_login";
    public static final String ACTION_REGISTER = "action_register";

    private TextView tvLoadingText;
    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        tvLoadingText = findViewById(R.id.tv_loading_text);
        tvLoadingText.setText("Conectando con el servidor . . .");

        String action = getIntent().getStringExtra(EXTRA_ACTION);
        if (ACTION_LOGIN.equals(action)) {
            executeLogin();
        } else if (ACTION_REGISTER.equals(action)) {
            executeRegister();
        } else {
            startDefaultLoading();
        }
    }

    private void executeLogin() {
        String loginId = getIntent().getStringExtra(EXTRA_USERNAME);
        String password = getIntent().getStringExtra(EXTRA_PASSWORD);
        boolean loginWithEmail = loginId != null && Patterns.EMAIL_ADDRESS.matcher(loginId).matches();

        ApiService apiService = ApiClient.getApiService();
        LoginRequest request = new LoginRequest(
                loginWithEmail ? null : loginId,
                loginWithEmail ? loginId.toLowerCase() : null,
                password
        );

        apiService.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String userId = response.body().getId();
                    String token = response.body().getToken();
                    ApiClient.setAuthToken(token);
                    if (userId == null) {
                        userId = loginId;
                    }
                    saveSession(loginId, userId, token);
                    tvLoadingText.setText("Sesion iniciada. Redirigiendo . . .");
                    final String uid = userId;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startShop(loginId, uid);
                        }
                    }, 1000);
                } else {
                    String message = "Usuario, correo o contrasena incorrectos";
                    if (response.code() == 400) {
                        message = "Rellena usuario o correo y contrasena";
                    } else if (response.code() == 405) {
                        message = "Servidor mal configurado para login (405)";
                    } else if (response.code() >= 500) {
                        message = "Imposible conectarse con el servidor";
                    }
                    showErrorAndReturn(message, LoginActivity.class);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showErrorAndReturn(getConnectionErrorMessage(t), LoginActivity.class);
            }
        });
    }

    private void executeRegister() {
        String username = getIntent().getStringExtra(EXTRA_USERNAME);
        String email = getIntent().getStringExtra(EXTRA_EMAIL);
        String name = getIntent().getStringExtra(EXTRA_NAME);
        String password = getIntent().getStringExtra(EXTRA_PASSWORD);
        String avatar = "avatar_1";

        ApiService apiService = ApiClient.getApiService();
        apiService.register(new RegisterRequest(username, email, name, password, avatar)).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String userId = response.body().getId();
                    String token = response.body().getToken();
                    ApiClient.setAuthToken(token);
                    if (userId == null) {
                        userId = username;
                    }
                    saveSession(username, userId, token);
                    tvLoadingText.setText("Registro completo. Redirigiendo . . .");
                    final String uid = userId;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startShop(username, uid);
                        }
                    }, 1000);
                } else {
                    String message = "Error en el registro";
                    if (response.code() == 400) {
                        message = "Revisa correo, contrasena y campos obligatorios";
                    } else if (response.code() == 409) {
                        message = "Ese usuario o correo ya existe";
                    } else if (response.code() >= 500) {
                        message = "Error del servidor. Intenta mas tarde";
                    }
                    showErrorAndReturn(message, RegisterActivity.class);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                showErrorAndReturn(getConnectionErrorMessage(t), RegisterActivity.class);
            }
        });
    }

    private String getConnectionErrorMessage(Throwable t) {
        if (t instanceof UnknownHostException) {
            return "No se encuentra el servidor";
        }
        if (t instanceof ConnectException) {
            return "No se puede abrir conexion con el servidor";
        }
        if (t instanceof SocketTimeoutException) {
            return "Tiempo de espera agotado con el servidor";
        }
        if (t instanceof IOException) {
            return "Error de red: " + (t.getMessage() != null ? t.getMessage() : "sin detalle");
        }
        return "Error: " + t.getClass().getSimpleName() + (t.getMessage() != null ? " - " + t.getMessage() : "");
    }

    private void showErrorAndReturn(String errorMessage, Class<?> destination) {
        tvLoadingText.setText(errorMessage);
        Toast.makeText(LoadingActivity.this, errorMessage, Toast.LENGTH_LONG).show();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this, destination);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }

    private void startShop(String username, String userId) {
        Intent intent = new Intent(LoadingActivity.this, ShopActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        intent.putExtra(EXTRA_USER_ID, userId);
        startActivity(intent);
        finish();
    }

    private void saveSession(String username, String userId, String token) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username);
        editor.putString("userId", userId);
        editor.putString("authToken", token);
        editor.apply();
    }

    private void startDefaultLoading() {
        final String[] messages = {
                "> Estableciendo conexion ...",
                "> Accediendo a la base de datos de la EETAC...",
                "> Acceso concedido. Bienvenido."
        };

        for (int i = 0; i < messages.length; i++) {
            final int index = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvLoadingText.setText(messages[index]);
                }
            }, i * 1000L);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startShop("guest", "guest");
            }
        }, 5000);
    }
}
