package com.example.choiceitsamsungschool.main_page;

import android.view.View;

import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;

public class MyCardStackListener implements CardStackListener {
    private int type;
    private SurveyPage surveyPage;
    private Direction direction;

    public MyCardStackListener(int position, SurveyPage surveyPage) {
        this.type = position;
        this.surveyPage = surveyPage;
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {
        this.direction = direction;
    }

    @Override
    public void onCardSwiped(Direction direction) {
        this.direction = direction;
        if (direction == Direction.Right) {
            surveyPage.nextProgress();
        }
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {
        if (direction == Direction.Right) {
            switch (type) {
                case 1:
                    surveyPage.closeSecond();
                    break;
                case 2:
                    surveyPage.closeFirst();
                    break;
            }
        } else {
            switch (type) {
                case 1:
                    surveyPage.chooseSecond();
                    break;
                case 2:
                    surveyPage.chooseFirst();
                    break;
            }
        }
    }
}
