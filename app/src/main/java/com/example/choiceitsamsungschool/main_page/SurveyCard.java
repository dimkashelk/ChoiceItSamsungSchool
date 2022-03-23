package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.choiceitsamsungschool.R;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SurveyCard extends View {
    private String survey_id;
    private Drawable image;
    private View page;
    private UserPage userPage;
    private MaterialButton like, favorite;
    private HomePage homePage;

    public SurveyCard(Context context, String survey_id, Drawable image, LayoutInflater inflater, UserPage userPage) {
        super(context);

        this.survey_id = survey_id;
        this.image = image;
        this.userPage = userPage;

        page = inflater.inflate(R.layout.survey_card, null);
        CircleImageView userImage = page.findViewById(R.id.survey_card_user_image);
        userImage.setImageDrawable(image);

        RoundedImageView surveyImage = page.findViewById(R.id.survey_card_image);
        surveyImage.setImageDrawable(image);

        like = page.findViewById(R.id.survey_card_like);
        favorite = page.findViewById(R.id.survey_card_favorite);
        page.setOnClickListener(v -> openSurvey());
    }

    public SurveyCard(Context context, String survey_id, Drawable image, LayoutInflater inflater, HomePage homePage) {
        super(context);

        this.survey_id = survey_id;
        this.image = image;
        this.homePage = homePage;

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
        Toast.makeText(getContext(), "Открыли страницу опроса", Toast.LENGTH_LONG).show();
    }

    public View getPage() {
        return page;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void setFavorite() {
        favorite.setIcon(getResources().getDrawable(R.drawable.ic_favourites_fill, null));
    }
}