package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.choiceitsamsungschool.AppDatabase;
import com.example.choiceitsamsungschool.InternalStorage;
import com.example.choiceitsamsungschool.R;
import com.example.choiceitsamsungschool.db.Survey;

import java.util.List;
import java.util.Vector;

public class UserPageFavorites extends View {
    private static View page;
    private static ViewGroup parent_favorites_survey = null;
    private static List<Survey> surveys = new Vector<>();
    private static LayoutInflater inflater;

    public UserPageFavorites(Context context) {
        super(context);
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceAsColor"})
    public static View get(Context context, LayoutInflater inflater, ViewGroup container) {
        UserPageFavorites.inflater = inflater;
        View view = inflater.inflate(R.layout.user_page_favorites, container, false);

        parent_favorites_survey = (ViewGroup) view.findViewById(R.id.user_page_favorites_list);

        if (surveys.size() != 0) {
            parent_favorites_survey.removeAllViews();
            for (int i = 0; i < surveys.size(); i++) {
                parent_favorites_survey.addView(new SurveyCard(
                        context,
                        surveys.get(i).survey_id,
                        InternalStorage.getInternalStorage().load(
                                surveys.get(i).survey_id,
                                InternalStorage.SURVEY_TITLE_IMAGE
                        ),
                        inflater).getPage());
            }
        }
        page = view;
        return page;
    }

    public static void updateFavorites() {
        AppDatabase appDatabase = AppDatabase.getDatabase(page.getContext());
        surveys = appDatabase.surveyDao().getFavorites();

        if (surveys.size() != 0 && parent_favorites_survey != null) {
            parent_favorites_survey.removeAllViews();
            for (int i = 0; i < surveys.size(); i++) {
                parent_favorites_survey.addView(new SurveyCard(
                        page.getContext(),
                        surveys.get(i).survey_id,
                        InternalStorage.getInternalStorage().load(
                                surveys.get(i).survey_id,
                                InternalStorage.SURVEY_TITLE_IMAGE
                        ),
                        inflater).getPage());
            }
        }
    }
}
