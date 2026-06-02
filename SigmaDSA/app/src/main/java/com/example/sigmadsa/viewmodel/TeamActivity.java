package com.example.sigmadsa.viewmodel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sigmadsa.R;
import com.example.sigmadsa.api.ApiClient;
import com.example.sigmadsa.api.ApiService;
import com.example.sigmadsa.api.TeamMemberResponse;
import com.example.sigmadsa.api.TeamResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TeamActivity extends AppCompatActivity {

    private String userId;
    private ApiService apiService;
    private TextView tvTeamName;
    private LinearLayout llMembersContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        userId = getIntent().getStringExtra(LoadingActivity.EXTRA_USER_ID);
        apiService = ApiClient.getApiService();

        tvTeamName = findViewById(R.id.tv_team_name);
        llMembersContainer = findViewById(R.id.ll_members_container);
        Button btnTienda = findViewById(R.id.btn_back_shop);

        btnTienda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Volver a la tienda
            }
        });


        cargarEquipo();
    }

    private void cargarEquipo() {
        if (userId == null) {
            Toast.makeText(this, "Error: Usuario no identificado", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.getUserTeam(userId).enqueue(new Callback<TeamResponse>() {
            @Override
            public void onResponse(Call<TeamResponse> call, Response<TeamResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TeamResponse teamData = response.body();
                    tvTeamName.setText(teamData.getTeam());
                    mostrarMiembros(teamData.getMembers());
                } else {
                    Toast.makeText(TeamActivity.this, "Error al cargar el equipo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TeamResponse> call, Throwable t) {
                Toast.makeText(TeamActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mostrarMiembros(List<TeamMemberResponse> members) {
        llMembersContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(this);

        if (members == null) return;

        for (TeamMemberResponse member : members) {
            View itemView = inflater.inflate(R.layout.team_member_item, llMembersContainer, false);

            TextView tvName = itemView.findViewById(R.id.tv_player_name);
            TextView tvPoints = itemView.findViewById(R.id.tv_player_points);
            ImageView ivAvatar = itemView.findViewById(R.id.iv_avatar);

            tvName.setText(member.getName());
            tvPoints.setText(String.valueOf(member.getPoints()));
            ivAvatar.setImageResource(getAvatarResource(member.getAvatar()));

            llMembersContainer.addView(itemView);
        }
    }

    private int getAvatarResource(String avatar) {
        if (avatar == null || avatar.trim().isEmpty()) {
            return R.drawable.avatar_12;
        }

        String normalizedAvatar = avatar.toLowerCase().trim();

        if (normalizedAvatar.contains("avatar_1.") || normalizedAvatar.equals("avatar_1") || normalizedAvatar.equals("1")) {
            return R.drawable.avatar_1;
        } else if (normalizedAvatar.contains("avatar_2.") || normalizedAvatar.equals("avatar_2") || normalizedAvatar.equals("2")) {
            return R.drawable.avatar_2;
        } else if (normalizedAvatar.contains("avatar_3.") || normalizedAvatar.equals("avatar_3") || normalizedAvatar.equals("3")) {
            return R.drawable.avatar_3;
        } else if (normalizedAvatar.contains("avatar_4.") || normalizedAvatar.equals("avatar_4") || normalizedAvatar.equals("4")) {
            return R.drawable.avatar_4;
        } else if (normalizedAvatar.contains("avatar_5.") || normalizedAvatar.equals("avatar_5") || normalizedAvatar.equals("5")) {
            return R.drawable.avatar_5;
        } else if (normalizedAvatar.contains("avatar_6.") || normalizedAvatar.equals("avatar_6") || normalizedAvatar.equals("6")) {
            return R.drawable.avatar_6;
        } else if (normalizedAvatar.contains("avatar_7.") || normalizedAvatar.equals("avatar_7") || normalizedAvatar.equals("7")) {
            return R.drawable.avatar_7;
        } else if (normalizedAvatar.contains("avatar_8.") || normalizedAvatar.equals("avatar_8") || normalizedAvatar.equals("8")) {
            return R.drawable.avatar_8;
        } else if (normalizedAvatar.contains("avatar_9.") || normalizedAvatar.equals("avatar_9") || normalizedAvatar.equals("9")) {
            return R.drawable.avatar_9;
        } else if (normalizedAvatar.contains("avatar_10.") || normalizedAvatar.equals("avatar_10") || normalizedAvatar.equals("10")) {
            return R.drawable.avatar_10;
        } else if (normalizedAvatar.contains("avatar_11.") || normalizedAvatar.equals("avatar_11") || normalizedAvatar.equals("11")) {
            return R.drawable.avatar_11;
        } else if (normalizedAvatar.contains("avatar_12.") || normalizedAvatar.equals("avatar_12") || normalizedAvatar.equals("12")) {
            return R.drawable.avatar_12;
        }

        return R.drawable.avatar_12;
    }
}
