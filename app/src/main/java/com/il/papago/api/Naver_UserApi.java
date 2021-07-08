package com.il.papago.api;

import com.il.papago.model.Naver_UserReq;
import com.il.papago.model.UserReq;
import com.il.papago.model.UserRes;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Naver_UserApi {

    @POST("/api/v1/naver_users")
        //call에 <> 안에 들어갈것은, 네트워크를 통해서 받아온 데이터를 처리할 클래스 , 메소드의 파라미터에는 보낼 데이터를 처리할 클래스

    Call<UserRes> createNaverUser(@Body Naver_UserReq naver_userReq);

    @POST("/api/v1/naver_users/login")

    Call<UserRes> login(@Body Naver_UserReq naver_userReq);


}
