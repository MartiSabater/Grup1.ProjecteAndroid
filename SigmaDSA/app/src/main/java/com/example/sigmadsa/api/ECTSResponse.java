package com.example.sigmadsa.api;

import com.google.gson.annotations.SerializedName;

public class ECTSResponse {

    @SerializedName("idUser")
    private String idUser;

    @SerializedName("ects")
    private int ects;
    public ECTSResponse() {}

    public ECTSResponse(String idUser, int ects) {
        this.idUser = idUser;
        this.ects = ects;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getEcts() {
        return ects;
    }
}
