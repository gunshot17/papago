package com.il.papago;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText translatedkorean;
    EditText translatedtext;
    Spinner spinner;
    Button button;

    RequestQueue requestQueue;
    String baseUrl = "https://openapi.naver.com/v1/papago/n2mt";

    String clientId = "7LR2DglLDUWXdP3x0iVX";
    String clientSecret = "Pg9qw9moy4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner);
        translatedkorean=findViewById(R.id.translatedkorean);
        translatedtext=findViewById(R.id.translatedtext);
        button=findViewById(R.id.button);

        requestQueue = Volley.newRequestQueue(MainActivity.this);

        ArrayAdapter monthAdapter = ArrayAdapter.createFromResource(this, R.array.translate, android.R.layout.simple_spinner_dropdown_item);

        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(monthAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            } //이 오버라이드 메소드에서 position은 몇번째 값이 클릭됬는지 알 수 있습니다.
            //getItemAtPosition(position)를 통해서 해당 값을 받아올수있습니다.

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    public void getNetworkData(String url){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("AAA", response.toString());
                try {
                    JSONArray message = response.getJSONArray("message");
                    for(int i = 0; i<message.length();i++){
                        JSONObject jsonObject = message.getJSONObject(i);
                        JSONObject result = jsonObject.getJSONObject("result");
                        String translatedText = result.getString("translatedText");

                        Transtext transtext = new Transtext(translatedText);

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("AAA", error.toString());
                    }
                }
        ){
            // 네이버 API의 헤더 셋팅 부분을 여기에 작성한다.
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                params.put("X-Naver-Client-Id", clientId);
                params.put("X-Naver-Client-Secret", clientSecret);
                return params;
            }
            // 네이버에 요청할 파라미터를 셋팅한다.
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("source", "ko");
                params.put("target", "en");
                params.put("text", "안녕하세요? 만나서 반갑습니다. 잘 지내시죠?");
                return params;
            }
        };

        // 실제로 네트워크로 API 호출 ( 요청 )
        requestQueue.add(jsonObjectRequest);
    }

    }
