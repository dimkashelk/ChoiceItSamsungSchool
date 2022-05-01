package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.choiceitsamsungschool.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class ResultLine extends View {
    private View page;
    private ResultLine resLine;
    private Context context;
    private TextView position;
    private TextView title;
    private SurveyPage surveyPage;
    private Spot spot;
    private RoundedImageView image;

    @SuppressLint("InflateParams")
    public ResultLine(Context context, Spot spot, String pos, LayoutInflater inflater, SurveyPage surveyPage) {
        super(context);

        this.spot = spot;
        this.surveyPage = surveyPage;
        this.resLine = this;
        this.context = context;

        page = inflater.inflate(R.layout.result_line, null);

        position = page.findViewById(R.id.result_line_position);
        position.setText(pos);

        image = page.findViewById(R.id.result_line_image);
        image.setImageDrawable(spot.getDrawable());

        title = page.findViewById(R.id.result_line_title);
        title.setText(spot.getTitle());
    }

    public View getPage() {
        return page;
    }
}
