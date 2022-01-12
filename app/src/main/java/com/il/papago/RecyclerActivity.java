package com.il.papago;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.il.papago.adapter.RecyclerViewAdapter;
import com.il.papago.data.DatabaseHandler;
import com.il.papago.model.Post;

import java.util.ArrayList;

public class RecyclerActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<Post> postArrayList;

    DatabaseHandler dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
//        actionBar.setDisplayHomeAsUpEnabled(true); //  버튼 만들기

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(RecyclerActivity.this));

        dh = new DatabaseHandler(RecyclerActivity.this);
        postArrayList = dh.getAllPost();


        recyclerViewAdapter = new RecyclerViewAdapter(RecyclerActivity.this, postArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_toolbar, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint(getString(R.string.edittext_search_video));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String search) {

                Toast.makeText(RecyclerActivity.this, "검색 처리됨 : " + search, Toast.LENGTH_SHORT).show();

                dh = new DatabaseHandler(RecyclerActivity.this);
                postArrayList = dh.getSearchedPost(search);

                recyclerViewAdapter = new RecyclerViewAdapter(RecyclerActivity.this, postArrayList);
                recyclerView.setAdapter(recyclerViewAdapter);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String search) {
                return false;
            }

        });
        return true;

    }

}