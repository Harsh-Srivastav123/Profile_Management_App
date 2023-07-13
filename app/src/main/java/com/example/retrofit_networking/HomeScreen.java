package com.example.retrofit_networking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeScreen extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton fb;
    List<Post_model> list;
    ImageView error;
    int x=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        recyclerView=findViewById(R.id.recycle);
        fb=findViewById(R.id.floatingActionButton);
        error=findViewById(R.id.errorimg);
        error.setVisibility(View.GONE);


        Intent intent=getIntent();

        x=intent.getIntExtra("pointer",-1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Call<List<Post_model>> call= ApiController.getInstance().getApi().getdata();
        call.enqueue(new Callback<List<Post_model>>() {
            @Override
            public void onResponse(Call<List<Post_model>> call, Response<List<Post_model>> response) {
              if(response.body()!=null){
                  list=response.body();
                  ArrayList<Post_model> arrayList=new ArrayList<Post_model>(list);

                  MyAdapter myAdapter=new MyAdapter(HomeScreen.this,arrayList);
                  recyclerView.setAdapter(myAdapter);
                  if(x==-1){
                      recyclerView.scrollToPosition(0);
                  }
                  else{
                      recyclerView.scrollToPosition(x);
                  }
              }
              else {
                error.setVisibility(View.VISIBLE);
              }
            }

            @Override
            public void onFailure(Call<List<Post_model>> call, Throwable t) {
               error.setVisibility(View.VISIBLE);
                Toast.makeText(HomeScreen.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeScreen.this,MainActivity.class);
                startActivity(intent);
            }
        });

    }
}