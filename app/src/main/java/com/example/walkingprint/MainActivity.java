package com.example.walkingprint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    AFragment aFragment;
    BFragment bFragment;
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aFragment = new AFragment();
        bFragment = new BFragment();

        currentFragment = aFragment;

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, currentFragment)
                .commit();
    }

    public void onFragmentChanged(int index) {
        Fragment newFragment;

        if (index == 0) {
            newFragment = aFragment;
        } else if (index == 1) {
            newFragment = bFragment;
        } else {
            return; // 유효하지 않은 index 값이면 종료
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, newFragment)
                .commit();

        currentFragment = newFragment;
    }
}
