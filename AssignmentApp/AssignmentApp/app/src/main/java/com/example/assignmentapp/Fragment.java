package com.example.assignmentapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

public class Fragment extends androidx.fragment.app.Fragment {
    String getText;
    TextView tvf;
    int color;

    Random rnd = new Random();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //tvf=(TextView) rootView.findViewById(R.id.textViewF);
        View rootView = inflater.inflate(R.layout.activity_fragment, container, false);
        color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        rootView.setBackgroundColor(color);

        return rootView;
    }
    public void getTextandColor()
    {

    }
}