package com.il.papago.api;

import com.il.papago.model.Post;
import com.il.papago.model.PostRes;
import com.il.papago.model.UserRes;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostApi { // 선언해주는 것

    @GET("/api/v1/posts/me")
    Call<PostRes> getPosts(@Header("Authorization") String token, @Query("offset") int offset, @Query("limit") int limit);

    @POST("/api/v1/posts")
    Call<UserRes> createPost(@Header("Authorization") String token,
                             @Body Post post);

    @PUT("/api/v1/posts/:post_id")
    Call<UserRes> deletePost(@Header("Authorization") String token,
                             @Path("post_id") int id);


}
