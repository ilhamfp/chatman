package com.chatman;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.chatman.adapter.ChatAdapter;
import com.chatman.adapter.ChatListAdapter;
import com.chatman.helper.FirebaseHelper;
import com.chatman.helper.PreferencesHelper;
import com.chatman.model.Chat;
import com.chatman.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BotFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BotFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ImageButton sentButton;
    private EditText message;
    private Context context;
    private RecyclerView recycler;
    private ChatAdapter adapter;
    private List<Chat> chatList;
    private static final String BOT_KEY = "BOT_KEY";

    public BotFragment() {
        // Required empty public constructor
    }


    public static BotFragment newInstance() {
        BotFragment fragment = new BotFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bot, container, false);

        sentButton = view.findViewById(R.id.chat_bot_room_send);
        message = view.findViewById(R.id.chat_bot_room_et);

        sentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!message.getText().toString().equals("")){
                    Toast.makeText(context, "Send: "+message.getText().toString(), Toast.LENGTH_SHORT).show();
                    Chat sendMessage = new Chat(PreferencesHelper.getUserFirebaseKey(context), PreferencesHelper.getUserName(context), BOT_KEY, Calendar.getInstance().getTime(), message.getText().toString());
                    String key = FirebaseHelper.dbMessage.push().getKey();
                    FirebaseHelper.dbMessage.child(key).setValue(sendMessage);

                    // BALASAN DARI BOT
                    Chat botMessage = new Chat(BOT_KEY, "ChatMan Bot", PreferencesHelper.getUserFirebaseKey(context), Calendar.getInstance().getTime(), "Halo! Selamat datang di ChatMan");
                    String botKey = FirebaseHelper.dbMessage.push().getKey();
                    FirebaseHelper.dbMessage.child(botKey).setValue(botMessage);

                }
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        // Recycler View
        recycler = view.findViewById(R.id.chat_bot_room_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);
        recycler.setLayoutManager(linearLayoutManager);
        adapter = new ChatAdapter(chatList);
        recycler.setAdapter(adapter);


        FirebaseHelper.dbMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getAllChat(dataSnapshot);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getAllChat(DataSnapshot dataSnapshot) {
        chatList = new ArrayList<>();
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (ds.getValue(Chat.class).getIdSender().equals(PreferencesHelper.getUserFirebaseKey(context)) || ds.getValue(Chat.class).getIdReceiver().equals(PreferencesHelper.getUserFirebaseKey(context))) {
                chatList.add(ds.getValue(Chat.class));
            }
        }
        adapter = new ChatAdapter(chatList);
        recycler.setAdapter(adapter);
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



}
