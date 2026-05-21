package com.example.sigmadsa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sigmadsa.api.ApiClient;
import com.example.sigmadsa.api.ApiService;
import com.example.sigmadsa.api.LoginRequest;
import com.example.sigmadsa.api.LoginResponse;
import com.example.sigmadsa.api.RegisterRequest;
import com.example.sigmadsa.api.RegisterResponse;

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
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    tvLoadingText.setText("");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startShop();
                        }
                    }, 1500);
                } else {
                    String message = "Usuario no encontrado";
                    if (response.body() != null && response.body().getMessage() != null) {
                        message = response.body().getMessage();
                    }
                    showErrorAndReturn(message, LoginActivity.class);
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                showErrorAndReturn("Fallo de connexión: " + t.getMessage(), LoginActivity.class);
            }
        });
    }

    private void executeRegister() {
        String username = getIntent().getStringExtra(EXTRA_USERNAME);
        String email = getIntent().getStringExtra(EXTRA_EMAIL);
        String name = getIntent().getStringExtra(EXTRA_NAME);
        String password = getIntent().getStringExtra(EXTRA_PASSWORD);

        ApiService apiService = ApiClient.getApiService();
        Call<RegisterResponse> call = apiService.register(new RegisterRequest(username, email, name, password));
        call.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    tvLoadingText.setText("> Registre complet. Redirigint...");
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startShop();
                        }
                    }, 1500);
                } else {
                    String message = "Error de registre.";
                    if (response.body() != null && response.body().getMessage() != null) {
                        message = response.body().getMessage();
                    }
                    showErrorAndReturn(message, RegisterActivity.class);
                }
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                showErrorAndReturn("Fallo de connexió: " + t.getMessage(), RegisterActivity.class);
            }
        });
    }

    private void showErrorAndReturn(String errorMessage, Class<?> destination) {
        tvLoadingText.setText(errorMessage);
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

    private void startShop() {
        Intent intent = new Intent(LoadingActivity.this, ShopActivity.class);
        startActivity(intent);
        finish();
    }

    private void startDefaultLoading() {
        final String[] messages = {
            "> Establiment de connexió segura...",
            "> Desxifrant protocols SIGMA...",
            "> Accedint a la base de dades EETAC...",
            "> Verificant integritat del sistema...",
            "> Accés concedit. Benvingut agent."
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
                startShop();
            }
        }, 5000);
    }
}
