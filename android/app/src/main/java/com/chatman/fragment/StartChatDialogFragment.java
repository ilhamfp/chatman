package com.chatman.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chatman.R;
import com.chatman.helper.FirebaseHelper;
import com.chatman.helper.PreferencesHelper;
import com.chatman.model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartChatDialogFragment extends DialogFragment {

    private static final String TAG = StartChatDialogFragment.class.getSimpleName();

    private EditText email;
    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.start_chat_dialog, null);
        email = view.findViewById(R.id.email);

        builder.setView(view)
                .setPositiveButton(R.string.start_chat, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!email.getText().toString().equals("")) {
                            FirebaseHelper.dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Boolean found = false;
                                    for (DataSnapshot ds: dataSnapshot.getChildren()) {
                                        User user = ds.getValue(User.class);
                                        if (user.getEmail().equalsIgnoreCase(email.getText().toString())) {
                                            found = true;
                                            createRoom(user.getKey(), PreferencesHelper.getToken(context));
                                        }
                                    }
                                    if (!found) {
                                        showToast(context, "User not found");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                        else {
                            showToast(context, "Please insert a valid email");
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StartChatDialogFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();

    }

    private void createRoom(final String userId1, final String userId2) {
        FirebaseDatabase.getInstance().getReference("chatroom").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean foundRoom = false;
                for (DataSnapshot chatroom: dataSnapshot.getChildren()) {
                    String idUser1 = (String) chatroom.child("users/0").getValue();
                    String idUser2 = (String) chatroom.child("users/1").getValue();
                    if ((idUser1.equals(userId1) && idUser2.equals(userId2)) ||
                            (idUser1.equals(userId2) && idUser2.equals(userId1))) {
                        foundRoom = true;
                        break;
                    }
                }
                if (foundRoom) {
                    Log.d(TAG, "onDataChange: foundroom");
                }
                else {
                    String idRoom = FirebaseDatabase.getInstance().getReference("chatroom").push().getKey();
                    FirebaseDatabase.getInstance().getReference("chatroom/" + idRoom)
                            .child("users/0").setValue(userId1);
                    FirebaseDatabase.getInstance().getReference("chatroom/" + idRoom)
                            .child("users/1").setValue(userId2);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public void showSnackBar(Activity activity, String message){
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }
}
