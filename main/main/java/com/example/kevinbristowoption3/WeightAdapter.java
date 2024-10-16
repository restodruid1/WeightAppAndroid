package com.example.kevinbristowoption3;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WeightAdapter extends RecyclerView.Adapter<WeightAdapter.ViewHolder> {
    private ArrayList<WeightInput> weightList;
    private MyDatabase database;



    public WeightAdapter(ArrayList<WeightInput> weightList, MyDatabase databaseHelper) {
        this.weightList = weightList;
        this.database = databaseHelper;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WeightInput weightRecord = weightList.get(position);
        holder.dateText.setText(weightRecord.getDate());
        holder.weightText.setText(String.valueOf(weightRecord.getWeight()));    //needs to be int not string?

        // Handle delete
        holder.deleteButton.setOnClickListener(v -> {
            database.deleteData(MainActivity.usr, weightRecord.getWeight());
            weightList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position,this.getItemCount());
        });
        //Log.d("POSITION DELETE", String.valueOf(position));

    }

    @Override
    public int getItemCount() {
        //Log.d("ADAPTER", String.valueOf(weightList.size()));
        return weightList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dateText, weightText;
        public Button deleteButton;

        public ViewHolder(View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dateText);
            weightText = itemView.findViewById(R.id.weightText);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}


