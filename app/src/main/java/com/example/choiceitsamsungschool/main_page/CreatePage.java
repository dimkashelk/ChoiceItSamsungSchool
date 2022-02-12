package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.choiceitsamsungschool.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;

public class CreatePage extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static CreatePage page = null;
    private View create_page;
    private BottomSheetBehavior sheetBehavior;
    private MaterialButton button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (page == null) {
            create_page = inflater.inflate(R.layout.create_page, container, false);
            Context context = getContext();

            button = create_page.findViewById(R.id.create_page_button);
            LinearLayout contentLayout = create_page.findViewById(R.id.create_page_front);

            sheetBehavior = BottomSheetBehavior.from(contentLayout);
            sheetBehavior.setFitToContents(false);
            sheetBehavior.setHideable(false);
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeState();
                }
            });
            page = this;
            return create_page;
        } else {
            return page.getCreate_page();
        }
    }

    private void changeState() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public View getCreate_page() {
        return create_page;
    }
}