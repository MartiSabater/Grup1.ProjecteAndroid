package com.example.sigmadsa.api;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class ApiClient {
    private static final String BASE_URL = "http://192.168.10.92:8080/dsaApp/";
    //private static final String BASE_URL = "http://dsa1.upc.edu/dsaApp/";
    //private static final String BASE_URL = "http://10.0.2.2:8080/dsaApp/";
    private static Retrofit retrofit;
    private static String authToken;

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static void setAuthToken(String token) {
        authToken = token;
    }

    public static ApiService getApiService() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(90, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder();
                        if (authToken != null && !authToken.isEmpty()) {
                            requestBuilder.header("Authorization", "Bearer " + authToken);
                        }
                        return chain.proceed(requestBuilder.build());
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
