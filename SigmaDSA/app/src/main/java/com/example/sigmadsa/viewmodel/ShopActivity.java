package com.example.sigmadsa.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sigmadsa.R;
import com.example.sigmadsa.api.ApiClient;
import com.example.sigmadsa.api.ApiService;
import com.example.sigmadsa.api.BotiguaResponse;
import com.example.sigmadsa.models.Group;
import com.example.sigmadsa.models.Mission;
import com.example.sigmadsa.models.Objective;
import com.example.sigmadsa.models.User;
import com.example.sigmadsa.models.UserGameState;

import java.util.List;
import java.text.Normalizer;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopActivity extends AppCompatActivity {

    private String username;
    private String userId;
    private ApiService apiService;
    private TextView tvUserInfo;
    private TextView tvGameState;
    private ImageView ivCurrentAvatar;
    private LinearLayout llProducts;
    private LinearLayout sectionShop;
    private LinearLayout sectionRanking;
    private LinearLayout sectionMissions;
    private LinearLayout sectionGroups;
    private User currentUser;
    private List<BotiguaResponse> shopProducts;
    private List<Group> groupList;
    private static final String[] AVATARS = {
            "avatar_1", "avatar_2", "avatar_3", "avatar_4",
            "avatar_5", "avatar_6", "avatar_7", "avatar_8",
            "avatar_9", "avatar_10", "avatar_11", "avatar_12"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        apiService = ApiClient.getApiService();
        userId = getIntent().getStringExtra(LoadingActivity.EXTRA_USER_ID);
        username = getIntent().getStringExtra(LoadingActivity.EXTRA_USERNAME);
        if (userId == null) {
            userId = username;
        }

        tvUserInfo = findViewById(R.id.tv_user_info);
        tvGameState = findViewById(R.id.tv_game_state);
        ivCurrentAvatar = findViewById(R.id.iv_current_avatar);
        llProducts = findViewById(R.id.ll_products);
        sectionShop = findViewById(R.id.section_shop);
        sectionRanking = findViewById(R.id.section_ranking);
        sectionMissions = findViewById(R.id.section_missions);
        sectionGroups = findViewById(R.id.section_groups);

        findViewById(R.id.tab_shop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSection("shop");
            }
        });
        findViewById(R.id.tab_ranking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSection("ranking");
                cargarRanking();
            }
        });
        findViewById(R.id.tab_missions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSection("missions");
                cargarMisiones();
            }
        });
        findViewById(R.id.tab_groups).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSection("groups");
                cargarGrupos();
            }
        });

        Button btnSalir = findViewById(R.id.btn_salir);
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        Button btnInventario = findViewById(R.id.btn_inventario_link);
        btnInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopActivity.this, InventarioActivity.class);
                intent.putExtra(LoadingActivity.EXTRA_USER_ID, userId);
                startActivity(intent);
            }
        });

        ivCurrentAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mostrarSelectorAvatar();
            }
        });

        Button btnAssistent = findViewById(R.id.btn_assistent_link);
        btnAssistent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ShopActivity.this, AssistentActivity.class));
            }
        });

        Button btnTeamLink = findViewById(R.id.btn_team_link);
        btnTeamLink.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(ShopActivity.this, TeamActivity.class);
                intent.putExtra(LoadingActivity.EXTRA_USER_ID, userId);
                startActivity(intent);
            }
        });

        showSection("shop");
        refrescarUsuario();
        cargarProductos();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (apiService != null && userId != null) {
            refrescarUsuario();
        }
    }

    private void refrescarUsuario() {
        apiService.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    if (currentUser.getId() != null) {
                        userId = currentUser.getId();
                    }
                    renderUser();
                } else if (response.code() == 404) {
                    Toast.makeText(ShopActivity.this, "Sesion caducada. Inicia sesion otra vez.", Toast.LENGTH_LONG).show();
                    logout();
                } else {
                    tvUserInfo.setText("No se ha podido cargar el usuario (" + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                tvUserInfo.setText("Error de conexion: " + (t.getMessage() != null ? t.getMessage() : t.getClass().getSimpleName()));
            }
        });
    }

    private void renderUser() {
        String name = currentUser.getNombre() != null ? currentUser.getNombre() : currentUser.getId();
        tvUserInfo.setText(name + " - " + currentUser.getEcts() + " ECTS");
        ivCurrentAvatar.setImageResource(avatarDrawable(currentUser.getAvatar()));
        if (shopProducts != null) {
            renderProductos(shopProducts);
        }

        UserGameState state = currentUser.getGameState();
        if (state == null) {
            tvGameState.setVisibility(View.GONE);
            return;
        }

        String mission = state.getCurrentMissionTitle() != null ? state.getCurrentMissionTitle() : "Mision " + state.getCurrentMissionId();
        String objective = state.getCurrentObjectiveTitle() != null ? state.getCurrentObjectiveTitle() : "Objetivo " + state.getCurrentObjectiveId();
        tvGameState.setText("Vida: " + state.getHealth() + " / " + state.getMaxHealth()
                + "\n" + mission
                + "\n" + objective);
        tvGameState.setVisibility(View.VISIBLE);
    }

    private void cargarProductos() {
        llProducts.removeAllViews();
        llProducts.addView(simpleText("Cargando productos...", R.color.label_gray, 14));
        apiService.getProductos().enqueue(new Callback<List<BotiguaResponse>>() {
            @Override
            public void onResponse(Call<List<BotiguaResponse>> call, Response<List<BotiguaResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    renderProductos(response.body());
                } else {
                    llProducts.removeAllViews();
                    llProducts.addView(simpleText("No se han podido cargar los productos (" + response.code() + ").", R.color.label_gray, 14));
                }
            }

            @Override
            public void onFailure(Call<List<BotiguaResponse>> call, Throwable t) {
                llProducts.removeAllViews();
                llProducts.addView(simpleText("Error de red al cargar productos: " + (t.getMessage() != null ? t.getMessage() : t.getClass().getSimpleName()), R.color.label_gray, 14));
            }
        });
    }

    private void renderProductos(List<BotiguaResponse> productos) {
        shopProducts = productos;
        llProducts.removeAllViews();
        if (productos.isEmpty()) {
            llProducts.addView(simpleText("No hay objetos disponibles en la tienda.", R.color.label_gray, 14));
            return;
        }

        for (BotiguaResponse producto : productos) {
            LinearLayout card = card();
            card.setOrientation(LinearLayout.HORIZONTAL);
            card.setGravity(android.view.Gravity.CENTER_VERTICAL);

            // Imagen del producto (buscar drawable por nombre del producto)
            ImageView ivProducto = new ImageView(this);
            LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(dp(56), dp(56));
            imgParams.setMargins(0, 0, dp(12), 0);
            ivProducto.setLayoutParams(imgParams);
            ivProducto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ivProducto.setImageResource(getDrawableForProductName(producto.getNombre()));
            TextView code = simpleText("OBJ-" + producto.getId(), R.color.shop_text_cyan, 10);
            TextView name = simpleText(producto.getNombre(), R.color.white, 16);
            name.setTypeface(null, android.graphics.Typeface.BOLD);
            TextView desc = simpleText(producto.getDescripcion(), R.color.label_gray, 12);
            TextView price = simpleText(producto.getPrecio() + " ECTS", R.color.terminal_green, 13);
            price.setTypeface(null, android.graphics.Typeface.BOLD);
            Button buy = new Button(this);
            buy.setText(canBuy(producto) ? "COMPRAR" : "ECTS insuficientes");
            buy.setEnabled(canBuy(producto));
            buy.setTextColor(getColor(R.color.black));
            buy.setTextSize(10);
            buy.setBackgroundResource(R.drawable.shop_button_buy);
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ejecutarCompra(String.valueOf(producto.getId()));
                }
            });

            // Añadir vistas al card en orden: imagen + textos + botón
            card.addView(ivProducto);

            LinearLayout textCol = new LinearLayout(this);
            textCol.setOrientation(LinearLayout.VERTICAL);
            textCol.addView(code);
            textCol.addView(name);
            textCol.addView(desc);
            textCol.addView(price);

            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            textCol.setLayoutParams(textParams);

            card.addView(textCol);
            card.addView(buy);
            llProducts.addView(card);
        }
    }

    private int getDrawableForProductName(String name) {
        if (name == null) return R.mipmap.ic_launcher_foreground;
        String resName = normalizeResourceName(name);
        int resId = getResources().getIdentifier(resName, "drawable", getPackageName());
        if (resId == 0) resId = getResources().getIdentifier(resName, "mipmap", getPackageName());
        return resId != 0 ? resId : R.mipmap.ic_launcher_foreground;
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

    private boolean canBuy(BotiguaResponse producto) {
        return currentUser != null && currentUser.getEcts() >= producto.getPrecio();
    }

    private void ejecutarCompra(final String idProd) {
        apiService.comprar(idProd, userId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ShopActivity.this, "Objeto adquirido. Inventario sincronizado.", Toast.LENGTH_SHORT).show();
                    refrescarUsuario();
                    cargarProductos();
                } else if (response.code() == 402) {
                    Toast.makeText(ShopActivity.this, "No tienes suficientes ECTS para este objeto.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ShopActivity.this, "No se ha podido completar la compra.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ShopActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarRanking() {
        sectionRanking.removeAllViews();
        sectionRanking.addView(simpleText("Cargando ranking...", R.color.label_gray, 14));
        apiService.getRanking().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                sectionRanking.removeAllViews();
                if (!response.isSuccessful() || response.body() == null) {
                    sectionRanking.addView(simpleText("No se ha podido cargar el ranking.", R.color.label_gray, 14));
                    return;
                }
                List<User> users = response.body();
                if (users.isEmpty()) {
                    sectionRanking.addView(simpleText("Todavia no hay jugadores en el ranking.", R.color.label_gray, 14));
                    return;
                }
                for (int i = 0; i < users.size(); i++) {
                    User user = users.get(i);
                    UserGameState state = user.getGameState();
                    String mission = state != null && state.getCurrentMissionTitle() != null
                            ? state.getCurrentMissionTitle()
                            : "Mision " + (state != null ? state.getCurrentMissionId() : 1);
                    String name = user.getNombre() != null ? user.getNombre() : user.getId();
                    sectionRanking.addView(rankingCard(user, "#" + (i + 1) + "  " + name + "\n" + mission));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                sectionRanking.removeAllViews();
                sectionRanking.addView(simpleText("No se ha podido cargar el ranking.", R.color.label_gray, 14));
            }
        });
    }

    private void cargarMisiones() {
        sectionMissions.removeAllViews();
        sectionMissions.addView(simpleText("Cargando misiones...", R.color.label_gray, 14));
        apiService.getMisiones().enqueue(new Callback<List<Mission>>() {
            @Override
            public void onResponse(Call<List<Mission>> call, Response<List<Mission>> response) {
                sectionMissions.removeAllViews();
                if (!response.isSuccessful() || response.body() == null) {
                    sectionMissions.addView(simpleText("No se han podido cargar las misiones.", R.color.label_gray, 14));
                    return;
                }
                List<Mission> missions = response.body();
                if (missions.isEmpty()) {
                    sectionMissions.addView(simpleText("No hay misiones configuradas todavia.", R.color.label_gray, 14));
                    return;
                }
                for (Mission mission : missions) {
                    sectionMissions.addView(cardText(formatMission(mission)));
                }
            }

            @Override
            public void onFailure(Call<List<Mission>> call, Throwable t) {
                sectionMissions.removeAllViews();
                sectionMissions.addView(simpleText("No se han podido cargar las misiones.", R.color.label_gray, 14));
            }
        });
    }

    private void cargarGrupos() {
        sectionGroups.removeAllViews();
        sectionGroups.addView(simpleText("Cargando grupos...", R.color.label_gray, 14));
        apiService.getGrupos().enqueue(new Callback<List<Group>>() {
            @Override
            public void onResponse(Call<List<Group>> call, Response<List<Group>> response) {
                sectionGroups.removeAllViews();
                if (!response.isSuccessful() || response.body() == null) {
                    sectionGroups.addView(simpleText("No se han podido cargar los grupos.", R.color.label_gray, 14));
                    return;
                }
                groupList = response.body();
                if (groupList.isEmpty()) {
                    sectionGroups.addView(simpleText("No hay grupos disponibles.", R.color.label_gray, 14));
                    return;
                }
                for (Group group : groupList) {
                    sectionGroups.addView(groupCard(group));
                }
            }

            @Override
            public void onFailure(Call<List<Group>> call, Throwable t) {
                sectionGroups.removeAllViews();
                sectionGroups.addView(simpleText("Error de red al cargar grupos: " + (t.getMessage() != null ? t.getMessage() : t.getClass().getSimpleName()), R.color.label_gray, 14));
            }
        });
    }

    private View groupCard(Group group) {
        LinearLayout card = card();
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(android.view.Gravity.CENTER_VERTICAL);

        LinearLayout textCol = new LinearLayout(this);
        textCol.setOrientation(LinearLayout.VERTICAL);
        textCol.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));

        TextView title = simpleText(group.getNombre() != null ? group.getNombre() : "Grupo " + group.getId(), R.color.white, 16);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        TextView desc = simpleText(group.getDescripcion() != null ? group.getDescripcion() : "Sin descripción", R.color.label_gray, 12);
        TextView members = simpleText("Miembros: " + group.getMiembros(), R.color.terminal_green, 12);

        textCol.addView(title);
        textCol.addView(desc);
        textCol.addView(members);

        Button join = new Button(this);
        join.setText(getString(R.string.unirse));
        join.setTextColor(getColor(R.color.black));
        join.setTextSize(10);
        join.setBackgroundResource(R.drawable.shop_button_buy);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                joinGroup(group.getId());
            }
        });

        card.addView(textCol);
        card.addView(join);
        return card;
    }

    private void joinGroup(String groupId) {
        apiService.joinGrupo(groupId, userId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ShopActivity.this, "Te has unido al grupo.", Toast.LENGTH_SHORT).show();
                    cargarGrupos();
                } else {
                    Toast.makeText(ShopActivity.this, "No se ha podido unir al grupo.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ShopActivity.this, "Error de red: " + (t.getMessage() != null ? t.getMessage() : t.getClass().getSimpleName()), Toast.LENGTH_LONG).show();
            }
        });
    }

    private String formatMission(Mission mission) {
        StringBuilder text = new StringBuilder();
        text.append("Mision ").append(mission.getId()).append(": ").append(nullToDash(mission.getTitle()));
        if (mission.getDescription() != null) {
            text.append("\n").append(mission.getDescription());
        }
        List<Objective> objectives = mission.getObjectives();
        if (objectives != null && !objectives.isEmpty()) {
            for (Objective objective : objectives) {
                text.append("\n- ").append(nullToDash(objective.getTitle()));
                if (objective.getReward() > 0) {
                    text.append(" (+").append(objective.getReward()).append(" ECTS)");
                }
            }
        }
        return text.toString();
    }

    private String nullToDash(String value) {
        return value == null ? "-" : value;
    }

    private void showSection(String section) {
        sectionShop.setVisibility("shop".equals(section) ? View.VISIBLE : View.GONE);
        sectionRanking.setVisibility("ranking".equals(section) ? View.VISIBLE : View.GONE);
        sectionMissions.setVisibility("missions".equals(section) ? View.VISIBLE : View.GONE);
        sectionGroups.setVisibility("groups".equals(section) ? View.VISIBLE : View.GONE);
    }

    private LinearLayout card() {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setBackgroundResource(R.drawable.shop_card_bg);
        card.setPadding(dp(12), dp(12), dp(12), dp(12));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, dp(10));
        card.setLayoutParams(params);
        return card;
    }

    private View cardText(String text) {
        LinearLayout card = card();
        TextView view = simpleText(text, R.color.white, 14);
        view.setLineSpacing(0, 1.15f);
        card.addView(view);
        return card;
    }

    private View rankingCard(User user, String text) {
        LinearLayout card = card();
        card.setOrientation(LinearLayout.HORIZONTAL);
        card.setGravity(android.view.Gravity.CENTER_VERTICAL);

        ImageView avatar = new ImageView(this);
        LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(dp(44), dp(44));
        avatarParams.setMargins(0, 0, dp(12), 0);
        avatar.setLayoutParams(avatarParams);
        avatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
        avatar.setImageResource(avatarDrawable(user.getAvatar()));

        TextView view = simpleText(text, R.color.white, 14);
        view.setLineSpacing(0, 1.15f);

        card.addView(avatar);
        card.addView(view);
        return card;
    }

    private void mostrarSelectorAvatar() {
        if (currentUser == null) {
            Toast.makeText(this, "Espera a que cargue el usuario.", Toast.LENGTH_SHORT).show();
            return;
        }

        GridLayout grid = new GridLayout(this);
        grid.setColumnCount(4);
        grid.setPadding(dp(16), dp(16), dp(16), dp(8));

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Cambiar avatar")
                .setView(grid)
                .setNegativeButton("Cancelar", null)
                .create();

        for (String avatar : AVATARS) {
            ImageView option = new ImageView(this);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = dp(64);
            params.height = dp(64);
            params.setMargins(dp(6), dp(6), dp(6), dp(6));
            option.setLayoutParams(params);
            option.setPadding(dp(4), dp(4), dp(4), dp(4));
            option.setScaleType(ImageView.ScaleType.CENTER_CROP);
            option.setImageResource(avatarDrawable(avatar));
            option.setContentDescription(avatar);
            option.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    cambiarAvatar(avatar);
                }
            });
            grid.addView(option);
        }

        dialog.show();
    }

    private void cambiarAvatar(String avatar) {
        apiService.updateAvatar(userId, avatar).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    currentUser = response.body();
                    if (currentUser.getId() != null) {
                        userId = currentUser.getId();
                    }
                    renderUser();
                    Toast.makeText(ShopActivity.this, "Avatar actualizado.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ShopActivity.this, "No se ha podido actualizar el avatar (" + response.code() + ")", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ShopActivity.this, "Error de red: " + (t.getMessage() != null ? t.getMessage() : t.getClass().getSimpleName()), Toast.LENGTH_LONG).show();
            }
        });
    }

    private int avatarDrawable(String avatar) {
        if (avatar == null) {
            return R.drawable.avatar_1;
        }

        int resourceId = getResources().getIdentifier(avatar, "drawable", getPackageName());
        return resourceId != 0 ? resourceId : R.drawable.avatar_1;
    }

    private TextView simpleText(String text, int colorRes, int sizeSp) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextColor(getColor(colorRes));
        view.setTextSize(sizeSp);
        view.setPadding(0, dp(2), 0, dp(6));
        return view;
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    private void logout() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        prefs.edit().remove("username").remove("userId").remove("authToken").apply();
        ApiClient.setAuthToken(null);

        Intent intent = new Intent(ShopActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
