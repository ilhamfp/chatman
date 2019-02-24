package com.chatman.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chatman.R;
import com.chatman.adapter.ChatListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatList extends Fragment {

    private RecyclerView recycler;
    private ChatListAdapter adapter;
    private List<com.chatman.model.ChatList> chatLists;
    private Context context;

    public ChatList() {

    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_chat_list, container, false);
        recycler = rootView.findViewById(R.id.chat_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(linearLayoutManager);
        getChatLists();
        adapter = new ChatListAdapter(chatLists);
        recycler.setAdapter(adapter);

        return rootView;
    }

    private List<com.chatman.model.ChatList> getChatLists() {
        chatLists = new ArrayList<>();
//        chatLists.add(new com.chatman.model.ChatList(
//                R.drawable.priagung,
//                "Priagung Satyagama",
//                "Woi sini ke sekre 2 temani diriku yang kesepian"
//        ));
//        chatLists.add(new com.chatman.model.ChatList(
//                R.drawable.rama,
//                "Yusuf Rahmat Pratama",
//                "Eh, gmn gmn?"
//        ));
//        chatLists.add(new com.chatman.model.ChatList(
//                R.drawable.priagung,
//                "Priagung Satyagama",
//                "Woi sini ke sekre 2 temani diriku yang kesepian"
//        ));
//        chatLists.add(new com.chatman.model.ChatList(
//                R.drawable.rama,
//                "Yusuf Rahmat Pratama",
//                "Eh, gmn gmn?"
//        ));
//        chatLists.add(new com.chatman.model.ChatList(
//                R.drawable.priagung,
//                "Priagung Satyagama",
//                "Woi sini ke sekre 2 temani diriku yang kesepian"
//        ));
//        chatLists.add(new com.chatman.model.ChatList(
//                R.drawable.rama,
//                "Yusuf Rahmat Pratama",
//                "Eh, gmn gmn?"
//        ));
//        chatLists.add(new com.chatman.model.ChatList(
//                R.drawable.priagung,
//                "Priagung Satyagama",
//                "Woi sini ke sekre 2 temani diriku yang kesepian"
//        ));
//        chatLists.add(new com.chatman.model.ChatList(
//                R.drawable.rama,
//                "Yusuf Rahmat Pratama",
//                "Eh, gmn gmn?"
//        ));
//        chatLists.add(new com.chatman.model.ChatList(
//                R.drawable.priagung,
//                "Priagung Satyagama",
//                "Woi sini ke sekre 2 temani diriku yang kesepian"
//        ));
//        chatLists.add(new com.chatman.model.ChatList(
//                R.drawable.rama,
//                "Yusuf Rahmat Pratama",
//                "Eh, gmn gmn?"
//        ));
//        chatLists.add(new com.chatman.model.ChatList(
//                R.drawable.priagung,
//                "Priagung Satyagama",
//                "Woi sini ke sekre 2 temani diriku yang kesepian"
//        ));
//        chatLists.add(new com.chatman.model.ChatList(
//                R.drawable.rama,
//                "Yusuf Rahmat Pratama",
//                "Eh, gmn gmn?"
//        ));
        return chatLists;
    }

}
