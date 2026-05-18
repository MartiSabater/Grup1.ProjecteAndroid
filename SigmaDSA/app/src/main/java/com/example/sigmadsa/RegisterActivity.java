package com.example.sigmadsa;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

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
                // Lógica de registro aquí
                android.content.Intent intent = new android.content.Intent(RegisterActivity.this, LoadingActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
