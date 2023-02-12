package com.example.app.krishishop.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.krishishop.Model.AllHistory;
import com.example.app.krishishop.R;

import java.util.List;

public class AllHistoryAdapter extends RecyclerView.Adapter<AllHistoryAdapter.ViewHolder> {

    List<AllHistory> allHistoryList;
    Context context;

    public AllHistoryAdapter(List<AllHistory> allHistoryList, Context context) {
        this.allHistoryList = allHistoryList;
        this.context = context;
    }

    @NonNull
    @Override
    public AllHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.total_history,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllHistoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.pName.setText(allHistoryList.get(position).getProductName());
        holder.pQuantity.setText(allHistoryList.get(position).getProductQuantity());
        holder.pDate.setText(allHistoryList.get(position).getPurchaseDate());
        holder.UId.setText(allHistoryList.get(position).getUserID());
    }

    @Override
    public int getItemCount() {
        return allHistoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView pName,pQuantity,pDate,UId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pName = itemView.findViewById(R.id.pName);
            pQuantity = itemView.findViewById(R.id.pQuantity);
            pDate = itemView.findViewById(R.id.pDate);
            UId = itemView.findViewById(R.id.UId);
        }
    }
}
