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
import com.makeramen.roundedimageview.RoundedImageView;

public class UserPage extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static UserPage page = null;
    private View user_page;
    private BottomSheetBehavior sheetBehavior;
    private MaterialButton button;
    private LinearLayout friends;

    @Nullable
    @Override
    @SuppressLint({"CutPasteId", "UseCompatLoadingForDrawables"})
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (page == null) {
            user_page = inflater.inflate(R.layout.user_page, container, false);
            Context context = getContext();

            button = user_page.findViewById(R.id.user_page_button);
            LinearLayout contentLayout = user_page.findViewById(R.id.user_page_front);
            friends = user_page.findViewById(R.id.user_page_friends_list);

            ViewGroup parent = (ViewGroup) user_page.findViewById(R.id.user_page_friends_list);

            for (int i = 0; i < 10; i++) {
                parent.addView(new FriendCard(context, String.valueOf(i), context.getDrawable(R.mipmap.ic_launcher), inflater, null).getPage());
            }

            RoundedImageView imageView = user_page.findViewById(R.id.user_page_image);
            imageView.setImageDrawable(context.getDrawable(R.mipmap.ic_launcher));

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
            return user_page;
        } else {
            return page.getUser_page();
        }
    }

    private void changeState() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    public View getUser_page() {
        return user_page;
    }
}