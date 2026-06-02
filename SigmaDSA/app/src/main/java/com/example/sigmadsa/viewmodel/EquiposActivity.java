package com.example.sigmadsa.viewmodel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sigmadsa.R;
import com.example.sigmadsa.api.ApiClient;
import com.example.sigmadsa.api.ApiService;
import com.example.sigmadsa.models.Equipo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EquiposActivity extends AppCompatActivity {

    private LinearLayout llEquiposLista;
    private ProgressBar pbLoading;
    private ApiService apiService;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipos);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        apiService = ApiClient.getApiService();
        userId = getIntent().getStringExtra(LoadingActivity.EXTRA_USER_ID);
        if (userId == null) {
            userId = getIntent().getStringExtra(LoadingActivity.EXTRA_USERNAME);
        }
        if (userId == null) userId = "guest";

        llEquiposLista = findViewById(R.id.ll_equipos_lista);
        pbLoading = findViewById(R.id.pb_equipos_loading);
        Button btnTienda = findViewById(R.id.btn_tienda_equipos);
        btnTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cargarEquipos();
    }

    private void cargarEquipos() {
        pbLoading.setVisibility(View.VISIBLE);
        apiService.getGrupos().enqueue(new Callback<List<Equipo>>() {
            @Override
            public void onResponse(Call<List<Equipo>> call, Response<List<Equipo>> response) {
                pbLoading.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    mostrarEquipos(response.body());
                } else {
                    Toast.makeText(EquiposActivity.this, "Error al cargar equipos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Equipo>> call, Throwable t) {
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(EquiposActivity.this, "Error de conexion", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarEquipos(List<Equipo> equipos) {
        llEquiposLista.removeAllViews();
        if (equipos.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("> No hay equipos disponibles.");
            emptyText.setTextColor(getResources().getColor(R.color.label_gray));
            emptyText.setPadding(0, 50, 0, 0);
            llEquiposLista.addView(emptyText);
            return;
        }

        for (final Equipo equipo : equipos) {
            View itemView = getLayoutInflater().inflate(R.layout.team_item_simple, llEquiposLista, false);
            TextView tvId = itemView.findViewById(R.id.tv_equipo_id);
            TextView tvNombre = itemView.findViewById(R.id.tv_equipo_nombre);
            TextView tvDescripcion = itemView.findViewById(R.id.tv_equipo_descripcion);
            Button btnUnirse = itemView.findViewById(R.id.btn_unirse_equipo);

            tvId.setText("TEAM-" + equipo.getId());
            tvNombre.setText(equipo.getNombre());
            tvDescripcion.setText(equipo.getDescripcion());
            btnUnirse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    unirseAGrupo(equipo.getId(), btnUnirse);
                }
            });

            llEquiposLista.addView(itemView);
        }
    }

    private void unirseAGrupo(final String idGrupo, final Button btnUnirse) {
        btnUnirse.setEnabled(false);
        btnUnirse.setText("...");
        apiService.unirseAGrupo(idGrupo, userId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    btnUnirse.setText("UNIDO");
                    btnUnirse.setBackgroundResource(R.drawable.team_button_joined);
                    btnUnirse.setTextColor(getResources().getColor(R.color.white));
                    Toast.makeText(EquiposActivity.this, "Solicitud enviada al equipo " + idGrupo, Toast.LENGTH_SHORT).show();
                } else {
                    btnUnirse.setEnabled(true);
                    btnUnirse.setText("UNIRSE");
                    Toast.makeText(EquiposActivity.this, "Error al unirse al equipo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                btnUnirse.setEnabled(true);
                btnUnirse.setText("UNIRSE");
                Toast.makeText(EquiposActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
