package com.example.choiceitsamsungschool.main_page;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
    }

    @Override
    public void onCardRewound() {
        Log.i("Stack", "rewound");
    }

    @Override
    public void onCardCanceled() {
        Log.i("Stack", "canceled");
    }

    @Override
    public void onCardAppeared(View view, int position) {
        Log.i("Stack", "appeared");
    }

    @Override
    public void onCardDisappeared(View view, int position) {
        Log.i("Stack", "disappeared");
        if (direction == Direction.Left) {
            switch (type) {
                case 1:
                    surveyPage.chooseSecond();
                    break;
                case 2:
                    surveyPage.chooseFirst();
                    break;
            }
        } else {
            switch (type) {
                case 1:
                    surveyPage.closeSecond();
                    break;
                case 2:
                    surveyPage.closeFirst();
                    break;
            }
        }
    }
}
