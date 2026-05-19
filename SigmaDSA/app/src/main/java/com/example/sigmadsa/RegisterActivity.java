package com.example.sigmadsa;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsuario, etCorreo, etNombre, etPassword, etConfirmPassword;
    private TextView tvCorreoError, tvPasswordError;
    private boolean isPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    private void validatePasswords() {
        String pass = etPassword.getText().toString();
        String confirmPass = etConfirmPassword.getText().toString();

        if (confirmPass.isEmpty()) {
            tvPasswordError.setVisibility(View.GONE);
        } else if (!pass.equals(confirmPass)) {
            tvPasswordError.setVisibility(View.VISIBLE);
        } else {
            tvPasswordError.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        etUsuario = findViewById(R.id.et_usuario);
        etCorreo = findViewById(R.id.et_correo);
        etNombre = findViewById(R.id.et_nombre);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        tvCorreoError = findViewById(R.id.tv_correo_error);
        tvPasswordError = findViewById(R.id.tv_password_error);

        TextWatcher passwordWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePasswords();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        etPassword.addTextChangedListener(passwordWatcher);
        etConfirmPassword.addTextChangedListener(passwordWatcher);

        TextView tvShowPassword = findViewById(R.id.tv_show_password);
        TextView tvShowConfirmPassword = findViewById(R.id.tv_show_confirm_password);

        tvShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPasswordVisible = !isPasswordVisible;
                if (isPasswordVisible) {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    tvShowPassword.setText("👁️");
                } else {
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    tvShowPassword.setText("🙈");
                }
                etPassword.setSelection(etPassword.getText().length());
            }
        });

        tvShowConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isConfirmPasswordVisible = !isConfirmPasswordVisible;
                if (isConfirmPasswordVisible) {
                    etConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    tvShowConfirmPassword.setText("👁️");
                } else {
                    etConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    tvShowConfirmPassword.setText("🙈");
                }
                etConfirmPassword.setSelection(etConfirmPassword.getText().length());
            }
        });

        // Inicialmente ocultamos el error de correo si queremos que solo aparezca al validar
        // tvCorreoError.setVisibility(View.GONE);

        Button btnLoginTab = findViewById(R.id.btn_login_tab);
        btnLoginTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Regresa al Login
            }
        });

        Button btnCrearExpediente = findViewById(R.id.btn_crear_expediente);

        btnCrearExpediente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass = etPassword.getText().toString();
                String confirmPass = etConfirmPassword.getText().toString();

                if (!pass.equals(confirmPass)) {
                    tvPasswordError.setVisibility(View.VISIBLE);
                } else {
                    tvPasswordError.setVisibility(View.GONE);
                    // Lógica de registro aquí
                    android.content.Intent intent = new android.content.Intent(RegisterActivity.this, LoadingActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
