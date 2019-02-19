package com.chatman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chatman.helper.FirebaseHelper;
import com.chatman.helper.PreferencesHelper;
import com.chatman.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthActivity extends AppCompatActivity {

    // view component
    private LinearLayout authContainer;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private EditText nameEditText;
    private Button loginButton;
    private Button registerButton;
    private TextView switchTextView;
    private ImageView authImageView;
    private Context context;


    // activity state
    private boolean isLoginView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        bindView();
        switchToLogin();
        context = this;
    }

    // bind all component view with it's xml
    private void bindView() {
        authContainer = findViewById(R.id.auth_container);
        emailEditText = findViewById(R.id.auth_email);
        passwordEditText = findViewById(R.id.auth_password);
        confirmEditText = findViewById(R.id.auth_confirm);
        nameEditText = findViewById(R.id.auth_name);
        loginButton = findViewById(R.id.auth_login);
        registerButton = findViewById(R.id.auth_register);
        switchTextView = findViewById(R.id.auth_switch);
        authImageView = findViewById(R.id.auth_logo);
        Glide.with(this).load(getImage("logo")).fitCenter().into(authImageView);
    }

    // do login
    public void login(final View view) {
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();

        FirebaseHelper.dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!loginAsUser(dataSnapshot, email, password)) {
                    showSnackBar((AuthActivity)view.getContext(), "Email or password is incorrect!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private boolean loginAsUser(DataSnapshot dataSnapshot, String email, String password) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            User user = ds.getValue(User.class);
            if (user.getEmail().equals(email) && user.getPassword().equals(md5(password))) {
                PreferencesHelper.setUserFirebaseKey(this, ds.getKey());
                PreferencesHelper.setUserName(this, user.getName());
                startActivity(new Intent(this, MainActivity.class));
                return true;
            }
        }
        return false;
    }

    private String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    // do register
    public void register(View view) {
        String name = nameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirm = confirmEditText.getText().toString().trim();

        if (password.equals(confirm)) {
            final boolean[] emailUnique = new boolean[] {true};
            FirebaseHelper.dbUser.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        User user = ds.getValue(User.class);
                        if (user.getEmail().equalsIgnoreCase(email)) {
                            emailUnique[0] = false;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });

            if (emailUnique[0]) {
                String key = FirebaseHelper.dbUser.push().getKey();
                User user = new User(key, name, email, md5(password));
                user.setKey(key);
                PreferencesHelper.setUserFirebaseKey(this, key);
                PreferencesHelper.setUserName(this, user.getName());

                FirebaseHelper.dbUser.child(key).setValue(user);


                finish();
                startActivity(new Intent(this, MainActivity.class));
            } else {
                showSnackBar(this, "Email has been used");
            }
        } else {
            showSnackBar(this, "Password did not match");
        }
    }

    // switch between login and register
    public void switchAction(View view) {
        isLoginView = !isLoginView;
        if (isLoginView) {
            switchToLogin();
        } else {
            switchToRegister();
        }
    }

    private void switchToLogin() {
        TransitionManager.beginDelayedTransition(authContainer);
        nameEditText.setVisibility(View.GONE);
        confirmEditText.setVisibility(View.GONE);
        loginButton.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.GONE);
        switchTextView.setText(getString(R.string.register));
    }

    private void switchToRegister() {
        TransitionManager.beginDelayedTransition(authContainer);
        nameEditText.setVisibility(View.VISIBLE);
        confirmEditText.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);
        registerButton.setVisibility(View.VISIBLE);
        switchTextView.setText(getString(R.string.login));
    }

    public int getImage(String imageName) {

        int drawableResourceId = this.getResources().getIdentifier(imageName, "drawable", this.getPackageName());

        return drawableResourceId;
    }

    public void showSnackBar(Activity activity, String message){
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar.make(rootView, message, Snackbar.LENGTH_SHORT).show();
    }
}