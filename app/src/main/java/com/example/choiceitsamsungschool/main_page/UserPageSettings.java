package com.example.choiceitsamsungschool.main_page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.choiceitsamsungschool.R;

public class UserPageSettings extends View {
    private static View page;

    public UserPageSettings(Context context) {
        super(context);
    }

    public static View get(Context context, LayoutInflater inflater, ViewGroup container) {
        if (page == null) {
            View view = inflater.inflate(R.layout.user_page_settings, container, false);
            page = view;
        }
        return page;
    }
}
