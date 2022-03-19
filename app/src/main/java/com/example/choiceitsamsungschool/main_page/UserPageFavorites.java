package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.viewpager.widget.ViewPager;

import com.example.choiceitsamsungschool.R;

import java.util.Vector;

public class UserPageFavorites extends View {
    private static View page;

    public UserPageFavorites(Context context) {
        super(context);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceAsColor"})
    public static View get(Context context, LayoutInflater inflater, ViewGroup container) {
        if (page == null) {
            View view = inflater.inflate(R.layout.user_page_favorites, container, false);

            ViewGroup parent_favorites_survey = (ViewGroup) view.findViewById(R.id.user_page_favorites_list);

            Vector<SurveyCard> cards = UserPage.getFavorites_surveys();
            if (cards.size() != 0) {
                parent_favorites_survey.removeAllViews();
                for (int i = 0; i < 5; i++) {
                    parent_favorites_survey.addView(new SurveyCard(context, String.valueOf(i), context.getDrawable(R.mipmap.ic_launcher), inflater, null).getPage());
                }
            }
            page = view;
        }
        return page;
    }
}
