package com.example.choiceitsamsungschool.main_page;

import android.util.Log;
import android.view.View;

import com.example.choiceitsamsungschool.MainActivity;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.concurrent.TimeUnit;

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
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (direction == Direction.Right) {
                    switch (type) {
                        case 1:
                            if (!surveyPage.isSwiped_second()) {
                                MainActivity.get().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        surveyPage.changeStateFirst();
                                        surveyPage.closeSecond();
                                        surveyPage.nextProgress();
                                    }
                                });
                                try {
                                    TimeUnit.SECONDS.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                MainActivity.get().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        surveyPage.changeStateFirst();
                                    }
                                });
                            }
                            break;
                        case 2:
                            if (!surveyPage.isSwiped_first()) {
                                MainActivity.get().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        surveyPage.changeStateSecond();
                                        surveyPage.closeFirst();
                                        surveyPage.nextProgress();
                                    }
                                });
                                try {
                                    TimeUnit.SECONDS.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                MainActivity.get().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        surveyPage.changeStateSecond();
                                    }
                                });
                            }
                            break;
                    }
                } else {
                    switch (type) {
                        case 1:
                            if (!surveyPage.isSwiped_second()) {
                                MainActivity.get().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        surveyPage.changeStateFirst();
                                        surveyPage.chooseSecond();
                                        surveyPage.nextProgress();
                                    }
                                });
                                try {
                                    TimeUnit.SECONDS.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                MainActivity.get().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        surveyPage.changeStateFirst();
                                    }
                                });
                            }
                            break;
                        case 2:
                            if (!surveyPage.isSwiped_first()) {
                                MainActivity.get().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        surveyPage.changeStateSecond();
                                        surveyPage.chooseFirst();
                                        surveyPage.nextProgress();
                                    }
                                });
                                try {
                                    TimeUnit.SECONDS.sleep(1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                MainActivity.get().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        surveyPage.changeStateSecond();
                                    }
                                });
                            }
                            break;
                    }
                }
            }
        });
        thread.start();
//        if (direction == Direction.Right) {
//            surveyPage.nextProgress();
//            switch (type) {
//                case 1:
//                    if (!surveyPage.isSwiped_second()) {
//                        surveyPage.changeStateFirst();
//                        surveyPage.closeSecond();
//                        surveyPage.changeStateFirst();
//                    }
//                    break;
//                case 2:
//                    if (!surveyPage.isSwiped_first()) {
//                        surveyPage.changeStateFirst();
//                        surveyPage.closeFirst();
//                        surveyPage.changeStateFirst();
//                    }
//                    break;
//            }
//        } else {
//            switch (type) {
//                case 1:
//                    if (!surveyPage.isSwiped_second()) {
//                        surveyPage.changeStateSecond();
//                        surveyPage.chooseSecond();
//                        surveyPage.changeStateSecond();
//                    }
//                    break;
//                case 2:
//                    if (!surveyPage.isSwiped_first()) {
//                        surveyPage.changeStateFirst();
//                        surveyPage.chooseFirst();
//                        surveyPage.changeStateFirst();
//                    }
//                    break;
//            }
//        }
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

    }
}
