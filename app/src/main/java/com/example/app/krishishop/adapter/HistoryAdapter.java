package com.example.app.krishishop.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.krishishop.Model.HistoryModel;
import com.example.app.krishishop.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    List<HistoryModel> historyModelList;

    public HistoryAdapter(List<HistoryModel> historyModelList) {
        this.historyModelList = historyModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.history_products,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.pName.setText(historyModelList.get(position).getProductName());
        holder.pQuantity.setText(historyModelList.get(position).getProductQuantity());
        holder.pDate.setText(historyModelList.get(position).getPurchaseDate());
    }

    @Override
    public int getItemCount() {
        return historyModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView pName,pQuantity,pDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pName = itemView.findViewById(R.id.pName);
            pQuantity = itemView.findViewById(R.id.pQuantity);
            pDate = itemView.findViewById(R.id.pDate);

        }
    }
}
