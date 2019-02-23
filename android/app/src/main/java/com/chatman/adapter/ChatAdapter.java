package com.chatman.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.chatman.R;
import com.chatman.helper.PreferencesHelper;
import com.chatman.model.Chat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    private List<Chat> items = new ArrayList<>();
    private int itemLayoutSent = R.layout.item_message_sent;
    private int itemLayoutReceived = R.layout.item_message_received;
    private Context context;
    private String currentUser;

    public ChatAdapter(List<Chat> items) {
        this.items = items;
    }

//    @Override
//    public SentMessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new SentMessageHolder(LayoutInflater.from(parent.getContext()).inflate(itemLayoutSent, parent, false));
//    }

//    @Override
//    public void onBindViewHolder(SentMessageHolder holder, int position) {
//        holder.bind(items.get(position));
//    }

    public void addItems(List<Chat> items) {
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

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        Chat message = (Chat) items.get(position);

        if (message.getIdSender().equals(currentUser)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        currentUser = PreferencesHelper.getUserFirebaseKey(context);
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }

        return null;
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Chat message = (Chat) items.get(position);

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private class SentMessageHolder extends RecyclerView.ViewHolder {
        private TextView messageText;
        private TextView timeText;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = (TextView) itemView.findViewById(R.id.sent_text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.sent_text_message_time);
        }

        void bind(Chat message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String strDate = dateFormat.format(message.getDate());
            timeText.setText(strDate);
        }
    }

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        private TextView messageText, timeText, nameText;
        private ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = (TextView) itemView.findViewById(R.id.received_text_message_body);
            timeText = (TextView) itemView.findViewById(R.id.received_text_message_time);
            nameText = (TextView) itemView.findViewById(R.id.received_text_message_name);
            profileImage = (ImageView) itemView.findViewById(R.id.received_image_message_profile);
        }

        void bind(Chat message) {
            messageText.setText(message.getMessage());

            // Format the stored timestamp into a readable String using method.
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String strDate = dateFormat.format(message.getDate());
            timeText.setText(strDate);

            nameText.setText(message.getNameSender());

            // Insert the profile image from the URL into the ImageView.
            Glide.with(itemView).load(R.drawable.robotic).into(profileImage);
        }

    }

//    public int getImage(String imageName) {
//
//        int drawableResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
//
//        return drawableResourceId;
//    }
}