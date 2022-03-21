package com.example.choiceitsamsungschool.main_page;

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

public class UserPageArchive extends View {
    private static View page = null;
    private static ViewGroup parent_archive_survey = null;
    private static List<Survey> surveys = new Vector<>();
    private static LayoutInflater inflater;

    public UserPageArchive(Context context) {
        super(context);
    }

    public static View get(Context context, LayoutInflater inflater, ViewGroup container) {
        if (page == null) {
            UserPageArchive.inflater = inflater;
            View view = inflater.inflate(R.layout.user_page_archive, container, false);
            parent_archive_survey = (ViewGroup) view.findViewById(R.id.user_page_archive_list);

            AppDatabase appDatabase = AppDatabase.getDatabase(context);
            surveys = appDatabase.surveyDao().getArchives();
            if (surveys.size() != 0) {
                parent_archive_survey.removeAllViews();
                for (int i = 0; i < surveys.size(); i++) {
                    parent_archive_survey.addView(new SurveyCard(
                            context,
                            surveys.get(i).survey_id,
                            InternalStorage.getInternalStorage().load(
                                    surveys.get(i).survey_id,
                                    InternalStorage.SURVEY_TITLE_IMAGE
                            ),
                            inflater,
                            null).getPage());
                }
            }
            page = view;
        }
        return page;
    }

    public static void updateArchive() {
        AppDatabase appDatabase = AppDatabase.getDatabase(page.getContext());
        surveys = appDatabase.surveyDao().getFavorites();

        if (surveys.size() != 0 && parent_archive_survey != null) {
            parent_archive_survey.removeAllViews();
            for (int i = 0; i < surveys.size(); i++) {
                parent_archive_survey.addView(new SurveyCard(
                        page.getContext(),
                        surveys.get(i).survey_id,
                        InternalStorage.getInternalStorage().load(
                                surveys.get(i).survey_id,
                                InternalStorage.SURVEY_TITLE_IMAGE
                        ),
                        inflater,
                        null).getPage());
            }
        }
    }
}
