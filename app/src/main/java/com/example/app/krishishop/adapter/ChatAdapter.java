package com.example.app.krishishop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app.krishishop.Model.Chatsmodal;
import com.example.app.krishishop.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {

    private ArrayList<Chatsmodal> chatsmodalArrayList;
    private Context context;

    public ChatAdapter(ArrayList<Chatsmodal> chatsmodalArrayList, Context context) {
        this.chatsmodalArrayList = chatsmodalArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        // below code is to switch our
        // layout type along with view holder.
        switch (viewType) {
            case 0:
                // below line we are inflating user message layout.
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_holder, parent, false);
                return new UserViewHolder(view);
            case 1:
                // below line we are inflating bot message layout.
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bot_reply, parent, false);
                return new BotViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Chatsmodal modal = chatsmodalArrayList.get(position);
        switch (modal.getSender()) {
            case "user":
                // below line is to set the text to our text view of user layout
                ((UserViewHolder) holder).idTVUser.setText(modal.getMessage());
                break;
            case "bot":
                // below line is to set the text to our text view of bot layout
                ((BotViewHolder) holder).idTVBot.setText(modal.getMessage());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return chatsmodalArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        switch (chatsmodalArrayList.get(position).getSender()) {
            case "user":
                return 0;
            case "bot":
                return 1;
            default:
                return -1;
        }
    }
    public static class UserViewHolder extends RecyclerView.ViewHolder {

        // creating a variable
        // for our text view.
        TextView idTVUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing with id.
            idTVUser = itemView.findViewById(R.id.idTVUser);
        }
    }

    public static class BotViewHolder extends RecyclerView.ViewHolder {

        // creating a variable
        // for our text view.
        TextView idTVBot;

        public BotViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing with id.
            idTVBot = itemView.findViewById(R.id.idTVBot);
        }
    }
}

