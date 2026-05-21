package com.example.sigmadsa;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        final EditText etUsuario = findViewById(R.id.et_usuario);
        final EditText etPassword = findViewById(R.id.et_password);
        Button btnEntrar = findViewById(R.id.btn_entrar);
        Button btnRegistroTab = findViewById(R.id.btn_registro_tab);

        btnRegistroTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Intent intent = new android.content.Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsuario.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor, ingresa usuario y contraseña", Toast.LENGTH_LONG).show();
                    return;
                }

                android.content.Intent intent = new android.content.Intent(LoginActivity.this, LoadingActivity.class);
                intent.putExtra(LoadingActivity.EXTRA_ACTION, LoadingActivity.ACTION_LOGIN);
                intent.putExtra(LoadingActivity.EXTRA_USERNAME, username);
                intent.putExtra(LoadingActivity.EXTRA_PASSWORD, password);
                startActivity(intent);
                finish();
            }
        });
    }
}