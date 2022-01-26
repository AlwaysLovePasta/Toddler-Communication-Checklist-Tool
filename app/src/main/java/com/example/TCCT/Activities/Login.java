package com.example.TCCT.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.TCCT.Authentication.AuthDefault;
import com.example.TCCT.Authentication.AuthFacebook;
import com.example.TCCT.Authentication.AuthGoogle;
import com.example.TCCT.Database.NewUserData;
import com.example.TCCT.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class Login extends AppCompatActivity {

    private MaterialButton btnLogin, btnGoogle, btnFacebook;
    private EditText edtEmail, edtPassword;

    private static final String AUTH_TAG = "DEFAULT_AUTH";
    private boolean isNewUser = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Animation animBounce = AnimationUtils.loadAnimation(this, R.anim.bounce);

        /* View Components */
        edtEmail = findViewById(R.id.edtMail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);
        btnFacebook = findViewById(R.id.btnFacebook);
        /* View Components */

        btnLogin.setOnClickListener(view -> {
            btnLogin.startAnimation(animBounce);
            checkEditField(edtEmail, edtPassword);
        });

        btnGoogle.setOnClickListener(view -> {
            btnGoogle.startAnimation(animBounce);
            startActivity(new Intent(this, AuthGoogle.class));
        });
        btnFacebook.setOnClickListener(view -> {
            btnFacebook.startAnimation(animBounce);
            startActivity(new Intent(this, AuthFacebook.class));
        });
    }


    private void checkEditField(EditText edtEmail, EditText edtPassword) {
        String email = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("必須輸入電子信箱");
            edtEmail.requestFocus();
        } else if (TextUtils.isEmpty(password)) {
            edtPassword.setError("必須輸入密碼");
            edtPassword.requestFocus();
        } else if (edtPassword.getText().length() < 6){
            edtPassword.setError("密碼長度必須至少6個字元");
            edtPassword.requestFocus();
        }
        else {
            Intent intent = new Intent(this, AuthDefault.class);
            Bundle bundle = new Bundle();
            bundle.putString("EMAIL", email);
            bundle.putString("PASSWORD", password);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}