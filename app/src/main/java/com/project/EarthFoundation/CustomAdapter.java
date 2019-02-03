package com.project.EarthFoundation;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    ArrayList<String> treeNameList;
    ArrayList<String> treeAddressList;
    ArrayList<String> plantDateList;
    ArrayList<String> updatedDateList;
    ArrayList<String> updateStatusList;
    ArrayList<String> treeImageList;

    Context context;

    public CustomAdapter(Context context, ArrayList<String> treeNameList, ArrayList<String> treeAddressList, ArrayList<String> plantDateList, ArrayList<String> updatedDateList,ArrayList<String> updateStatusList,ArrayList<String> treeImageList) {
        this.context = context;
        this.treeNameList = treeNameList;
        this.treeAddressList = treeAddressList;
        this.plantDateList = plantDateList;
        this.updatedDateList = updatedDateList;
        this.updateStatusList=updateStatusList;
        this.treeImageList=treeImageList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        MyViewHolder vh = new MyViewHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // set the data in items
        if(updateStatusList.get(position).equals("yes")){
            holder.updateStatus.setVisibility(View.VISIBLE);
        }else if(updateStatusList.get(position).equals("no")){
            holder.updateStatus.setVisibility(View.GONE);
        }
        holder.treeName.setText(treeNameList.get(position));
        holder.treeAddress.setText(treeAddressList.get(position));
        holder.plantDate.setText(plantDateList.get(position));
        new ImageLoadTask(treeImageList.get(position), holder.treeImage).execute();

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // display a toast with person name on item click
                Intent intent = new Intent(context.getApplicationContext(), UpdateTreeDetails.class);
                intent.putExtra("treeName",treeNameList.get(position));
                intent.putExtra("treeAddress",treeAddressList.get(position));
                intent.putExtra("plantDate",plantDateList.get(position));
                intent.putExtra("updatedDate",updatedDateList.get(position));
                intent.putExtra("updateStatus",updateStatusList.get(position));
                intent.putExtra("treeImage",treeImageList.get(position));
             context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return treeNameList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView treeName, treeAddress, plantDate, updateStatus;// init the item view's
        ImageView treeImage;
        public MyViewHolder(View itemView) {
            super(itemView);

            // get the reference of item view's
            updateStatus = (TextView) itemView.findViewById(R.id.update_error);
            treeName = (TextView) itemView.findViewById(R.id.tree_Name);
            treeAddress = (TextView) itemView.findViewById(R.id.tree_Address);
            plantDate = (TextView) itemView.findViewById(R.id.plant_Date);
            treeImage = (ImageView) itemView.findViewById(R.id.tree_image);

        }
    }
}
