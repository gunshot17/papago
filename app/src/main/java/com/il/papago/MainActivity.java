package com.il.papago;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.il.papago.data.DatabaseHandler;

import com.il.papago.dataBinding.Bio;
import com.il.papago.databinding.NaviHeaderBinding;
import com.il.papago.model.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    //databinding
    private NaviHeaderBinding binding;
    Bio bio = new Bio();

    EditText translatedkorean;
    EditText translatedthing;
    Spinner spinner;
    Spinner spinner2;
    Button button;
    int arrayitem;
    int arrayitem2;


    Post post;
    DatabaseHandler dh;

    private DrawerLayout mDrawerLayout;
    private Context context = this;




    RequestQueue requestQueue;
    String baseUrl = "https://openapi.naver.com/v1/papago/n2mt";
    String clientId = BuildConfig.clientId;
    String clientSecret = BuildConfig.clientSecret;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //dataBinding    여기에 조건문 달아서 해야됨 null object 뜸
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        spinner = findViewById(R.id.spinner);
        translatedkorean = findViewById(R.id.translatedkorean);
        translatedthing = findViewById(R.id.translatedthing);
        button = findViewById(R.id.button);
        spinner2 = findViewById(R.id.spinner2);


//dataBinding

        bio.setName("손님");
        bio.setEmail(" ");
        binding.setBio(bio);


        dh = new DatabaseHandler(MainActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowCustomEnabled(true); // 커스텀 가능하게 라는데 작동 확인해야됨 12.03
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); //  버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.hamburger);



        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ArrayAdapter languageAdapter = ArrayAdapter.createFromResource(this, R.array.translate, android.R.layout.simple_spinner_dropdown_item);
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(languageAdapter);

        ArrayAdapter languageAdapter2 = ArrayAdapter.createFromResource(this,R.array.translate2, android.R.layout.simple_spinner_dropdown_item);
        languageAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(languageAdapter2);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();
                if (id == R.id.login) {
                    Toast.makeText(context, title + ": 로그인 화면으로 이동합니다.", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(i);
                } else if (id == R.id.setting) {
                    Toast.makeText(context, title + ": 설정 정보를 확인합니다.", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(i);
                } else if (id == R.id.logout) {
                    Toast.makeText(context, title + ": 로그아웃 시도중", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                arrayitem = ((int) parent.getItemIdAtPosition(position));
                Log.i("AAA", " " + arrayitem);
            } //이 오버라이드 메소드에서 position은 몇번째 값이 클릭됬는지 알 수 있습니다.

            //getItemAtPosition(position)를 통해서 해당 값을 받아올수있습니다.
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                arrayitem2 = ((int) parent.getItemIdAtPosition(position));
                Log.i("AAA", " " + arrayitem2);
            } //이 오버라이드 메소드에서 position은 몇번째 값이 클릭됬는지 알 수 있습니다.

            //getItemAtPosition(position)를 통해서 해당 값을 받아올수있습니다.
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                requestQueue = Volley.newRequestQueue(MainActivity.this);
                StringRequest request = new StringRequest(
                        Request.Method.POST,
                        baseUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("AAA", response);
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONObject message = jsonObject.getJSONObject("message");
                                    JSONObject result = message.getJSONObject("result");
                                    String Text = result.getString("translatedText");
                                    Log.i("DDD", Text);


                                    translatedthing.setText(Text);


                                    SharedPreferences tet = getSharedPreferences("origintet", MODE_PRIVATE);
                                    String origintext = tet.getString("origintet", null);


                                    Log.i("DDD", "text :" + origintext);


                                    post = new Post(origintext, Text);
                                    dh.addPost(post);


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
                ) {// 네이버 API의 헤더 셋팅 부분을 여기에 작성한다.
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
                        String translatedkor = translatedkorean.getText().toString();

                        if ( arrayitem == 0 && arrayitem2 == 1 ) {
                            params.put("source", "ko");
                            params.put("target", "en");
                        } else if (arrayitem == 0 && arrayitem2 == 2) {
                            params.put("source", "ko");
                            params.put("target", "ja");
                        } else if (arrayitem == 0 && arrayitem2 == 3) {
                            params.put("source", "ko");
                            params.put("target", "zh-TW");
                        } else if (arrayitem == 0 && arrayitem2 == 4) {
                            params.put("source", "ko");
                            params.put("target", "zh-CN");
                        } else if (arrayitem == 1 && arrayitem2 == 0){
                            params.put("source", "en");
                            params.put("target", "ko");
                        }else if (arrayitem == 1 && arrayitem2 == 2){
                            params.put("source", "en");
                            params.put("target", "ja");
                        }else if (arrayitem == 1 && arrayitem2 == 3){
                            params.put("source", "en");
                            params.put("target", "zh-TW");
                        }else if (arrayitem == 1 && arrayitem2 == 4){
                            params.put("source", "en");
                            params.put("target", "zh-CN");
                        }else if (arrayitem == 2 && arrayitem2 == 0){
                            params.put("source", "ja");
                            params.put("target", "ko");
                        }else if (arrayitem == 2 && arrayitem2 == 1){
                            params.put("source", "ja");
                            params.put("target", "en");
                        }else if (arrayitem == 2 && arrayitem2 == 3){
                            params.put("source", "ja");
                            params.put("target", "zh-TW");
                        }else if (arrayitem == 2 && arrayitem2 == 4){
                            params.put("source", "ja");
                            params.put("target", "zh-CN");
                        }else if (arrayitem == 3 && arrayitem2 == 0){
                            params.put("source", "zh-TW");
                            params.put("target", "ko");
                        }else if (arrayitem == 3 && arrayitem2 == 1){
                            params.put("source", "zh-TW");
                            params.put("target", "en");
                        }else if (arrayitem == 3 && arrayitem2 == 2){
                            params.put("source", "zh-TW");
                            params.put("target", "ja");
                        }else if (arrayitem == 3 && arrayitem2 == 4){
                            params.put("source", "zh-TW");
                            params.put("target", "zh-CN");
                        }else if (arrayitem == 4 && arrayitem2 == 0){
                            params.put("source", "zh-CN");
                            params.put("target", "ko");
                        }else if (arrayitem == 4 && arrayitem2 == 1){
                            params.put("source", "zh-CN");
                            params.put("target", "en");
                        }else if (arrayitem == 4 && arrayitem2 == 2){
                            params.put("source", "zh-CN");
                            params.put("target", "ja");
                        }else if (arrayitem == 4 && arrayitem2 == 3){
                            params.put("source", "zh-CN");
                            params.put("target", "zh-TW");
                        }


                        params.put("text", translatedkor);


                        SharedPreferences tet = getSharedPreferences("origintet", MODE_PRIVATE);
                        SharedPreferences.Editor editor = tet.edit();
                        editor.putString("origintet", translatedkor);
                        editor.apply();


                        return params;
                    }

                };


                // 실제로 네트워크로 API 호출 ( 요청 )
                requestQueue.add(request);


            }


        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
//     작동할 내용 넣기  ex) 버튼 누르기
            }
            case R.id.toolbar_memo: {
                Intent i = new Intent(MainActivity.this, RecyclerActivity.class);
                startActivity(i);
            }

        }

        return super.onOptionsItemSelected(item);
    }
}
