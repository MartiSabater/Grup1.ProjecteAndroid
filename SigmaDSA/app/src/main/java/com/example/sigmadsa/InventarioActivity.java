package com.example.sigmadsa;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sigmadsa.api.ApiClient;
import com.example.sigmadsa.api.ApiService;
import com.example.sigmadsa.api.BotiguaResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventarioActivity extends AppCompatActivity {

    private LinearLayout llLista;
    private ProgressBar pbLoading;
    private String username;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        llLista = findViewById(R.id.ll_inventario_lista);
        pbLoading = findViewById(R.id.pb_loading);
        Button btnTienda = findViewById(R.id.btn_tienda);

        apiService = ApiClient.getApiService();
        username = getIntent().getStringExtra(LoadingActivity.EXTRA_USERNAME);
        if (username == null) username = "guest";

        btnTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Volver a la tienda
            }
        });

        cargarInventario();
    }

    private void cargarInventario() {
        pbLoading.setVisibility(View.VISIBLE);
        apiService.getProductos().enqueue(new Callback<List<BotiguaResponse>>() {
            @Override
            public void onResponse(Call<List<BotiguaResponse>> call, Response<List<BotiguaResponse>> response) {
                pbLoading.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    mostrarItems(response.body());
                } else {
                    Toast.makeText(InventarioActivity.this, "Error al carregar productes", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BotiguaResponse>> call, Throwable t) {
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(InventarioActivity.this, "Error de xarxa", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarItems(List<BotiguaResponse> productos) {
        llLista.removeAllViews();
        if (productos.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("No tienes objetos en tu inventario.");
            emptyText.setTextColor(getResources().getColor(R.color.label_gray));
            emptyText.setPadding(0, 50, 0, 0);
            llLista.addView(emptyText);
            return;
        }

        for (final BotiguaResponse p : productos) {
            View itemView = getLayoutInflater().inflate(R.layout.shop_item_simple, null);
            TextView tvNombre = itemView.findViewById(R.id.tv_item_nombre);
            final Button btnEliminar = itemView.findViewById(R.id.btn_eliminar);
            
            tvNombre.setText("> " + p.getNombre());
            
            // Configurar el "Long Press" para mostrar el botón de eliminar
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    btnEliminar.setVisibility(View.VISIBLE);
                    return true;
                }
            });

            // Configurar el click en el botón eliminar
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eliminarDelInventario(String.valueOf(p.getId()), btnEliminar);
                }
            });

            llLista.addView(itemView);
        }
    }

    private void eliminarDelInventario(final String idProd, final View btn) {
        apiService.eliminarProducto(idProd, username).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(InventarioActivity.this, "Objecte eliminat", Toast.LENGTH_SHORT).show();
                    cargarInventario(); // Recarregar la llista
                } else {
                    Toast.makeText(InventarioActivity.this, "Error en eliminar l'objecte", Toast.LENGTH_SHORT).show();
                    btn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(InventarioActivity.this, "Error de xarxa", Toast.LENGTH_SHORT).show();
                btn.setVisibility(View.GONE);
            }
        });
    }
}
