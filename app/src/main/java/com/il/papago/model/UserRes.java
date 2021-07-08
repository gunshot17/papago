package com.il.papago.model;

import com.google.gson.annotations.SerializedName;

public class UserRes {
    //@serializedName 에는 ,실제로 json 으로 받아오는 키 이름을 셋팅
    @SerializedName("success")
    boolean success;

    @SerializedName("token")
    String token;


    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
