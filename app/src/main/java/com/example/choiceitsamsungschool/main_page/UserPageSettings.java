package com.example.choiceitsamsungschool.main_page;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.choiceitsamsungschool.AppDatabase;
import com.example.choiceitsamsungschool.R;
import com.example.choiceitsamsungschool.db.User;
import com.google.android.material.textfield.TextInputEditText;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


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

            page.findViewById(R.id.settings_page_edit_profile_image).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    userPage.startChooseImage();
                }
            });

            page.findViewById(R.id.settings_page_reset).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetAll();
                }
            });
        }
        return page;
    }

    private static void resetAll() {
        if (page == null) {
            return;
        }
        List<User> user = AppDatabase.getDatabase(page.getContext()).userDao().getAllUsers();
        User current_user = user.get(0);

        TextInputEditText first_name = page.findViewById(R.id.settings_page_edit_first_name);
        TextInputEditText second_name = page.findViewById(R.id.settings_page_edit_second_name);
        TextInputEditText login_name = page.findViewById(R.id.settings_page_edit_login);

        first_name.setText(current_user.first_name);
        second_name.setText(current_user.second_name);
        login_name.setText(current_user.login);

        TextInputEditText old_password = page.findViewById(R.id.settings_page_edit_old_password);
        TextInputEditText new_password = page.findViewById(R.id.settings_page_edit_password);
        TextInputEditText re_new_password = page.findViewById(R.id.settings_page_edit_re_password);

        old_password.setText("");
        new_password.setText("");
        re_new_password.setText("");
    }

    public static void changeUserImage(Bitmap image) {
        if (page == null) {
            return;
        }
        CircleImageView circularImageView = page.findViewById(R.id.settings_page_edit_profile_image);
        circularImageView.setImageDrawable(new BitmapDrawable(null, image));
    }
}