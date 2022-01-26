package com.example.TCCT.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.TCCT.R;
import com.google.android.material.button.MaterialButton;

public class Chatbot extends Fragment {

    private static final String INDEX = "index";
    private Integer position;

    public Chatbot() {
        // Required empty public constructor
    }

    public static Chatbot newInstance(Integer position) {
        Chatbot fragment = new Chatbot();
        Bundle args = new Bundle();
        args.putInt(INDEX, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chatbot, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialButton btnChatbot = view.findViewById(R.id.btnChatbot);

        btnChatbot.setOnClickListener(view1 -> {
            Uri chatbotUri = Uri.parse("https://liff.line.me/1645278921-kWRPP32q?accountId=273uocbo&openerPlatform=native&openerKey=home%3Arecommend#mst_challenge=8FTBM36G5I7dDVzu7ks4dO9ctX5oZPVyk6ncbXg8tjU");
            Intent chatbot = new Intent(Intent.ACTION_VIEW, chatbotUri);
            requireActivity().startActivity(chatbot);
        });
    }
}