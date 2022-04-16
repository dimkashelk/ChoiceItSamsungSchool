package com.example.choiceitsamsungschool.main_page;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;

public class MyCardStackListener implements CardStackListener {
    private int type;
    private SurveyPage surveyPage;

    public MyCardStackListener(int position, SurveyPage surveyPage) {
        this.type = position;
        this.surveyPage = surveyPage;
    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {
        Log.i("Stack", "dragging");
    }

    @Override
    public void onCardSwiped(Direction direction) {
        Log.i("Stack", "swiped");
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
        switch (type) {
            case 1:
                surveyPage.swipeSecond();
                break;
            case 2:
                surveyPage.swipeFirst();
                break;
        }
    }
}
