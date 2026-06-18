package com.example.sigmadsa.viewmodel;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sigmadsa.R;
import com.example.sigmadsa.api.ApiClient;
import com.example.sigmadsa.api.ApiService;
import com.example.sigmadsa.api.AssistentRequest;
import com.example.sigmadsa.api.AssistentResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AssistentActivity extends AppCompatActivity {
    private static final String TAG = "SigmaAssistant";

    private EditText etQuestion;
    private TextView tvAnswer;
    private Button btnAsk;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        apiService = ApiClient.getApiService();
        etQuestion = findViewById(R.id.et_assistent_question);
        tvAnswer = findViewById(R.id.tv_assistent_answer);
        btnAsk = findViewById(R.id.btn_assistent_ask);

        Button btnBack = findViewById(R.id.btn_assistent_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askAssistent();
            }
        });
    }

    private void askAssistent() {
        String question = etQuestion.getText().toString().trim();
        if (question.isEmpty()) {
            Toast.makeText(this, "Escribe una pregunta", Toast.LENGTH_SHORT).show();
            return;
        }

        btnAsk.setEnabled(false);
        tvAnswer.setText("Consultando asistente...");

        Call<AssistentResponse> request = apiService.askAssistent(new AssistentRequest(question));
        Log.d(TAG, "Enviando pregunta al asistente");
        Log.d(TAG, "Backend base URL: " + ApiClient.getBaseUrl());
        Log.d(TAG, "Endpoint completo: " + request.request().url());
        Log.d(TAG, "Pregunta: " + question);

        request.enqueue(new Callback<AssistentResponse>() {
            @Override
            public void onResponse(Call<AssistentResponse> call, Response<AssistentResponse> response) {
                btnAsk.setEnabled(true);
                Log.d(TAG, "Respuesta HTTP del asistente: " + response.code());

                if (response.isSuccessful() && response.body() != null) {
                    String answer = response.body().getAnswer();
                    if (answer != null && !answer.trim().isEmpty()) {
                        Log.d(TAG, "Respuesta IA recibida: " + answer);
                        tvAnswer.setText(answer);
                        return;
                    }
                    Log.w(TAG, "Respuesta HTTP correcta, pero el body no contiene answer/response/message");
                } else {
                    Log.e(TAG, "Error HTTP del asistente: " + response.code() + " " + response.message());
                    if (response.errorBody() != null) {
                        try {
                            Log.e(TAG, "Cuerpo del error: " + response.errorBody().string());
                        } catch (IOException e) {
                            Log.e(TAG, "No se pudo leer el cuerpo del error", e);
                        }
                    }
                }

                tvAnswer.setText("No se pudo obtener respuesta del asistente.");
                Toast.makeText(AssistentActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<AssistentResponse> call, Throwable t) {
                btnAsk.setEnabled(true);
                Log.e(TAG, "Fallo de red llamando al asistente: " + call.request().url(), t);
                tvAnswer.setText("No se puede conectar con el servidor.");
                Toast.makeText(AssistentActivity.this, "Error de red: " + (t.getMessage() != null ? t.getMessage() : t.getClass().getSimpleName()), Toast.LENGTH_LONG).show();
            }
        });
    }
}
