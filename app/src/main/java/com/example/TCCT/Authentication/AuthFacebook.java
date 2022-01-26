package com.example.TCCT.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.TCCT.Activities.AddToddler;
import com.example.TCCT.Activities.Login;
import com.example.TCCT.Activities.MainActivity;
import com.example.TCCT.Database.NewUserData;
import com.example.TCCT.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class AuthFacebook extends Login {

    private CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    private static final String AUTH_TAG = "FACEBOOK_AUTH";
    private boolean isNewUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jump_page_facebook);

        callbackManager = CallbackManager.Factory.create();
        firebaseAuth = FirebaseAuth.getInstance();

        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.sdkInitialize(getApplicationContext());
            AppEventsLogger.activateApp(this.getApplication());
        }

        LoginManager.getInstance().logInWithReadPermissions(AuthFacebook.this, Arrays.asList("public_profile"));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.e(AUTH_TAG, exception.getMessage());
                        if (AccessToken.getCurrentAccessToken() != null && com.facebook.Profile.getCurrentProfile() != null) {
                            LoginManager.getInstance().logOut();
                        }
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.e(AUTH_TAG, "Pass the activity result back to the Facebook SDK");
    }


    protected void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e(AUTH_TAG, "Sign In With Credential: Success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        isNewUser = task.getResult().getAdditionalUserInfo().isNewUser();
                        Log.e(AUTH_TAG, "onComplete: " + (isNewUser ? "new user" : "old user"));
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.e(AUTH_TAG, "Sign In With Credential: Failure", task.getException());
                        updateUI(null);
                    }
                });
    }

    protected void updateUI(FirebaseUser user) {
        if (user != null) {
            if (isNewUser) {
                NewUserData userData = new NewUserData(this, user);
                userData.create();
                startActivity(new Intent(AuthFacebook.this, AddToddler.class));
            } else {
                startActivity(new Intent(AuthFacebook.this, MainActivity.class));
                finish();
            }
        } else {
            Toast.makeText(AuthFacebook.this, "帳號連結失敗", Toast.LENGTH_SHORT).show();
        }
    }
}
