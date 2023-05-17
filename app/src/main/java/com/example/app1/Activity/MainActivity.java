package com.example.app1.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app1.Adaptor.CategoryAdaptor;
import com.example.app1.Adaptor.PopularAdaptor;
import com.example.app1.BusinessActivity.MainBusiness;
import com.example.app1.Domain.CategoryDomain;
import com.example.app1.Domain.PopularDomain;
import com.example.app1.Domain.Store;
import com.example.app1.R;
import com.example.app1.UserActivity.GetTheMap;
import com.example.app1.UserActivity.UserProfileActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SelectListener{
    private RecyclerView.Adapter adapter, adapter2;
    private RecyclerView recyclerViewCategoryList;
    private RecyclerView recyclerViewPopularList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView profilebtn = findViewById(R.id.customerprofile);
        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this, GetTheMap.class);
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        TextView textbtn = findViewById(R.id.textView3);
        textbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(MainActivity.this, MainBusiness.class);
                startActivity(intent2);
            }
        });

        recyclerviewCategory();
        recyclerViewPopular();
    }

    private void recyclerviewCategory(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategoryList = findViewById(R.id.recyclerView);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

        ArrayList<CategoryDomain> category = new ArrayList<>();
        category.add(new CategoryDomain("Cafe", "cafe"));
        category.add(new CategoryDomain("Brunch", "brunch"));
        category.add(new CategoryDomain("Restaurant", "restaurant"));
        category.add(new CategoryDomain("Bar", "bar"));

        adapter = new CategoryAdaptor(category,this);
        recyclerViewCategoryList.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(CategoryDomain myModel) {
        //edw epilegei ti ginetai meta to click
        Toast.makeText(this,myModel.getTitle(),Toast.LENGTH_SHORT).show();
    }


    private void recyclerViewPopular(){
        LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewPopularList = findViewById(R.id.recyclerView2);
        recyclerViewPopularList.setLayoutManager(verticalLayoutManager);

        ArrayList<PopularDomain> popularlist = new ArrayList<>();
        popularlist.add(new PopularDomain("bar", "Magazi", "bar", 3.71, 87, 0.77, true));
        popularlist.add(new PopularDomain("cafe", "Magaziiiiiiiiii", "cafe", 4.71, 117, 0.77, true));
        popularlist.add(new PopularDomain("bar", "Magazi", "bar", 3.71, 87, 0.77, true));
        popularlist.add(new PopularDomain("cafe", "Magaziiiiiiiiii", "cafe", 4.71, 117, 0.77, true));
        popularlist.add(new PopularDomain("bar", "Magazi", "bar", 3.71, 87, 0.77, true));
        popularlist.add(new PopularDomain("cafe", "Magaziiiiiiiiii", "cafe", 4.71, 117, 0.77, true));

        //to popular domain tha periexei stores mesa



        adapter2 = new PopularAdaptor(popularlist, this);
        recyclerViewPopularList.setAdapter(adapter2);
    }
}