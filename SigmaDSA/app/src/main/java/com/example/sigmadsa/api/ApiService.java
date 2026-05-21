package com.example.sigmadsa.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest body);

    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest body);
}
