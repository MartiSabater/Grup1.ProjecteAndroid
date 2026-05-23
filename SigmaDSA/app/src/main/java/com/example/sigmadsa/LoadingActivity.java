package com.example.sigmadsa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

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
        String username = getIntent().getStringExtra(EXTRA_USERNAME);
        String password = getIntent().getStringExtra(EXTRA_PASSWORD);

        ApiService apiService = ApiClient.getApiService();
        Call<LoginResponse> call = apiService.login(new LoginRequest(username, password));
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    saveSession(username);
                    tvLoadingText.setText("");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startShop(username);
                        }
                    }, 1500);
                } else {
                    String message = "Error: Usuario o contraseña incorrectos";
                    if (response.code() == 404) {
                        message = "Usuario no encontrado";
                    } else if (response.code() == 401) {
                        message = "Contraseña incorrecta";
                    } else if (response.code() >= 500) {
                        message = "Imposible conectarse con el servidor";
                    }
                    showErrorAndReturn(message, LoginActivity.class);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                t.printStackTrace();
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
        Call<RegisterResponse> call = apiService.register(new RegisterRequest(username, email, name, password, avatar));
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    saveSession(username);
                    tvLoadingText.setText("Registro completo. Redirigiendo . . .");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startShop(username);
                        }
                    }, 1500);
                } else {
                    String message = "Error en el registro";
                    if (response.code() == 400) {
                        message = "Datos inválidos o usuario ya existe";
                    } else if (response.code() == 409) {
                        message = "El usuario ya existe";
                    } else if (response.code() >= 500) {
                        message = "Error del servidor. Intenta más tarde";
                    }
                    showErrorAndReturn(message, RegisterActivity.class);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                t.printStackTrace();
                showErrorAndReturn(getConnectionErrorMessage(t), RegisterActivity.class);
            }
        });
    }

    private String getConnectionErrorMessage(Throwable t) {
        if (t instanceof IOException || t instanceof UnknownHostException || t instanceof ConnectException || t instanceof SocketTimeoutException) {
            return "Error.";
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
        }, 2500);
    }

    private void startShop(String username) {
        Intent intent = new Intent(LoadingActivity.this, ShopActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        startActivity(intent);
        finish();
    }

    private void saveSession(String username) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username);
        editor.apply();
    }

    private void startDefaultLoading() {
        final String[] messages = {
            "> Estableciendo conexión ...",
            "> Accediento a la base de datos de la EETAC...",
            "> Acceso concedido. ¡ Bienvenido !."
        };

        for (int i = 0; i < messages.length; i++) {
            final int index = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvLoadingText.setText(messages[index]);
                }
            }, i * 1000);
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startShop("guest");
            }
        }, 5000);
    }
}
