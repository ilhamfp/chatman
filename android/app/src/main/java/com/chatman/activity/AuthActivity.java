package com.chatman.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chatman.R;
import com.chatman.fragment.LoadingDialogFragment;
import com.chatman.helper.FirebaseHelper;
import com.chatman.helper.PreferencesHelper;
import com.chatman.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthActivity extends AppCompatActivity {

    private static final String TAG = AuthActivity.class.getSimpleName();

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
    private TextView authTextView;
    private LoadingDialogFragment loadingDialog;


    // activity state
    private boolean isLoginView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PreferencesHelper.getHasLogin(this)){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        } else {
            setContentView(R.layout.activity_auth);
            bindView();
            switchToLogin();
            context = this;
        }
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
        authTextView = findViewById(R.id.auth_word);
        loadingDialog = new LoadingDialogFragment(this);
        loadingDialog.setCancelable(false);
        loadingDialog.setCanceledOnTouchOutside(false);
        Glide.with(this).load(getImage("logo")).fitCenter().into(authImageView);
    }

    // do login
    public void login(final View view) {
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        loadingDialog.show();
        Log.d(TAG, "login: instance id di sharedpref " + PreferencesHelper.getToken(AuthActivity.this));


        FirebaseHelper.dbUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!loginAsUser(dataSnapshot, email, password)) {
                    showSnackBar((AuthActivity)view.getContext(), "Email or password is incorrect!");
                    loadingDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loadingDialog.dismiss();
            }
        });

    }

    private boolean loginAsUser(DataSnapshot dataSnapshot, String email, String password) {
        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            final User user = ds.getValue(User.class);
            if (user.getEmail().equals(email) && user.getPassword().equals(md5(password))) {
                PreferencesHelper.setUserFirebaseId(AuthActivity.this, user.getId());
                PreferencesHelper.setUserName(this, user.getName());
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        Log.d(TAG, "onComplete: instance id waktu login awal " + task.getResult().getToken());
                        Log.d(TAG, "onComplete: instance id di sharedpref " + PreferencesHelper.getToken(AuthActivity.this));
                        updateInstanceId(task.getResult().getToken(), user.getKey());
                    }
                });
                return true;
            }
            else {
                loadingDialog.dismiss();
            }
        }
        return false;
    }

    private void updateInstanceId(final String instanceId, final String oldInstanceId) {
        PreferencesHelper.setTokenKey(this, instanceId);
        FirebaseDatabase.getInstance().getReference("chatroom").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: id database " + PreferencesHelper.getUserFirebaseKey(AuthActivity.this));
                FirebaseHelper.dbUser.child(PreferencesHelper.getUserFirebaseKey(AuthActivity.this)).child("key").setValue(instanceId);
                Boolean found = false;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String idUser1 = (String) ds.child("users/0").getValue();
                    String idUser2 = (String) ds.child("users/1").getValue();
                    if (idUser1.equals(oldInstanceId)) {
                        ds.child("users/0").getRef().setValue(instanceId);
                        updateMessages(ds.child("messages"), instanceId, oldInstanceId);
                        found = true;
                    }
                    if (idUser2.equals(oldInstanceId)) {
                        ds.child("users/1").getRef().setValue(instanceId);
                        updateMessages(ds.child("messages"), instanceId, oldInstanceId);
                        found = true;
                    }
                }
                if (!found) {
                    loadingDialog.dismiss();
                    PreferencesHelper.setHasLogin(AuthActivity.this, true);
                    finish();
                    startActivity(new Intent(AuthActivity.this, MainActivity.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateMessages(DataSnapshot messages, final String instanceId, final String oldInstanceId) {
        for (final DataSnapshot message: messages.getChildren()) {
            FirebaseHelper.dbMessage.child((String) message.getValue()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String idReceiver = (String) dataSnapshot.child("idReceiver").getValue();
                    String idSender = (String) dataSnapshot.child("idSender").getValue();
                    if (idReceiver.equals(oldInstanceId)) {
                        dataSnapshot.child("idReceiver").getRef().setValue(instanceId);
                    }
                    if (idSender.equals(oldInstanceId)) {
                        dataSnapshot.child("idSender").getRef().setValue(instanceId);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    loadingDialog.dismiss();
                }
            });
        }
        loadingDialog.dismiss();
        PreferencesHelper.setHasLogin(AuthActivity.this, true);
        finish();
        startActivity(new Intent(AuthActivity.this, MainActivity.class));
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
        final String name = nameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        final String password = passwordEditText.getText().toString().trim();
        final String confirm = confirmEditText.getText().toString().trim();
        Log.d(TAG, "register: name " + name);
        Log.d(TAG, "register: email " + email);
        Log.d(TAG, "register: password " + password);
        if (password.equals(confirm)) {
            loadingDialog.show();
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

            if (emailUnique[0] && !name.equals("") && !email.equals("") && !password.equals("") && !confirm.equals("") ) {
                Log.d(TAG, "onDataChange: register : masuk");
                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        String idFirebase = FirebaseHelper.dbUser.push().getKey();
                        String instanceId = task.getResult().getToken();
                        Log.d(TAG, "onComplete: idFirebase " + idFirebase);
                        Log.d(TAG, "onComplete: idInstance " + instanceId);
                        User user;
                        user = new User(instanceId, name, email, md5(password), idFirebase);
                        PreferencesHelper.setUserFirebaseId(AuthActivity.this, user.getId());
                        PreferencesHelper.setTokenKey(AuthActivity.this, user.getKey());
                        PreferencesHelper.setUserName(AuthActivity.this, user.getName());
                        PreferencesHelper.setHasLogin(AuthActivity.this, true);

                        FirebaseHelper.dbUser.child(idFirebase).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String idRoom = FirebaseDatabase.getInstance().getReference("chatroom").push().getKey();
                                PreferencesHelper.setBotRoom(AuthActivity.this, idRoom);
                                FirebaseDatabase.getInstance().getReference("chatroom/" + idRoom)
                                        .child("users/0").setValue("BOT_TOKEN");
                                FirebaseDatabase.getInstance().getReference("chatroom/" + idRoom)
                                        .child("users/1").setValue(PreferencesHelper.getToken(AuthActivity.this));
                                loadingDialog.dismiss();
                                finish();
                                startActivity(new Intent(AuthActivity.this, MainActivity.class));
                            }
                        });
                    }
                });
            } else {
                loadingDialog.dismiss();
                showSnackBar(AuthActivity.this, "Email has been used");
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
        authTextView.setText(R.string.register_word);
        switchTextView.setText(getString(R.string.register));
    }

    private void switchToRegister() {
        TransitionManager.beginDelayedTransition(authContainer);
        nameEditText.setVisibility(View.VISIBLE);
        confirmEditText.setVisibility(View.VISIBLE);
        loginButton.setVisibility(View.GONE);
        registerButton.setVisibility(View.VISIBLE);
        switchTextView.setText(getString(R.string.login));
        authTextView.setText(getString(R.string.login_word));

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