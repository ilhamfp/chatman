package com.chatman.activity;

import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chatman.R;

public class ChatRoomActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView chatRv;
    private EditText chatEt;
    private ImageButton chatSend;
    private TextView toolbarName;

    private String chatRoomId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        toolbar = findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(ContextCompat
                        .getColor(this, android.R.color.black));
            }
            else {
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(ContextCompat
                        .getColor(this, R.color.white));
            }

        }
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        chatRv = findViewById(R.id.chat_rv);
        chatEt = findViewById(R.id.chat_et);
        chatSend = findViewById(R.id.chat_send);
        toolbarName = findViewById(R.id.toolbar_name);
        if (savedInstanceState != null) {

        }
        else {
            Intent intent = getIntent();
            toolbarName.setText(intent.getStringExtra("name"));
            chatRoomId = intent.getStringExtra("chatRoomId");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
