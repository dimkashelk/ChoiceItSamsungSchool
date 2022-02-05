package com.example.choiceitsamsungschool.main_page;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.choiceitsamsungschool.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;

public class HomePage extends Fragment {
    private View home_page;
    private BottomSheetBehavior sheetBehavior;
    private MaterialButton button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        home_page = inflater.inflate(R.layout.home_page, container, false);
        Context context = getContext();

        Toast.makeText(context, "Получилось вызвать функцию", Toast.LENGTH_LONG).show();

        button = home_page.findViewById(R.id.home_page_button);
        LinearLayout contentLayout = home_page.findViewById(R.id.home_page_front);

        sheetBehavior = BottomSheetBehavior.from(contentLayout);
        sheetBehavior.setFitToContents(false);
        sheetBehavior.setHideable(false);//prevents the bottom sheet from completely hiding off the screen
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);//initially state to fully expanded

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFilters();
            }
        });
        return home_page;
    }

    private void toggleFilters() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }
}
