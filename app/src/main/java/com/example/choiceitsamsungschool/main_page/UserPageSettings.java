package com.example.choiceitsamsungschool.main_page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.choiceitsamsungschool.R;

import gun0912.tedimagepicker.builder.TedImagePicker;

public class UserPageSettings extends View {
    private static View page;
    public static UserPage userPage;

    public UserPageSettings(Context context) {
        super(context);
    }

    public static View get(Context context, LayoutInflater inflater, ViewGroup container) {
        if (page == null) {
            View view = inflater.inflate(R.layout.user_page_settings, container, false);
            page = view;

            page.findViewById(R.id.settings_page_edit_profile_image_button).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    userPage.startChooseImage();
                }
            });
        }
        return page;
    }
}
