package com.example.app.krishishop;


import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.app.krishishop.Model.Chatsmodal;
import com.example.app.krishishop.adapter.ChatAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatBotActivity extends AppCompatActivity {

    private RecyclerView chatsRV;
    private Button sendMsgIB;
    private EditText userMsgEdt;
    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";

    private RequestQueue mRequestQueue;

    private ArrayList<Chatsmodal> messageModalArrayList;
    private ChatAdapter chatAdapter;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        chatsRV = findViewById(R.id.idRVChats);
        sendMsgIB = findViewById(R.id.idIBSend);
        userMsgEdt = findViewById(R.id.idEdtMessage);

        mRequestQueue = Volley.newRequestQueue(ChatBotActivity.this);
        mRequestQueue.getCache().clear();

        messageModalArrayList = new ArrayList<>();

        sendMsgIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userMsgEdt.getText().toString().isEmpty()) {
                    // if the edit text is empty display a toast message.
                    Toast.makeText(ChatBotActivity.this, "Please enter your message..", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendMessage(userMsgEdt.getText().toString());
                userMsgEdt.setText("");

            }
        });
        // on below line we are initialing our adapter class and passing our array list to it.
        chatAdapter = new ChatAdapter(messageModalArrayList, this);

        // below line we are creating a variable for our linear layout manager.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatBotActivity.this, RecyclerView.VERTICAL, false);

        // below line is to set layout
        // manager to our recycler view.
        chatsRV.setLayoutManager(linearLayoutManager);

        // below line we are setting
        // adapter to our recycler view.
        chatsRV.setAdapter(chatAdapter);
    }

    private void sendMessage(String userMsg){
        messageModalArrayList.add(new Chatsmodal(userMsg, USER_KEY));
        chatAdapter.notifyDataSetChanged();

        String url = "http://api.brainshop.ai/get?bid=162720&key=j0c0Qt1bll56b1hz&uid=[uid]&msg=" + userMsg;

        RequestQueue queue = Volley.newRequestQueue(ChatBotActivity.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    // in on response method we are extracting data
                    // from json response and adding this response to our array list.
                    String botResponse = response.getString("cnt");
                    messageModalArrayList.add(new Chatsmodal(botResponse, BOT_KEY));

                    // notifying our adapter as data changed.
                    chatAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();

                    // handling error response from bot.
                    messageModalArrayList.add(new Chatsmodal("No response", BOT_KEY));
                    chatAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error handling.
                messageModalArrayList.add(new Chatsmodal("Sorry no response found", BOT_KEY));
                Toast.makeText(ChatBotActivity.this, "No response from the bot..", Toast.LENGTH_SHORT).show();
            }
        });

        // at last adding json object
        // request to our queue.
        queue.add(jsonObjectRequest);

    }
}