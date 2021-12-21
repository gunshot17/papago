package com.il.papago;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.il.papago.util.Url;
import com.il.papago.api.NetworkClient;
import com.il.papago.api.UserApi;
import com.il.papago.model.UserReq;
import com.il.papago.model.UserRes;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    public static OAuthLoginButton mOAuthLoginButton;
    public static OAuthLogin mOAuthLoginInstance;
    public static Context mContext;

    String OAUTH_CLIENT_ID =BuildConfig.OAUTH_CLIENT_ID;
    String OAUTH_CLIENT_SECRET =BuildConfig.OAUTH_CLIENT_SECRET;
    String OAUTH_CLIENT_NAME =BuildConfig.OAUTH_CLIENT_NAME;

    RequestQueue requestQueue;

    EditText userid;
    EditText passwd;

    Button LoginButton;
    Button SignUpButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userid=findViewById(R.id.editEmail);
        passwd=findViewById(R.id.editPasswd);
        LoginButton=findViewById(R.id.LoginButton);
        SignUpButton=findViewById(R.id.SignUpButton);



        requestQueue = Volley.newRequestQueue(LoginActivity.this);

        mContext = LoginActivity.this;

        //초기화
        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.showDevelopersLog(true);
        mOAuthLoginInstance.init(mContext,OAUTH_CLIENT_ID,OAUTH_CLIENT_SECRET,OAUTH_CLIENT_NAME);


        //로그인 버튼
        mOAuthLoginButton = findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userid.getText().toString().trim();
                String password = passwd.getText().toString().trim();

                if(email.contains("@")== false){
                    Toast.makeText(LoginActivity.this,"이메일 형식이 올바르지 않습니다.",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length()<4 || password.length()>12){
                    Toast.makeText(LoginActivity.this,"비밀번호는 4자리 이상 12자리 이하입니다.",Toast.LENGTH_SHORT).show();
                    return;
                }

                //body
                UserReq userReq = new UserReq(email,password);
                //네트워크로 데이터 처리
                Retrofit retrofit = NetworkClient.getRetrofitClient(LoginActivity.this);
                UserApi userApi = retrofit.create(UserApi.class);

                Call<UserRes> call = userApi.login(userReq);

                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        String token = response.body().getToken();

                        SharedPreferences sp = getSharedPreferences(Url.PREFERENCES_NAME,MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("token",token);
                        editor.apply();

                        Intent i = new Intent(LoginActivity.this,RecyclerActivity.class);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {

                    }
                });





            }
        });

        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,SignUpActivity.class);
                startActivity(i);
                finish();
            }
        });




    }

     OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if(success){
                String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
                String tokenType = mOAuthLoginInstance.getTokenType(mContext);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String header = "Bearer " +  accessToken;
                        Map<String, String> requestHeaders = new HashMap<>();
                        requestHeaders.put("Authorization", header);
                        String apiURL = "https://openapi.naver.com/v1/nid/me"; //엑세스 토큰으로 유저정보를 받아올 주소
                        String responseBody = get(apiURL,requestHeaders);
                        Log.d("responseBody 확인 ",responseBody); //주소로 얻은 유저정보
                        NaverUserInfo(responseBody);


                    }
                }).start();


            }else {
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                Toast.makeText(mContext,"errorCode:" + errorCode + ", errorDesc:" + errorDesc,Toast.LENGTH_SHORT).show();

            }
        }
    };

    private void NaverUserInfo(String msg){
        JSONParser jsonParser = new JSONParser();



        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(msg);
            String resultcode = jsonObject.get("resultcode").toString();
            Log.d("resultcode 확인 ",resultcode);

            String message = jsonObject.get("message").toString();
            Log.d("message 확인 ",message);

            if(resultcode.equals("00")){
                if(message.equals("success")){
                    JSONObject naverJson = (JSONObject) jsonObject.get("response");

                    String id = naverJson.get("id").toString();
                    String nickName = naverJson.get("nickname").toString();
                    String profileImage = naverJson.get("profile_image").toString();
                    String email = naverJson.get("email").toString();



                    Log.d("id 확인 ",id);
                    Log.d("nickName 확인 ",nickName);
                    Log.d("profileImage 확인 ",profileImage);
                    Log.d("email 확인 ",email);

//
//                    Naver_UserReq naver_userReq = new Naver_UserReq(id,email,nickName);
//
//                    Retrofit retrofit = NetworkClient.getRetrofitClient(LoginActivity.this);
//                    Naver_UserApi naver_userApi = retrofit.create(Naver_UserApi.class);
//
//                    Call<UserRes> call = naver_userApi.createNaverUser(naver_userReq);
//                                 call.enqueue(new Callback<UserRes>() {
//                                     @Override
//                                     public void onResponse(Call<UserRes> call, Response<UserRes> response) {
//                                         if (response.isSuccessful()) {
//
//                                             boolean success = response.body().isSuccess();
//                                             Log.i("AAA","success :" + success);
//
//
//                                             Intent i = new Intent(LoginActivity.this,RecyclerActivity.class);
//                                             startActivity(i);
//                                             finish();
//
//                                         }
//                                     }
//
//                                     @Override
//                                     public void onFailure(Call<UserRes> call, Throwable t) {
//
//                                     }
//                                 });
                }
            }
            else{
                //Toast.makeText(getApplicationContext(),"로그인 오류가 발생했습니다.",Toast.LENGTH_SHORT).show();
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private static String get(String apiUrl, Map<String, String> requestHeaders){
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for(Map.Entry<String, String> header :requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }


            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
                return readBody(con.getInputStream());
            } else { // 에러 발생
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }


    private static HttpURLConnection connect(String apiUrl){
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection)url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }


    private static String readBody(InputStream body){
        InputStreamReader streamReader = new InputStreamReader(body);


        try (BufferedReader lineReader = new BufferedReader(streamReader)) {
            StringBuilder responseBody = new StringBuilder();


            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }


            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }
}
