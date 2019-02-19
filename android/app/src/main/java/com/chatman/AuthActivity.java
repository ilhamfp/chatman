package com.etsi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.transition.TransitionManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etsi.helper.FirebaseHelper;
import com.etsi.helper.PreferencesHelper;
import com.etsi.model.Kreator;
import com.etsi.model.Sobat;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthActivity extends AppCompatActivity {

    // view component
    private LinearLayout authContainer;
    private CardView nameContainer;
    private CardView confirmContainer;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmEditText;
    private EditText nameEditText;
    private Button loginButton;
    private Button registerButton;
    private TextView switchTextView;


    // activity state
    private boolean isLoginView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        bindView();
        switchToLogin();
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
    }

    // do login
    public void login(View view) {
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();

        if (email.endsWith("@etsi.com")) {
            FirebaseHelper.dbKreator.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!loginAsKreator(dataSnapshot, email, password)) {
                        Toast.makeText(AuthActivity.this, "Email dan password tidak benar", Toast.LENGTH_SHORT).show();
                    } else {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            FirebaseHelper.dbSobat.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!loginAsSobat(dataSnapshot, email, password)) {
                        Toast.makeText(AuthActivity.this, "Email dan password tidak benar", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private boolean loginAsKreator(DataSnapshot dataSnapshot, String email, String password) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Kreator kreator = ds.getValue(Kreator.class);
            if (kreator.getEmail().equals(email) && kreator.getPassword().equals(md5(password))) {
                PreferencesHelper.setUserFirebaseKey(this, ds.getKey());
                PreferencesHelper.setCurrentKreatorKey(ds.getKey());
                PreferencesHelper.setUserRole(this, PreferencesHelper.USER_ROLE_KREATOR);
                startActivity(new Intent(this, MainActivity.class));
                return true;
            }
        }
        return false;
    }

    private boolean loginAsSobat(DataSnapshot dataSnapshot, String email, String password) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            Sobat sobat = ds.getValue(Sobat.class);
            if (sobat.getEmail().equals(email) && sobat.getPassword().equals(md5(password))) {
                PreferencesHelper.setUserFirebaseKey(this, ds.getKey());
                PreferencesHelper.setUserRole(this, PreferencesHelper.USER_ROLE_SOBAT);
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
            MessageDigest digest = java.security.MessageDigest.getInstance(MD5);
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
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirm = confirmEditText.getText().toString().trim();

        if (!password.equals(confirm)) {
            Toast.makeText(this, "Konfirmasi password tidak cocok", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.endsWith("@etsi.com")) {
            String key = FirebaseHelper.dbKreator.push().getKey();
            Kreator kreator = new Kreator(key, name, email, md5(password));
            kreator.setKey(key);
            PreferencesHelper.setUserRole(this, PreferencesHelper.USER_ROLE_KREATOR);
            PreferencesHelper.setUserFirebaseKey(this, key);
            FirebaseHelper.dbKreator.child(key).setValue(kreator);
        } else {
            String key = FirebaseHelper.dbSobat.push().getKey();
            Sobat sobat = new Sobat(key, name, email, md5(password));
            sobat.setKey(key);
            PreferencesHelper.setUserRole(this, PreferencesHelper.USER_ROLE_SOBAT);
            PreferencesHelper.setUserFirebaseKey(this, key);
            FirebaseHelper.dbSobat.child(key).setValue(sobat);
        }
        startActivity(new Intent(this, MainActivity.class));
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
}