package com.example.sigmadsa.viewmodel;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.text.Normalizer;
import java.util.Locale;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.io.InputStream;
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
            TextView tvDescripcion = itemView.findViewById(R.id.tv_item_descripcion);
            ImageView ivImagen = itemView.findViewById(R.id.iv_item_image);
            final Button btnEliminar = itemView.findViewById(R.id.btn_eliminar);

            tvNombre.setText("> " + p.getNombre());
            tvDescripcion.setText(p.getDescripcion() != null ? p.getDescripcion() : "");

            // Intentar cargar recurso local si `imagen` viene con el nombre del drawable/mipmap,
            // y como fallback intentar cargar desde assets/objetos/ (por ejemplo: assets/objetos/espada_icon.png)
            String img = (p.getImagen() != null && !p.getImagen().isEmpty()) ? p.getImagen() : null;
            if (img == null && p.getNombre() != null) {
                img = normalizeResourceName(p.getNombre());
            }
            if (img != null && !img.isEmpty()) {
                int resId = getResources().getIdentifier(img, "drawable", getPackageName());
                if (resId == 0) resId = getResources().getIdentifier(img, "mipmap", getPackageName());
                if (resId != 0) {
                    ivImagen.setImageResource(resId);
                } else {
                    // Fallback: buscar en assets/objetos/
                    AssetManager am = getAssets();
                    InputStream is = null;
                    try {
                        String assetPath = img;
                        // Si el nombre no contiene carpeta ni extensión, buscar en objetos/<name>.png primero
                        if (!assetPath.contains("/") && !assetPath.contains(".")) {
                            String tryPng = "objetos/" + assetPath + ".png";
                            try {
                                is = am.open(tryPng);
                            } catch (Exception e) {
                                String tryWebp = "objetos/" + assetPath + ".webp";
                                is = am.open(tryWebp);
                            }
                        } else {
                            // Ruta relativa proporcionada, ábrela tal cual
                            is = am.open(assetPath);
                        }

                        if (is != null) {
                            Bitmap bmp = BitmapFactory.decodeStream(is);
                            ivImagen.setImageBitmap(bmp);
                            is.close();
                        }
                    } catch (Exception ignored) {
                        // No encontrar la imagen no rompe la UI; se mantendrá el placeholder.
                        try {
                            if (is != null) is.close();
                        } catch (Exception ex) {
                        }
                    }
                }
            }
            
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

    private String normalizeResourceName(String s) {
        if (s == null) return "";
        String normalized = Normalizer.normalize(s, Normalizer.Form.NFD);
        normalized = normalized.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        normalized = normalized.toLowerCase(Locale.ROOT);
        normalized = normalized.replaceAll("[^a-z0-9]+", "_");
        normalized = normalized.replaceAll("^_+|_+$", "");
        return normalized;
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
