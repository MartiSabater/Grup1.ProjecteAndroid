package com.example.sigmadsa;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        TextView tvLoadingText = findViewById(R.id.tv_loading_text);
        
        // Simular missatges de terminal durant la càrrega
        final String[] messages = {
            "> Establiment de connexió segura...",
            "> Desxifrant protocols SIGMA...",
            "> Accedint a la base de dades EETAC...",
            "> Verificant integritat del sistema...",
            "> Accés concedit. Benvingut agent."
        };

        final Handler handler = new Handler();
        for (int i = 0; i < messages.length; i++) {
            final int index = i;
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvLoadingText.setText(messages[index]);
                }
            }, i * 1000);
        }

        // Navegar a ShopActivity després de 5 segons
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LoadingActivity.this, ShopActivity.class);
                startActivity(intent);
                finish();
            }
        }, 5000);
    }
}
