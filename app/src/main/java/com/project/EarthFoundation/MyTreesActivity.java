package com.project.EarthFoundation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MyTreesActivity extends Fragment {
    private static final String ARG_COLOR = "color";
    private int color;
    ArrayList<String> treeIdList = new ArrayList<>();
    ArrayList<String> treeNamesList = new ArrayList<>();
    ArrayList<String> treeAddressList = new ArrayList<>();
    ArrayList<String> plantDateList = new ArrayList<>();
    ArrayList<String> updatedDateList = new ArrayList<>();
    ArrayList<String> updateStatusList = new ArrayList<>();
    ArrayList<String> treeImageList = new ArrayList<>();
    ArrayList<String> treeRelationList = new ArrayList<>();
    ArrayList<String> treeCountList = new ArrayList<>();

    RecyclerView recyclerView;
    CoordinatorLayout layout;
    TextView _plantTree;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            color = getArguments().getInt(ARG_COLOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.activity_my_trees, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        layout =(CoordinatorLayout) rootView.findViewById(R.id.background);
        _plantTree=(TextView) rootView.findViewById(R.id.mytrees_planttree);

        layout.setBackgroundColor(getLighterColor(color));

        Intent intent = getActivity().getIntent();
        treeIdList = intent.getStringArrayListExtra("treeIdList");
        treeNamesList = intent.getStringArrayListExtra("treeNamesList");
        treeAddressList = intent.getStringArrayListExtra("treeAddressList");
        plantDateList = intent.getStringArrayListExtra("plantDateList");
        updatedDateList = intent.getStringArrayListExtra("updatedDateList");
        updateStatusList = intent.getStringArrayListExtra("updateStatusList");
        treeImageList = intent.getStringArrayListExtra("treeImageList");
        treeRelationList = intent.getStringArrayListExtra("treeRelationList");
        treeCountList = intent.getStringArrayListExtra("treeCountList");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        if (!treeNamesList.isEmpty()) {
            CustomAdapter customAdapter = new CustomAdapter(getContext(), treeNamesList, treeAddressList, plantDateList, updatedDateList, updateStatusList, treeImageList,treeIdList,treeRelationList,treeCountList);
            recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
        }
        else{
            layout.setBackgroundResource(R.drawable.treebackground);
            _plantTree.setVisibility(View.VISIBLE);
//            _plantTree.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent=new Intent(getContext(),PlantTreeActivity.class);
//                    startActivity(intent);
//                    //getActivity().finish();
//                }
//            });
        }

        return rootView;
    }

    private int getLighterColor(int color) {
        return Color.argb(30,
                Color.red(color),
                Color.green(color),
                Color.blue(color)
        );
    }

}
