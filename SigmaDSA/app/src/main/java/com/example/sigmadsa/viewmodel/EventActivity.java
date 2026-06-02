package com.example.sigmadsa.viewmodel;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sigmadsa.R;
import com.example.sigmadsa.api.ApiClient;
import com.example.sigmadsa.api.ApiService;
import com.example.sigmadsa.api.EventResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventActivity extends AppCompatActivity {

    private LinearLayout llEvents;
    private ProgressBar pbLoading;
    private TextView tvEmpty;
    private ApiService apiService;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        llEvents = findViewById(R.id.ll_events_container);
        pbLoading = findViewById(R.id.pb_events_loading);
        tvEmpty = findViewById(R.id.tv_events_empty);

        userId = getIntent().getStringExtra(LoadingActivity.EXTRA_USER_ID);
        if (userId == null) {
            userId = getIntent().getStringExtra(LoadingActivity.EXTRA_USERNAME);
        }
        if (userId == null) {
            userId = "guest";
        }

        apiService = ApiClient.getApiService();

        cargarEventos();
    }

    private void cargarEventos() {
        pbLoading.setVisibility(View.VISIBLE);
        tvEmpty.setVisibility(View.GONE);
        llEvents.removeAllViews();

        apiService.getEventos().enqueue(new Callback<List<EventResponse>>() {
            @Override
            public void onResponse(Call<List<EventResponse>> call, Response<List<EventResponse>> response) {
                pbLoading.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    mostrarEventos(response.body());
                } else {
                    Log.e("EventActivity", "Error al cargar eventos: " + response.code());
                    Toast.makeText(EventActivity.this, getString(R.string.error_cargar_eventos), Toast.LENGTH_LONG).show();
                    tvEmpty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<EventResponse>> call, Throwable t) {
                pbLoading.setVisibility(View.GONE);
                Log.e("EventActivity", "onFailure", t);
                Toast.makeText(EventActivity.this, getString(R.string.error_cargar_eventos), Toast.LENGTH_LONG).show();
                tvEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    private void mostrarEventos(List<EventResponse> eventos) {
        llEvents.removeAllViews();
        if (eventos.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            return;
        }

        tvEmpty.setVisibility(View.GONE);
        for (final EventResponse evento : eventos) {
            View itemView = getLayoutInflater().inflate(R.layout.event_item, llEvents, false);
            ImageView ivEventImage = itemView.findViewById(R.id.iv_event_image);
            TextView tvNombre = itemView.findViewById(R.id.tv_event_name);
            TextView tvDesc = itemView.findViewById(R.id.tv_event_desc);
            TextView tvDates = itemView.findViewById(R.id.tv_event_dates);
            Button btnRegister = itemView.findViewById(R.id.btn_register_event);

            String imagenUrl = evento.getImagen();
            if (imagenUrl != null && !imagenUrl.isEmpty()) {
                Picasso.get()
                        .load(imagenUrl)
                        .placeholder(android.R.drawable.ic_menu_my_calendar)
                        .error(android.R.drawable.ic_menu_report_image)
                        .fit()
                        .centerCrop()
                        .into(ivEventImage);
            } else {
                ivEventImage.setImageResource(android.R.drawable.ic_menu_my_calendar);
            }

            tvNombre.setText(evento.getNombre() != null ? evento.getNombre() : "Evento");
            tvDesc.setText(evento.getDescripcion() != null ? evento.getDescripcion() : "Sin descripción disponible.");
            String fechas = "Inicio: " + (evento.getFechaInicio() != null ? evento.getFechaInicio() : "?")
                    + " - Fin: " + (evento.getFechaFin() != null ? evento.getFechaFin() : "?");
            tvDates.setText(fechas);

            btnRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inscribirEvento(evento.getId(), btnRegister);
                }
            });

            llEvents.addView(itemView);
        }
    }

    private void inscribirEvento(final String eventId, final Button btnRegister) {
        apiService.inscribirEvento(eventId, userId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EventActivity.this, getString(R.string.evento_inscrito), Toast.LENGTH_SHORT).show();
                    btnRegister.setText("Inscrito");
                    btnRegister.setEnabled(false);
                } else {
                    Toast.makeText(EventActivity.this, getString(R.string.error_inscribirse_evento), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("EventActivity", "inscribirEvento onFailure", t);
                Toast.makeText(EventActivity.this, getString(R.string.error_inscribirse_evento), Toast.LENGTH_LONG).show();
            }
        });
    }
}
