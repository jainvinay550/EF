package com.project.EarthFoundation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MyTreesActivity extends AppCompatActivity {
    ArrayList<String> treeNamesList = new ArrayList<>();
    ArrayList<String> treeAddressList = new ArrayList<>();
    ArrayList<String> plantDateList = new ArrayList<>();
    ArrayList<String> updatedDateList = new ArrayList<>();
    ArrayList<String> updateStatusList = new ArrayList<>();
    ArrayList<String> treeImageList = new ArrayList<>();

    Button _plantTree;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_trees);
        Intent intent = getIntent();
        treeNamesList = intent.getStringArrayListExtra("treeNamesList");
        treeAddressList = intent.getStringArrayListExtra("treeAddressList");
        plantDateList = intent.getStringArrayListExtra("plantDateList");
        updatedDateList = intent.getStringArrayListExtra("updatedDateList");
        updateStatusList = intent.getStringArrayListExtra("updateStatusList");
        treeImageList = intent.getStringArrayListExtra("treeImageList");

        // get the reference of RecyclerView
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        // set a LinearLayoutManager with default vertical orientation
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        if (!treeNamesList.isEmpty()) {
            CustomAdapter customAdapter = new CustomAdapter(MyTreesActivity.this, treeNamesList, treeAddressList, plantDateList, updatedDateList, updateStatusList, treeImageList);
            recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
        }
        else{
            CoordinatorLayout layout =(CoordinatorLayout)findViewById(R.id.background);
            layout.setBackgroundResource(R.drawable.treebackground);
            _plantTree=(Button) findViewById(R.id.mytrees_planttree);
            _plantTree.setVisibility(View.VISIBLE);
            _plantTree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(getApplicationContext(),PlantTreeActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    @Override
    public void onBackPressed() {
        // Disable going back to the previous activity
        finish();
    }
}
