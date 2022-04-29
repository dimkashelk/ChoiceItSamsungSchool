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
        switch (type) {
            case 1:
                surveyPage.freezeSecond();
                break;
            case 2:
                surveyPage.freezeFirst();
                break;
        }
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
                                        surveyPage.freezeCardViews();
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
                                        surveyPage.freezeCardViews();
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
                                        surveyPage.freezeCardViews();
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
                                        surveyPage.freezeCardViews();
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
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {
        switch (type) {
            case 1:
                surveyPage.unfreezeSecond();
                break;
            case 2:
                surveyPage.unfreezeFirst();
                break;
        }
    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {
        switch (type) {
            case 1:
                surveyPage.unfreezeSecond();
                break;
            case 2:
                surveyPage.unfreezeFirst();
                break;
        }
    }
}
