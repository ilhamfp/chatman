package com.chatman.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.chatman.R;
import com.chatman.adapter.ChatListAdapter;
import com.chatman.helper.PreferencesHelper;
import com.chatman.model.ChatList;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static String TAG = HomeFragment.class.getSimpleName();

    private RecyclerView recycler;
    private ChatListAdapter adapter;
    private List<ChatList> chatLists;
    private FloatingActionButton fab;
    private StartChatDialogFragment dialog;
    private ProgressBar progressBar;

    private Context mContext;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new StartChatDialogFragment();
                dialog.setContext(getActivity());
                dialog.show(getFragmentManager(), "ADD_ROOM");
            }
        });

        progressBar = view.findViewById(R.id.progressBarLoading);

        // Recycler View
        recycler = view.findViewById(R.id.chat_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(linearLayoutManager);
        chatLists = new ArrayList<>();
        getChatLists();
        adapter = new ChatListAdapter(chatLists);
        recycler.setAdapter(adapter);
        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private class MessageChildListener implements ChildEventListener {

        private String sender;
        private String idChatRoom;
        public MessageChildListener(String sender, String idChatRoom) {
            this.sender = sender;
            this.idChatRoom = idChatRoom;
        }

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            String messageId = (String) dataSnapshot.getValue();
            Log.d(TAG, "onDataChange: messageId " + messageId);
            FirebaseDatabase.getInstance().getReference().child("message")
                    .child(messageId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String message = (String) dataSnapshot.child("message").getValue();
                    Log.d(TAG, "onDataChange: message " + message);
                    for (ChatList chatList : new ArrayList<>(chatLists)) {
                        if (chatList.getIdChatRoom().equals(idChatRoom)) {
                            chatLists.remove(chatList);
                        }
                    }
                    progressBar.setVisibility(View.GONE);
                    chatLists.add(new ChatList(R.mipmap.chatman_launcher_round, sender, message, idChatRoom));
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    }

    private class UserChildListener implements ChildEventListener {

        private DataSnapshot chatRoomSnapshot;

        public UserChildListener(DataSnapshot chatRoomSnapshot) {
            this.chatRoomSnapshot = chatRoomSnapshot;
        }

        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            final String sender = (String) dataSnapshot.child("name").getValue();
            String idChatRoom = chatRoomSnapshot.getKey();
            Log.d(TAG, "onChildAdded: idChatRoom " + idChatRoom);
            Log.d(TAG, "onDataChange: sender " + sender);
            if (chatRoomSnapshot.hasChild("messages")) {
                Log.d(TAG, "onDataChange: chatroom is occupied with messages");
                chatRoomSnapshot.getRef().child("messages").addChildEventListener(new MessageChildListener(sender, idChatRoom));
            }
            else {
                for (ChatList chatList : new ArrayList<>(chatLists)) {
                    if (chatList.getIdChatRoom().equals(idChatRoom)) {
                        chatLists.remove(chatList);
                    }
                }
                progressBar.setVisibility(View.GONE);
                chatLists.add(new ChatList(R.mipmap.chatman_launcher_round, sender, "", idChatRoom));
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            progressBar.setVisibility(View.GONE);
        }
    }

    // Todo: Ganti data dummmy jadi data asli ngambil dari database
    // sorry for bad code :(
    private List<ChatList> getChatLists() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseDatabase.getInstance().getReference().child("chatroom").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull final DataSnapshot chatRoomSnapshot, @Nullable String s) {
                Log.d(TAG, "onDataChange: chatroom changed");
                Log.d(TAG, "onDataChange: chatroom key " + chatRoomSnapshot.getKey());
                chatRoomSnapshot.getRef().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                        Log.d(TAG, "onDataChange: users changed");
                        Log.d(TAG, "onDataChange: userSnapshot count " + userSnapshot.getChildrenCount());
                        String instanceId1 = (String) userSnapshot.child("0").getValue();
                        String instanceId2 = (String) userSnapshot.child("1").getValue();
                        Log.d(TAG, "onDataChange: instance id " + instanceId1);
                        Log.d(TAG, "onDataChange: instance id 2 " + instanceId2);
                        Boolean found = false;
                        if (instanceId1.equals(PreferencesHelper.getToken(mContext))) {
                            found = true;
                            if (instanceId2.equals("BOT_TOKEN")) {
                                found = false;
                            }
                            Log.d(TAG, "onDataChange: user found");
                            FirebaseDatabase.getInstance().getReference().child("user").orderByChild("key").limitToFirst(1)
                                    .equalTo(instanceId2).addChildEventListener(new UserChildListener(chatRoomSnapshot));
                        }
                        if (instanceId2.equals(PreferencesHelper.getToken(mContext))) {
                            found = true;
                            if (instanceId1.equals("BOT_TOKEN")) {
                                found = false;
                            }
                            Log.d(TAG, "onDataChange: user found 2");
                            FirebaseDatabase.getInstance().getReference().child("user").orderByChild("key").limitToFirst(1)
                                    .equalTo(instanceId1).addChildEventListener(new UserChildListener(chatRoomSnapshot));
                        }

                        if (!found) {
                            Log.d(TAG, "onDataChange: not found");
                            progressBar.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
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
                progressBar.setVisibility(View.GONE);
            }
        });

        return chatLists;
    }
}
