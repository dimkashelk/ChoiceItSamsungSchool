package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

import com.example.choiceitsamsungschool.AppDatabase;
import com.example.choiceitsamsungschool.R;
import com.example.choiceitsamsungschool.db.Survey;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SurveyCard extends View {
    private String survey_id;
    private Drawable image;
    private View page;
    private MaterialButton like, favorite;

    public SurveyCard(Context context, String survey_id, Drawable image, LayoutInflater inflater) {
        super(context);

        this.survey_id = survey_id;
        this.image = image;

        page = inflater.inflate(R.layout.survey_card, null);
        CircleImageView userImage = page.findViewById(R.id.survey_card_user_image);
        userImage.setImageDrawable(image);

        RoundedImageView surveyImage = page.findViewById(R.id.survey_card_image);
        surveyImage.setImageDrawable(image);

        like = page.findViewById(R.id.survey_card_like);
        favorite = page.findViewById(R.id.survey_card_favorite);
        page.setOnClickListener(v -> openSurvey());
    }

    public void openSurvey() {
        AppDatabase appDatabase = AppDatabase.getDatabase(getContext());
        List<Survey> survey = appDatabase.surveyDao().getSurvey(survey_id);
        NavigationItemListener.get().openSurvey(survey.get(0));
    }

    public View getPage() {
        return page;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setFavorite() {
        favorite.setIcon(getResources().getDrawable(R.drawable.ic_favourites_fill, null));
    }
}