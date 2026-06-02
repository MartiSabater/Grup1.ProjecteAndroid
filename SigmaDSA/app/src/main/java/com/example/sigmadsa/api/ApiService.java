package com.example.sigmadsa.api;

import com.example.sigmadsa.models.Producto;
import com.example.sigmadsa.models.Equipo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest body);

    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest body);

    @POST("tienda/comprar/{idProd}/{idUser}")
    Call<Void> comprar(@Path("idProd") String idProd, @Path("idUser") String idUser);

    @GET("tienda/inventario/{idUser}")
    Call<List<Producto>> getInventario(@Path("idUser") String idUser);

    @GET("auth/usuarios/{idUser}/ects")
    Call<ECTSResponse> getUserEcts(@Path("idUser") String idUser);

    @GET("tienda/productos")
    Call<List<BotiguaResponse>> getProductos();

    @DELETE("tienda/inventario/{idProd}/{idUser}")
    Call<Void> eliminarProducto(@Path("idProd") String idProd, @Path("idUser") String idUser);

    @GET("grupos")
    Call<List<Equipo>> getGrupos();

    @POST("grupos/{idGrupo}/usuarios/{idUser}")
    Call<Void> unirseAGrupo(@Path("idGrupo") String idGrupo, @Path("idUser") String idUser);
}
