package com.il.papago;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.il.papago.Util.Translated;
import com.il.papago.Util.Transtext;
import com.il.papago.api.NetworkClient;
import com.il.papago.api.PostApi;
import com.il.papago.model.Post;
import com.il.papago.model.UserRes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    EditText translatedkorean;
    EditText translatedthing;
    Spinner spinner;
    Button button;
    int arrayitem;


    private DrawerLayout mDrawerLayout;
    private Context context = this;


    Data d = new Data();



    ArrayList<Transtext> transtextArrayList = new ArrayList<>();
    ArrayList<Translated> translatedArrayList = new ArrayList<>();


    RequestQueue requestQueue;
    String baseUrl = "https://openapi.naver.com/v1/papago/n2mt";

    String clientId = BuildConfig.clientId;
    String clientSecret = BuildConfig.clientSecret;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = findViewById(R.id.spinner);
        translatedkorean = findViewById(R.id.translatedkorean);
        translatedthing = findViewById(R.id.translatedthing);
        button = findViewById(R.id.button);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); //  버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.hamburger);




        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        ArrayAdapter languageAdapter = ArrayAdapter.createFromResource(this, R.array.translate, android.R.layout.simple_spinner_dropdown_item);

        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(languageAdapter);

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

                    Intent i = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(i);
                } else if (id == R.id.setting) {
                    Toast.makeText(context, title + ": 설정 정보를 확인합니다.", Toast.LENGTH_SHORT).show();
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
                                    Log.i("text", Text);


                                    translatedthing.setText(Text);
                                    Transtext transtext = new Transtext(Text);
                                    transtextArrayList.add(transtext);
                                    String aa = transtext.getText();
                                    Log.i("BBB","aa :"+aa);
                                    d.x = aa;




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
                        params.put("source", "ko");


                        if (arrayitem == 1) {
                            params.put("target", "en");
                        } else if (arrayitem == 2) {
                            params.put("target", "ja");
                        } else if (arrayitem == 3) {
                            params.put("target", "zh-TW");
                        } else if (arrayitem == 4) {
                            params.put("target", "zh-CN");
                        }
                        params.put("text", translatedkor);

                        Translated translated = new Translated(translatedkor);
                        translatedArrayList.add(translated);
                        String AA = translated.getTranslatedkor();


                            Log.i("BBB", "list :"+translatedArrayList.size());
                            Log.i("BBB", "AA :"+AA);




                        return params;

                    }


                };

                Data d2 = copy(d);

                Log.i("DDD","translated :"+ d2.x);



                // 실제로 네트워크로 API 호출 ( 요청 )
                requestQueue.add(request);




            }

        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
//     작동할 내용 넣기  ex) 버튼 누르기
            }

        }

        return super.onOptionsItemSelected(item);
    }


    public Data copy(Data d){
        Data tmp = new Data();
        tmp.x = d.x;
        return tmp;

    }



    class Data{
        String x;
    }




    }

