package com.il.papago;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText Password;
    EditText Address;
    Button Kakao;
    Button Naver;
    Button Google;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Password = findViewById(R.id.Password);
        Address = findViewById(R.id.Address);
        Kakao = findViewById(R.id.Kakao);
        Naver = findViewById(R.id.Naver);
        Google = findViewById(R.id.Google);


        Kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Naver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}