package com.chatman.helper;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseHelper {

    private static final String DB_USER = "user";
    private static final String DB_MESSAGE = "MESSAGE";

    public static String USER_KEY, USER_EMAIL, USER_NAMA, USER_PASSWORD;
    private static DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    public static final DatabaseReference dbUser = reference.child(DB_USER);
    public static final DatabaseReference dbMessage = reference.child(DB_MESSAGE);
}
