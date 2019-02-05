package com.project.EarthFoundation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;

public class MyTreesActivity extends AppCompatActivity {
    ArrayList<String> treeNamesList = new ArrayList<>();
    ArrayList<String> treeAddressList = new ArrayList<>();
    ArrayList<String> plantDateList = new ArrayList<>();
    ArrayList<String> updatedDateList = new ArrayList<>();
    ArrayList<String> updateStatusList = new ArrayList<>();
    ArrayList<String> treeImageList = new ArrayList<>();
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
