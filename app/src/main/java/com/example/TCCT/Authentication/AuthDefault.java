package com.example.TCCT.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.TCCT.Activities.AddToddler;
import com.example.TCCT.Activities.Login;
import com.example.TCCT.Activities.MainActivity;
import com.example.TCCT.Database.NewUserData;
import com.example.TCCT.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.Objects;

public class AuthDefault extends Login {

    private FirebaseAuth firebaseAuth;
    private static final String AUTH_TAG = "DEFAULT_AUTH";
    private boolean isNewUser = false;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jump_page_default);

        firebaseAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        email = bundle.getString("EMAIL");
        password = bundle.getString("PASSWORD");

        checkIsNewUser(email);
    }

    private void checkIsNewUser(String email) {
        firebaseAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    isNewUser = task.getResult().getSignInMethods().isEmpty();
                    if (isNewUser) {
                        Log.e("TAG", "Is New User!");
                        createAccount(email, password);
                    } else {
                        Log.e("TAG", "Is Old User!");
                        signIn(email, password);
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI(currentUser);
        }
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(AUTH_TAG, "createUserWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(AUTH_TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(AuthDefault.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e(AUTH_TAG, "signInWithEmail:success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        isNewUser = Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser();
                        Log.e(AUTH_TAG, "onComplete: " + (isNewUser ? "new user" : "old user"));
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e(AUTH_TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(AuthDefault.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                });
        // [END sign_in_with_email]
    }

    protected void updateUI(FirebaseUser user) {
        if (user != null) {
            if (isNewUser) {
                NewUserData userData = new NewUserData(this, user);
                userData.create();
                startActivity(new Intent(AuthDefault.this, AddToddler.class));
            } else {
                startActivity(new Intent(AuthDefault.this, MainActivity.class));
                finish();
            }
        } else {
            Log.e(AUTH_TAG, "Current user didn't exist");
        }
    }
}
