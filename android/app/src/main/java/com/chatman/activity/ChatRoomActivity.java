package com.chatman.activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.chatman.R;
import com.chatman.adapter.ChatAdapter;
import com.chatman.helper.FirebaseHelper;
import com.chatman.helper.PreferencesHelper;
import com.chatman.model.Chat;
import com.chatman.model.ChatRoom;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    private static final String TAG = ChatRoomActivity.class.getSimpleName();

    private Toolbar toolbar;
    private RecyclerView chatRv;
    private ChatAdapter adapter;
    private EditText chatEt;
    private ImageButton chatSend;
    private TextView toolbarName;
    private List<Chat> chatList;
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

        chatList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);
        chatRv.setLayoutManager(linearLayoutManager);
        adapter = new ChatAdapter(chatList);
        chatRv.setAdapter(adapter);
        chatRv.scrollToPosition(chatList.size() - 1);
        initializeListener();
    }

    private void initializeListener() {
        chatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chatEt.getText().toString().equals("")) {
                    FirebaseDatabase.getInstance().getReference().child("chatroom").child(chatRoomId)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String friendId = "";
                                    for(DataSnapshot userId : dataSnapshot.child("users").getChildren()) {
                                        if (!((String) userId.getValue()).equals(PreferencesHelper.getToken(ChatRoomActivity.this))) {
                                            friendId = (String) userId.getValue();
                                            Log.d(TAG, "onDataChange: friendId " + friendId);
                                        }
                                    }
                                    Chat sendChat = new Chat(PreferencesHelper.getToken(ChatRoomActivity.this),
                                            PreferencesHelper.getUserName(ChatRoomActivity.this), friendId,
                                            Calendar.getInstance().getTime(), chatEt.getText().toString());
                                    String idChat = FirebaseHelper.dbMessage.push().getKey();
                                    Log.d(TAG, "onDataChange: idChat" + idChat);
                                    FirebaseHelper.dbMessage.child(idChat).setValue(sendChat);
                                    String index = String.valueOf(dataSnapshot.child("messages").getChildrenCount());
                                    Log.d(TAG, "onDataChange: index chat " + index);
                                    FirebaseDatabase.getInstance().getReference("chatroom/" + chatRoomId + "/messages")
                                            .child(index).setValue(idChat);
                                    chatEt.setText("");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                }
            }
        });

        FirebaseDatabase.getInstance().getReference("chatroom/" + chatRoomId + "/messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                FirebaseHelper.dbMessage.child((String) dataSnapshot.getValue()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        chatList.add(dataSnapshot.getValue(Chat.class));
                        adapter.notifyDataSetChanged();
                        chatRv.scrollToPosition(chatList.size() - 1);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
