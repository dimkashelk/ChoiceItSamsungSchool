package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;

import androidx.fragment.app.Fragment;

import com.example.choiceitsamsungschool.MainActivity;
import com.example.choiceitsamsungschool.R;

public class HomePageFragment extends Fragment {
    private MainActivity mainActivity;

    HomePageFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @SuppressLint("ResourceType")
    public void setContentToThis() {
        mainActivity.setContentView(R.id.home_page);
    }
}
