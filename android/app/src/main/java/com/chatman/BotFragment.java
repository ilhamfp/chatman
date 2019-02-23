package com.chatman;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.chatman.helper.FirebaseHelper;
import com.chatman.helper.PreferencesHelper;
import com.chatman.model.Chat;
import com.chatman.model.User;

import java.util.Calendar;


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
                }
            }
        });

        // Inflate the layout for this fragment
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
