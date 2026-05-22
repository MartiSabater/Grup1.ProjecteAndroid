package com.example.sigmadsa;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sigmadsa.api.ApiClient;
import com.example.sigmadsa.api.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.example.sigmadsa.api.BotiguaResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopActivity extends AppCompatActivity {

    private String username;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Inicializamos el servicio de API
        apiService = ApiClient.getApiService();
        
        // Obtenemos el username pasado desde LoadingActivity
        username = getIntent().getStringExtra(LoadingActivity.EXTRA_USERNAME);
        if (username == null) {
            username = "guest";
        }

        cargarProductos();

        Button btnSalir = findViewById(R.id.btn_salir);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Configurar botón para ir al inventario
        Button btnInventario = findViewById(R.id.btn_inventario_link);
        btnInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.content.Intent intent = new android.content.Intent(ShopActivity.this, InventarioActivity.class);
                intent.putExtra(LoadingActivity.EXTRA_USERNAME, username);
                startActivity(intent);
            }
        });
    }

    private void cargarProductos() {
        apiService.getProductos().enqueue(new Callback<List<BotiguaResponse>>() {
            @Override
            public void onResponse(Call<List<BotiguaResponse>> call, Response<List<BotiguaResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    actualizarUI(response.body());
                } else {
                    Toast.makeText(ShopActivity.this, "Error al cargar productos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<BotiguaResponse>> call, Throwable t) {
                Toast.makeText(ShopActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void actualizarUI(List<BotiguaResponse> productos) {
        // IDs de los elementos en el XML para los 6 productos
        int[][] resIds = {
            {R.id.item1_id, R.id.item1_name, R.id.item1_desc, R.id.item1_price, R.id.btn1_buy},
            {R.id.item2_id, R.id.item2_name, R.id.item2_desc, R.id.item2_price, R.id.btn2_buy},
            {R.id.item3_id, R.id.item3_name, R.id.item3_desc, R.id.item3_price, R.id.btn3_buy},
            {R.id.item4_id, R.id.item4_name, R.id.item4_desc, R.id.item4_price, R.id.btn4_buy},
            {R.id.item5_id, R.id.item5_name, R.id.item5_desc, R.id.item5_price, R.id.btn5_buy},
            {R.id.item6_id, R.id.item6_name, R.id.item6_desc, R.id.item6_price, R.id.btn6_buy}
        };

        for (int i = 0; i < resIds.length; i++) {
            // Buscamos el contenedor del item (el padre del ID) para ocultarlo si no hay producto
            View itemContainer = (View) findViewById(resIds[i][0]).getParent();
            
            if (i < productos.size()) {
                final BotiguaResponse p = productos.get(i);
                
                TextView tvId = findViewById(resIds[i][0]);
                TextView tvNombre = findViewById(resIds[i][1]);
                TextView tvDesc = findViewById(resIds[i][2]);
                TextView tvPrecio = findViewById(resIds[i][3]);
                Button btnComprar = findViewById(resIds[i][4]);

                tvId.setText("OBJ-" + p.getId());
                tvNombre.setText(p.getNombre());
                tvDesc.setText(p.getDescripcion());
                tvPrecio.setText(p.getPrecio() + " ECTS");

                btnComprar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ejecutarCompra(String.valueOf(p.getId()));
                    }
                });
                
                itemContainer.setVisibility(View.VISIBLE);
            } else {
                itemContainer.setVisibility(View.GONE);
            }
        }
    }

    private void ejecutarCompra(final String idProd) {
        // Realizamos la petición POST: /tienda/comprar/{idProd}/{idUser}
        Call<Void> call = apiService.comprar(idProd, username);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ShopActivity.this, "Compra confirmada: " + idProd, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShopActivity.this, "Error: Saldo insuficiente o problema en el servidor", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ShopActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
