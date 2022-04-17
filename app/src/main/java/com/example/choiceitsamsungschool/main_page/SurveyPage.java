package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.animation.AccelerateInterpolator;
import androidx.fragment.app.Fragment;

import com.example.choiceitsamsungschool.APIServer;
import com.example.choiceitsamsungschool.MainActivity;
import com.example.choiceitsamsungschool.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;

import java.util.ArrayList;
import java.util.List;

public class SurveyPage extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static SurveyPage page = null;
    private static LayoutInflater inflater;
    private static APIServer apiServer;
    private View survey_page;
    private BottomSheetBehavior sheetBehavior;
    private MaterialToolbar toolbar;
    private AnimatedVectorDrawable menu;
    private AnimatedVectorDrawable close;
    private InputMethodManager manager;
    private TextView progress_title;
    private LinearProgressIndicator progress;
    private CardStackView cardStackViewFirst;
    private CardStackView cardStackViewSecond;
    private CardStackLayoutManager cardManagerFirst;
    private CardStackLayoutManager cardManagerSecond;
    private CardStackAdapter cardStackAdapterFirst;
    private CardStackAdapter cardStackAdapterSecond;
    private SwipeAnimationSetting left;
    private SwipeAnimationSetting right;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        survey_page = inflater.inflate(R.layout.survey_page, container, false);
        Context context = getContext();

        LinearLayout contentLayout = survey_page.findViewById(R.id.survey_page_front);

        sheetBehavior = BottomSheetBehavior.from(contentLayout);
        sheetBehavior.setFitToContents(false);
        sheetBehavior.setHideable(false);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        manager = (InputMethodManager) MainActivity.get().getSystemService(Activity.INPUT_METHOD_SERVICE);

        toolbar = survey_page.findViewById(R.id.survey_tool_bar);
        toolbar.setNavigationOnClickListener(v -> changeState());

        menu = (AnimatedVectorDrawable) context.getDrawable(R.drawable.ic_menu_animated);
        close = (AnimatedVectorDrawable) context.getDrawable(R.drawable.ic_close_animated);

        progress = survey_page.findViewById(R.id.survey_page_progress);
        progress_title = survey_page.findViewById(R.id.survey_page_progress_title);

        cardStackViewFirst = survey_page.findViewById(R.id.survey_page_first_layout);
        cardStackViewSecond = survey_page.findViewById(R.id.survey_page_second_layout);

        cardManagerFirst = new CardStackLayoutManager(getContext(), new MyCardStackListener(1, this));
        cardManagerSecond = new CardStackLayoutManager(getContext(), new MyCardStackListener(2, this));

        cardManagerFirst.setCanScrollHorizontal(true);
        cardManagerFirst.setCanScrollVertical(false);

        cardStackViewFirst.setLayoutManager(cardManagerFirst);
        cardStackViewSecond.setLayoutManager(cardManagerSecond);

        Drawable drawable = getContext().getDrawable(R.mipmap.ic_launcher);

        List<Spot> spots = new ArrayList<>();
        spots.add(new Spot(1, "Test", drawable));
        spots.add(new Spot(2, "Test", drawable));
        spots.add(new Spot(3, "Test", drawable));
        spots.add(new Spot(4, "Test", drawable));
        spots.add(new Spot(5, "Test", drawable));
        spots.add(new Spot(6, "Test", drawable));
        spots.add(new Spot(7, "Test", drawable));
        spots.add(new Spot(8, "Test", drawable));
        spots.add(new Spot(9, "Test", drawable));
        spots.add(new Spot(10, "Test", drawable));

        cardStackAdapterFirst = new CardStackAdapter(spots);
        cardStackAdapterSecond = new CardStackAdapter(spots);

        cardStackViewFirst.setAdapter(cardStackAdapterFirst);
        cardStackViewSecond.setAdapter(cardStackAdapterSecond);

        right = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new android.view.animation.AccelerateInterpolator())
                .build();

        left = new SwipeAnimationSetting.Builder()
                .setDirection(Direction.Left)
                .setDuration(Duration.Normal.duration)
                .setInterpolator(new android.view.animation.AccelerateInterpolator())
                .build();

        page = this;
        return survey_page;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void changeState() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            toolbar.setNavigationIcon(menu);
            menu.start();
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            toolbar.setNavigationIcon(close);
            close.start();
        }
        manager.hideSoftInputFromWindow(page.getView().getWindowToken(), 0);
    }

    public void chooseFirst() {
        cardManagerFirst.setSwipeAnimationSetting(right);
        cardStackViewFirst.swipe();
    }

    public void chooseSecond() {
        cardManagerSecond.setSwipeAnimationSetting(right);
        cardStackViewSecond.swipe();
    }

    public void closeFirst() {
        cardManagerFirst.setSwipeAnimationSetting(left);
        cardStackViewFirst.swipe();
    }

    public void closeSecond() {
        cardManagerSecond.setSwipeAnimationSetting(left);
        cardStackViewSecond.swipe();
    }
}
