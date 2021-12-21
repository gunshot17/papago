package com.il.papago;

import androidx.appcompat.app.AppCompatActivity;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {

    EditText EditTextEmail;
    EditText EditTextPasswd;
    EditText EditTextPwConfirm;

    Button SignUpBtn;


    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        EditTextEmail = findViewById(R.id.EditTextEmail);
        EditTextPasswd = findViewById(R.id.EditTextPasswd);
        EditTextPwConfirm = findViewById(R.id.EditTextPwConfirm);
        SignUpBtn = findViewById(R.id.SignUpBtn);

        requestQueue = Volley.newRequestQueue(SignUpActivity.this);


        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = EditTextEmail.getText().toString().trim();
                final String passwd1 = EditTextPasswd.getText().toString().trim();
                String passwd2 = EditTextPwConfirm.getText().toString().trim();

                if(email.contains("@") == false){
                    Toast.makeText(SignUpActivity.this, "이메일 형식이 올바르지 않습니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwd1.length() < 4 || passwd1.length() > 12){
                    Toast.makeText(SignUpActivity.this, "비밀번호 길이는 4자리 이상,12자리 이하입니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(passwd1.equalsIgnoreCase(passwd2) == false){
                    Toast.makeText(SignUpActivity.this, "비밀번호가 일치하지 않습니다.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // body 셋팅
                UserReq userReq = new UserReq(email, passwd1);

                // 서버로 이메일과 비밀번호를 전송한다.
                Retrofit retrofit = NetworkClient.getRetrofitClient(SignUpActivity.this);
                UserApi userApi = retrofit.create(UserApi.class);

                Call<UserRes> call = userApi.createUser(userReq);
                call.enqueue(new Callback<UserRes>() {
                    @Override
                    public void onResponse(Call<UserRes> call, Response<UserRes> response) {
                        // 상태코드가 200 인지 확인
                        if(response.isSuccessful()){
                            // response.body() 가 UserRes 이다.
                            boolean success = response.body().isSuccess();
                            String token = response.body().getToken();
                            Log.i("AAA", "success : " + success + ", token : "+token);

                            SharedPreferences sp = getSharedPreferences(Url.PREFERENCES_NAME,
                                    MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("token", token);
                            editor.apply();

                            Intent i = new Intent(SignUpActivity.this, RecyclerActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRes> call, Throwable t) {

                    }
                });





            }
        });


    }
}