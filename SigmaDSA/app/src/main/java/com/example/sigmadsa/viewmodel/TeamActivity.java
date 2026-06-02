package com.example.sigmadsa.viewmodel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
            // ImageView ivAvatar = itemView.findViewById(R.id.iv_avatar); // Usamos placeholder por ahora

            tvName.setText(member.getName());
            tvPoints.setText(String.valueOf(member.getPoints()));

            llMembersContainer.addView(itemView);
        }
    }
}
