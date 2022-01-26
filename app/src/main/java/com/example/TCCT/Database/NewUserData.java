package com.example.TCCT.Database;

import android.content.Context;
import android.widget.Toast;

import com.example.TCCT.DataModel.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NewUserData {

    Context context;
    FirebaseFirestore firestore;
    FirebaseUser firebaseUser;

    public NewUserData(Context context, FirebaseUser firebaseUser) {
        this.context = context;
        this.firebaseUser = firebaseUser;
    }

    public void create() {
        User user = new User(0);
        firestore = FirebaseFirestore.getInstance();
        DocumentReference reference = firestore.document("User/" + firebaseUser.getUid());

        reference.set(user)
                .addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}
