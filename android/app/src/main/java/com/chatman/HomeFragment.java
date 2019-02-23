package com.chatman;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chatman.adapter.ChatListAdapter;
import com.chatman.model.ChatList;

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

    private RecyclerView recycler;
    private ChatListAdapter adapter;
    private List<ChatList> chatLists;

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
        // Recycler View
        recycler = view.findViewById(R.id.chat_list_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recycler.setLayoutManager(linearLayoutManager);
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

    // Todo: Ganti data dummmy jadi data asli ngambil dari database
    private List<ChatList> getChatLists() {
        chatLists = new ArrayList<>();
        chatLists.add(new ChatList(
                R.drawable.priagung,
                "Priagung Satyagama",
                "Woi sini ke sekre 2 temani diriku yang kesepian"
        ));
        chatLists.add(new ChatList(
                R.drawable.rama,
                "Yusuf Rahmat Pratama",
                "Eh, gmn gmn?"
        ));
        chatLists.add(new ChatList(
                R.drawable.priagung,
                "Priagung Satyagama2",
                "Woi sini ke sekre 2 temani diriku yang kesepian"
        ));
        chatLists.add(new ChatList(
                R.drawable.rama,
                "Yusuf Rahmat Pratama2",
                "Eh, gmn gmn?"
        ));
        chatLists.add(new ChatList(
                R.drawable.priagung,
                "Priagung Satyagama3",
                "Woi sini ke sekre 2 temani diriku yang kesepian"
        ));
        chatLists.add(new ChatList(
                R.drawable.rama,
                "Yusuf Rahmat Pratama3",
                "Eh, gmn gmn?"
        ));
        chatLists.add(new ChatList(
                R.drawable.priagung,
                "Priagung Satyagama4",
                "Woi sini ke sekre 2 temani diriku yang kesepian"
        ));
        chatLists.add(new ChatList(
                R.drawable.rama,
                "Yusuf Rahmat Pratama4",
                "Eh, gmn gmn?"
        ));
        chatLists.add(new ChatList(
                R.drawable.priagung,
                "Priagung Satyagama5",
                "Woi sini ke sekre 2 temani diriku yang kesepian"
        ));
        chatLists.add(new ChatList(
                R.drawable.rama,
                "Yusuf Rahmat Pratama5",
                "Eh, gmn gmn?"
        ));
        chatLists.add(new ChatList(
                R.drawable.priagung,
                "Priagung Satyagama6",
                "Woi sini ke sekre 2 temani diriku yang kesepian"
        ));
        chatLists.add(new ChatList(
                R.drawable.rama,
                "Yusuf Rahmat Pratama6",
                "Eh, gmn gmn?"
        ));
        return chatLists;
    }
}
