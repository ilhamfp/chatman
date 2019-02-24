package com.chatman.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.chatman.R;
import com.chatman.activity.ChatRoomActivity;
import com.chatman.model.ChatList;

import java.util.ArrayList;
import java.util.List;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListHolder> {

    private List<ChatList> items = new ArrayList<>();
    private int itemLayout = R.layout.item_chat_list;
    private Context context;

    public ChatListAdapter(List<ChatList> items) {
        this.items = items;
    }

    @Override
    public ChatListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new ChatListHolder(LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false));
    }

    @Override
    public void onBindViewHolder(ChatListHolder holder, final int position) {
        holder.bind(items.get(position));
        final String nama = holder.chatListName.getText().toString();
        holder.chatListBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chatRoomId = items.get(position).getIdChatRoom();
                String name = items.get(position).getName();
                Intent intent = new Intent(context, ChatRoomActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("chatRoomId", chatRoomId);
                context.startActivity(intent);
                Toast.makeText(context, "Show "+nama+" chat room", Toast.LENGTH_SHORT).show();
        }
        });
    }

    public void addItems(List<ChatList> items) {
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    public class ChatListHolder extends RecyclerView.ViewHolder {
        private RelativeLayout chatListBackground;
        private TextView chatListName;
        private TextView chatListLastMessage;
        private ImageView chatListImage;

        public ChatListHolder(View itemView) {
            super(itemView);
            bindView();
        }

        private void bindView() {
            chatListImage = itemView.findViewById(R.id.chat_list_avatar);
            chatListLastMessage = itemView.findViewById(R.id.chat_list_last_message);
            chatListName = itemView.findViewById(R.id.chat_list_name);
            chatListBackground = itemView.findViewById(R.id.chat_list_rl);
        }

        public void bind(ChatList item) {
            Glide.with(itemView).load(item.getAvatarResource()).into(chatListImage);
            chatListLastMessage.setText(item.getLastMessage());
            chatListName.setText(item.getName());
        }

    }
}