package com.example.sigmadsa.api;

import com.google.gson.annotations.SerializedName;

public class ComprarRequest {
    @SerializedName("idUser")
    private String idUser;

    @SerializedName("idProducto")
    private String idProducto;

    public ComprarRequest() {}

    public ComprarRequest(String idUser, String idProducto) {
        this.idUser = idUser;
        this.idProducto = idProducto;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }
}
