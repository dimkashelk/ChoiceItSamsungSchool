package com.example.choiceitsamsungschool.main_page;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.choiceitsamsungschool.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class SurveyCard extends View {
    private String survey_id;
    private Drawable image;
    private View page;
    private UserPage userPage;

    public SurveyCard(Context context, String survey_id, Drawable image, LayoutInflater inflater, UserPage userPage) {
        super(context);

        this.survey_id = survey_id;
        this.image = image;
        this.userPage = userPage;

        page = inflater.inflate(R.layout.survey_card, null);
        RoundedImageView userImage = page.findViewById(R.id.survey_card_user_image);
        userImage.setImageDrawable(image);

        RoundedImageView surveyImage = page.findViewById(R.id.survey_card_image);
        surveyImage.setImageDrawable(image);

        setOnClickListener(v -> openSurvey());
    }

    private void openSurvey() {
        Toast.makeText(getContext(), "Открыли страницу опроса", Toast.LENGTH_LONG).show();
    }

    public View getPage() {
        return page;
    }
}