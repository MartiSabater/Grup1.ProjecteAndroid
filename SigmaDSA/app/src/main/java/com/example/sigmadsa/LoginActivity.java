package com.example.sigmadsa;

import android.os.Bundle;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }



        Button btnEntrar = findViewById(R.id.btn_entrar);
        Button btnRegistroTab = findViewById(R.id.btn_registro_tab);
        TextView tvStatus = findViewById(R.id.tv_status);

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
                tvStatus.setText("Validando acceso...");
                tvStatus.setVisibility(View.VISIBLE);
                
                // Navegar a la pantalla de carga después de un pequeño retraso
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        android.content.Intent intent = new android.content.Intent(LoginActivity.this, LoadingActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 1000);
            }
        });
    }
}