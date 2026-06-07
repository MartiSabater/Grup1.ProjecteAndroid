package com.example.sigmadsa.viewmodel;

import android.os.Bundle;
import android.util.Log;
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
import com.example.sigmadsa.models.Producto;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InventarioActivity extends AppCompatActivity {

    private LinearLayout llLista;
    private ProgressBar pbLoading;
    private String userId;
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
        userId = getIntent().getStringExtra(LoadingActivity.EXTRA_USER_ID);
        if (userId == null) {
            userId = getIntent().getStringExtra(LoadingActivity.EXTRA_USERNAME);
        }
        if (userId == null) userId = "guest";

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
        apiService.getInventario(userId).enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                pbLoading.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    mostrarItems(response.body());
                } else {
                    Log.e("Inventario", "code=" + response.code() + " error=" + response.errorBody());
                    Toast.makeText(InventarioActivity.this, "Error al cargar inventario (" + response.code() + ")", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {
                pbLoading.setVisibility(View.GONE);
                Toast.makeText(InventarioActivity.this, "Error de conexion: " + (t.getMessage() != null ? t.getMessage() : t.getClass().getSimpleName()), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrarItems(List<Producto> productos) {
        llLista.removeAllViews();
        if (productos.isEmpty()) {
            TextView emptyText = new TextView(this);
            emptyText.setText("No tienes objetos en tu inventario.");
            emptyText.setTextColor(getResources().getColor(R.color.label_gray));
            emptyText.setPadding(0, 50, 0, 0);
            llLista.addView(emptyText);
            return;
        }

        for (final Producto p : productos) {
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
        apiService.eliminarProducto(idProd, userId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(InventarioActivity.this, "Objeto eliminado", Toast.LENGTH_SHORT).show();
                    cargarInventario();
                } else {
                    Toast.makeText(InventarioActivity.this, "Error al eliminar el objeto", Toast.LENGTH_SHORT).show();
                    btn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(InventarioActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                btn.setVisibility(View.GONE);
            }
        });
    }
}
