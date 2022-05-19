package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

import com.example.choiceitsamsungschool.R;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;

public class HistoryLine extends View {
    public static final String LEFT = "left";
    public static final String RIGHT = "right";

    private View page;
    private RoundedImageView left;
    private RoundedImageView right;
    private SurveyPage surveyPage;
    private MaterialButton left_res;
    private MaterialButton right_res;

    @SuppressLint("UseCompatLoadingForDrawables")
    public HistoryLine(Context context, Drawable left, Drawable right, String winner, LayoutInflater inflater, SurveyPage surveyPage) {
        super(context);

        this.surveyPage = surveyPage;

        page = inflater.inflate(R.layout.history_line, null);

        this.left = page.findViewById(R.id.history_line_left_image);
        this.left.setImageDrawable(left);
        left_res = page.findViewById(R.id.history_line_left_res);

        this.right = page.findViewById(R.id.history_line_right_image);
        this.right.setImageDrawable(right);
        right_res = page.findViewById(R.id.history_line_right_res);

        if (winner.equals(LEFT)) {
            left_res.setIcon(getResources().getDrawable(R.drawable.ic_done, null));
            right_res.setIcon(getResources().getDrawable(R.drawable.ic_clear, null));
        } else {
            left_res.setIcon(getResources().getDrawable(R.drawable.ic_clear, null));
            right_res.setIcon(getResources().getDrawable(R.drawable.ic_done, null));
        }
    }

    public View getPage() {
        return page;
    }
}
