package com.example.TCCT.Database;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.TCCT.Activities.AddToddler;
import com.example.TCCT.Activities.MainActivity;
import com.example.TCCT.Adapter.RecyclerView.MilestoneListAdapter;
import com.example.TCCT.Adapter.RecyclerView.ToddlerListAdapter;
import com.example.TCCT.DataModel.Feedback;
import com.example.TCCT.DataModel.Score;
import com.example.TCCT.DataModel.Toddler;

import com.example.TCCT.Dialogs.SpinnerDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SuppressLint("DefaultLocale")
public class FirebaseData {

    private static final String FIRESTORE_TAG = "FIRESTORE";
    private static final String STORAGE_TAG = "STORAGE";
    public static final int INIT_FIRESTORE = 0;
    public static final int INIT_STORAGE = 1;
    public static final int INIT_BOTH = 2;
    public static final int INIT_NONE = 3;
    private static final String KEY_IMAGE = "imgUri";
    private static final String KEY_BASIC = "basicData";
    private static final String KEY_DETAIL = "detailData";

    private final String userUID;
    private FirebaseFirestore firestore;
    private FirebaseStorage storage;
    private SpinnerDialog spinnerDialog;

    private Uri imgUri;
    private ArrayList<String> basicData, detailData;
    private Integer index;

    public FirebaseData(String userUID, int initCode) {
        this.userUID = userUID;

        if (initCode == 0) {
            initFirestore();
        } else if (initCode == 1) {
            initStorage();
        } else if (initCode == 2) {
            initFirestore();
            initStorage();
        }
    }

    public interface GetIndex {
        void onCallback(Integer index);
    }

    public interface OnRetrieveData {
        void onRetrieve(String imgUri, ArrayList<String> basicData, ArrayList<String> detailData);
    }

    public interface OnRetrieveScore {
        void onRetrieve(List<Integer> scoreSet);
    }

    public interface OnRetrievePosition {
        void onRetrieve(Integer currentPosition);
    }

    public interface OnRetrieveMilestone {
        void onRetrieve(ArrayList<Integer> milestoneIndex);
    }

    public void initFirestore() {
        firestore = FirebaseFirestore.getInstance();
    }

    public void initStorage() {
        storage = FirebaseStorage.getInstance();
    }

    public void createDataField(Uri imgUri, ArrayList<String> basicData, ArrayList<String> detailData) {
        this.imgUri = imgUri;
        this.basicData = basicData;
        this.detailData = detailData;
    }

    @SuppressLint("DefaultLocale")
    public void addToddler(Activity activity) {
        checkIndex(index -> {
            if (imgUri != null) {
                String fullUrl = "gs://independent-study-projec-bfae8.appspot.com/images/" + userUID;
                String fileName = UUID.randomUUID().toString();
                StorageReference storageReference = storage.getReferenceFromUrl(fullUrl);
                StorageReference fileReference = storageReference.child(fileName);
                fileReference.putFile(imgUri).addOnSuccessListener(taskSnapshot ->
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            Log.e(STORAGE_TAG, "Get download image uri success");
                            Toddler toddler = new Toddler(uri.toString(), basicData, detailData);
                            String documentPath = String.format("User/%s/Toddler/%03d", userUID, index);
                            DocumentReference documentReference = firestore.document(documentPath);
                            documentReference.set(toddler)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            //所有資料集合建立完後，回到首頁
                                            Log.e(FIRESTORE_TAG, "Add new toddler success");
                                            activity.startActivity(new Intent(activity, MainActivity.class));
                                            activity.finish();
                                            spinnerDialog.dismiss();
                                            updateCurrentToddler(index);
                                        }
                                    })
                                    .addOnFailureListener(e -> Log.e(FIRESTORE_TAG, e.getMessage()));
                        }).addOnFailureListener(e -> Toast.makeText(activity, e.getMessage(), Toast.LENGTH_LONG).show())
                );
            }
        });
    }

    @SuppressLint("DefaultLocale")
    public void readToddlerData(Context context, OnRetrieveData onRetrieveData) {
        getCurrentToddler(currentPosition -> {
            String documentPath = String.format("User/%s/Toddler/%03d", userUID, currentPosition);
            DocumentReference documentToddler = firestore.document(documentPath);
            documentToddler.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            onRetrieveData.onRetrieve(
                                    documentSnapshot.getString("imgUri"),
                                    (ArrayList<String>) documentSnapshot.get("basicData"),
                                    (ArrayList<String>) documentSnapshot.get("detailData"));
                        }
                    }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }

    public void updateToddlerData(Activity activity, Uri imgUri, ArrayList<String> basicData, ArrayList<String> detailData) {
        getCurrentToddler(currentPosition -> {
            String documentPath = String.format("User/%s/Toddler/%03d", userUID, currentPosition);
            DocumentReference documentToddler = firestore.document(documentPath);
            documentToddler.update(KEY_BASIC, basicData);
            documentToddler.update(KEY_DETAIL, detailData);
            if (imgUri != null) {
                updateToddlerImage(activity, imgUri, documentToddler);
            } else {
                activity.startActivity(new Intent(activity, MainActivity.class));
                activity.finish();
                spinnerDialog.dismiss();
            }
        });
    }

    public void updateToddlerImage(Activity activity, Uri imgUri, DocumentReference documentReference) {
        String fullUrl = "gs://independent-study-projec-bfae8.appspot.com/images/" + userUID;
        StorageReference storageReference = storage.getReferenceFromUrl(fullUrl);
        String fileName = UUID.randomUUID().toString();
        StorageReference fileReference = storageReference.child(fileName);
        fileReference.putFile(imgUri)
                .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl()
                        .addOnSuccessListener(uri -> {
                            documentReference.update(KEY_IMAGE, uri.toString());
                            activity.startActivity(new Intent(activity, MainActivity.class));
                            activity.finish();
                            spinnerDialog.dismiss();
                        })
                        .addOnFailureListener(e -> Log.e(STORAGE_TAG, e.getMessage())))
                .addOnFailureListener(e -> Log.e(STORAGE_TAG, e.getMessage()));
    }

    @SuppressLint("NotifyDataSetChanged")
    public void displayToddlerList(ArrayList<Toddler> toddlerList, ToddlerListAdapter toddlerListAdapter) {
        firestore.collection("User")
                .document(userUID)
                .collection("Toddler")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.e(FIRESTORE_TAG, error.getMessage());
                    } else {
                        assert value != null;
                        for (DocumentChange change : value.getDocumentChanges()) {
                            toddlerList.add(change.getDocument().toObject(Toddler.class));
                        }
                        toddlerListAdapter.notifyDataSetChanged();
                    }
                });
    }

    public void getCurrentToddler(OnRetrievePosition onRetrievePosition) {
        String documentPath = String.format("User/%s", userUID);
        DocumentReference documentUser = firestore.document(documentPath);
        documentUser.get().addOnSuccessListener(documentSnapshot -> onRetrievePosition.onRetrieve((Objects.requireNonNull(documentSnapshot.getLong("currentToddler"))).intValue()));
    }

    public void updateCurrentToddler(int currentPosition) {
        String documentPath = String.format("User/%s", userUID);
        DocumentReference documentUser = firestore.document(documentPath);
        documentUser.update("currentToddler", currentPosition);
        Log.e(FIRESTORE_TAG, "Update current toddler => " + currentPosition);
    }

    public void updateScore(Context context, int monthIndex, ArrayList<Integer> scoreSet) {
        getCurrentToddler(currentPosition -> {
            String collectionPath = String.format("User/%s/Toddler/%03d/Milestone", userUID, currentPosition);
            Score score = new Score(scoreSet);
            CollectionReference collectionMilestone = FirebaseFirestore.getInstance().collection(collectionPath);
            collectionMilestone.document(String.valueOf(monthIndex)).set(score)
                    .addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT));
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void displayMilestoneList(ArrayList<Score> scoreSet, MilestoneListAdapter milestoneListAdapter) {
        getCurrentToddler(currentPosition -> {
            String collectionPath = String.format("User/%s/Toddler/%03d/Milestone", userUID, currentPosition);
            firestore.collection(collectionPath)
                    .addSnapshotListener((value, error) -> {
                        if (error != null) {
                            Log.e(FIRESTORE_TAG, error.getMessage());
                        } else {
                            assert value != null;
                            for (DocumentChange change : value.getDocumentChanges()) {
                                scoreSet.add(change.getDocument().toObject(Score.class));
                            }
                            milestoneListAdapter.notifyDataSetChanged();
                        }
                    });
        });
    }

    public void readMilestoneScore(Context context, Integer milestoneIndex, OnRetrieveScore onRetrieveScore) {
        getCurrentToddler(currentPosition -> {
            String documentPath = String.format("User/%s/Toddler/%03d/Milestone/%d", userUID, currentPosition, milestoneIndex);
            DocumentReference documentMilestone = firestore.document(documentPath);
            documentMilestone.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            List<Integer> scoreSet = (List<Integer>) documentSnapshot.get("scoreSet");
                            onRetrieveScore.onRetrieve(scoreSet);
                        }
                    }).addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());
        });
    }

    @SuppressLint("DefaultLocale")
    public void getMilestoneIndex(OnRetrieveMilestone onRetrieveMilestone) {
        getCurrentToddler(currentPosition -> {
            String collectionPath = String.format("User/%s/Toddler/%03d/Milestone", userUID, currentPosition);
            CollectionReference collectionMilestone = FirebaseFirestore.getInstance().collection(collectionPath);
            collectionMilestone.get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            ArrayList<Integer> milestoneIndex = new ArrayList<>();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                milestoneIndex.add(Integer.parseInt(documentSnapshot.getId()));
                            }
                            onRetrieveMilestone.onRetrieve(milestoneIndex);
                        }
                    });
        });
    }

    public void addFeedback(Context context, String feedbackContent, String feedRating){
        Feedback feedback = new Feedback(feedbackContent, feedRating);
        String documentPath = String.format("Feedback/%s", userUID);
        DocumentReference documentFeedback = firestore.document(documentPath);
        documentFeedback.set(feedback)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(context, "回饋已送出！", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public void checkIndex(GetIndex getIndex) {
        firestore.collection("User")
                .document(userUID)
                .collection("Toddler")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        index = querySnapshot.size();
                        getIndex.onCallback(index);
                    } else {
                        Log.e(FIRESTORE_TAG, "Get index failure");
                    }
                })
                .addOnFailureListener(e -> Log.e(FIRESTORE_TAG, e.getMessage()));
    }

    public void setSpinnerDialog(SpinnerDialog spinnerDialog) {
        this.spinnerDialog = spinnerDialog;
    }
}
