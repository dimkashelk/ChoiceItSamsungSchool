package com.example.choiceitsamsungschool.main_page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.choiceitsamsungschool.R;

import java.util.Vector;

public class UserPageArchive extends View {
    private static View page = null;

    public UserPageArchive(Context context) {
        super(context);
    }

    public static View get(Context context, LayoutInflater inflater, ViewGroup container) {
        if (page == null) {
            View view = inflater.inflate(R.layout.user_page_archive, container, false);
            ViewGroup parent_archive_survey = (ViewGroup) view.findViewById(R.id.user_page_archive_list);

            Vector<SurveyCard> archive_surveys = UserPage.getArchive_surveys();
            if (archive_surveys.size() != 0) {
                parent_archive_survey.removeAllViews();
                for (int i = 0; i < 10; i++) {
                    parent_archive_survey.addView(new SurveyCard(context, String.valueOf(i), context.getDrawable(R.mipmap.ic_launcher), inflater, null).getPage());
                }
            }
            page = view;
        }
        return page;
    }
}
