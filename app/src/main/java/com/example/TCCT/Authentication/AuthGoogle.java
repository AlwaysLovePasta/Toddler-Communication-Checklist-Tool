package com.example.TCCT.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.TCCT.Activities.AddToddler;
import com.example.TCCT.Activities.Login;
import com.example.TCCT.Activities.MainActivity;
import com.example.TCCT.Database.NewUserData;
import com.example.TCCT.R;

        ;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class AuthGoogle extends Login {

    private static final int RC_SIGN_IN = 1;
    private static final String AUTH_TAG = "GOOGLE_AUTH";
    private static final String FIRESTORE_TAG = "FIRESTORE";
    private GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;
    private boolean isNewUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jump_page_google);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        firebaseAuth = FirebaseAuth.getInstance();
        signIn();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
                Log.e(AUTH_TAG, "firebaseAuthWithGoogle:" + account.getId());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e(AUTH_TAG, "Google sign in failed", e);
            }
        }
    }

    // [START auth_with_google]
    protected void firebaseAuthWithGoogle(String idToken) {
        Log.e(AUTH_TAG, "Access Token => " + idToken);
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.e(AUTH_TAG, "Sign In With Credential: Success");
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        isNewUser = Objects.requireNonNull(task.getResult().getAdditionalUserInfo()).isNewUser();
                        Log.e(AUTH_TAG, "onComplete: " + (isNewUser ? "new user" : "old user"));
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the user.
                        updateUI(null);
                    }
                });
    }
    // [END auth_with_google]

    protected void updateUI(FirebaseUser user) {
        if (user != null) {
            if (isNewUser) {
                NewUserData userData = new NewUserData(this, user);
                userData.create();
                startActivity(new Intent(AuthGoogle.this, AddToddler.class));
            } else {
                startActivity(new Intent(AuthGoogle.this, MainActivity.class));
                finish();
            }
        } else {
            Log.e(FIRESTORE_TAG, "Current user didn't exist");
        }
    }
}
