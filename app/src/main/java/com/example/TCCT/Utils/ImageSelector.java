package com.example.TCCT.Utils;

import android.net.Uri;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.ActivityResultRegistry;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.material.imageview.ShapeableImageView;

public class ImageSelector implements DefaultLifecycleObserver {

    final ActivityResultRegistry resultRegistry;
    Uri imgUri;
    ActivityResultLauncher<String> resultLauncher;
    ShapeableImageView imageView;

    public ImageSelector(@NonNull ActivityResultRegistry resultRegistry) {
        this.resultRegistry = resultRegistry;
    }

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        resultLauncher = resultRegistry.register("KEY", owner,
                new ActivityResultContracts.GetContent(),
                uri -> {
                    imgUri = uri;
                    imageView.setImageURI(uri);
                });
    }

    public void selectImage(ShapeableImageView imageView) {
        this.imageView = imageView;
        resultLauncher.launch("image/*");
    }

    public Uri getImageUri() {
        return imgUri;
    }
}
