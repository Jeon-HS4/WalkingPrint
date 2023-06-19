package com.example.walkingprint;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class AFragment extends Fragment {

    Button walkStartButton;
    Button recordButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_a, container, false);

        walkStartButton = view.findViewById(R.id.button);
        recordButton = view.findViewById(R.id.button2);

        walkStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 걷기 시작 버튼 클릭 시 B 프래그먼트로 전환
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new BFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 기록 보기 버튼 클릭 시 C 프래그먼트로 전환
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, new CFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}