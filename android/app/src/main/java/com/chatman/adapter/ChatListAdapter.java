package com.chatman.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.chatman.R;
import com.chatman.model.ChatList;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListHolder> {

    private List<ChatList> items = new ArrayList<>();
    private int itemLayout = R.layout.item_chat_list;

    public ChatListAdapter(List<ChatList> items) {
        this.items = items;
    }

    @Override
    public ChatListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChatListHolder(LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false));
    }

    @Override
    public void onBindViewHolder(ChatListHolder holder, int position) {
        holder.bind(items.get(position));
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
        }

        public void bind(ChatList item) {
            Glide.with(itemView).load(item.getAvatarResource()).into(chatListImage);
            chatListLastMessage.setText(item.getLastMessage());
            chatListName.setText(item.getName());
        }
    }
}