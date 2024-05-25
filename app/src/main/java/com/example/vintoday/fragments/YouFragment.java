package com.example.vintoday.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.vintoday.R;
import com.example.vintoday.SavedListActivity;
import com.example.vintoday.utils.LanguageUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class YouFragment extends Fragment {

    TextView greetings;
    LinearLayout logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_you, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
;
        String firstName = user.getDisplayName().split(" ")[0];

        greetings = view.findViewById(R.id.you_greetings);
        String hello = getString(R.string.hello);
        greetings.setText(hello + ", " + firstName + "!");

        logout = view.findViewById(R.id.ll_saved_news);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SavedListActivity.class);
                startActivity(intent);
            }
        });

    }
}