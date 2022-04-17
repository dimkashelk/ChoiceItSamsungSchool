package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    private int index_of_spot = 0;
    private int count_questions = 15;
    private int current_question = 1;
    private int step = (int) (current_question * 1.0 / count_questions * 100);
    private int current_progress = step;
    private List<Spot> begin_list_spots = new ArrayList<>();
    private List<Spot> dop_spots = new ArrayList<>();
    private List<Spot> all_spots = new ArrayList<>();
    private List<Spot> first = new ArrayList<>();
    private List<Spot> second = new ArrayList<>();
    private boolean is_finished = false;

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
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
        all_spots.add(new Spot(0, "Test", drawable));
        all_spots.add(new Spot(1, "Test", drawable));
        all_spots.add(new Spot(2, "Test", drawable));
        all_spots.add(new Spot(3, "Test", drawable));
        all_spots.add(new Spot(4, "Test", drawable));
        all_spots.add(new Spot(5, "Test", drawable));
        all_spots.add(new Spot(6, "Test", drawable));
        all_spots.add(new Spot(7, "Test", drawable));
        all_spots.add(new Spot(8, "Test", drawable));
        all_spots.add(new Spot(9, "Test", drawable));
        all_spots.add(new Spot(10, "Test", drawable));
        all_spots.add(new Spot(11, "Test", drawable));
        all_spots.add(new Spot(13, "Test", drawable));
        all_spots.add(new Spot(14, "Test", drawable));
        all_spots.add(new Spot(15, "Test", drawable));
        all_spots.add(new Spot(16, "Test", drawable));

        int count = all_spots.size() / 2;
        first = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            first.add(all_spots.get(i));
        }
        cardStackAdapterFirst = new CardStackAdapter(first);

        second = new ArrayList<>();
        for (int i = count; i < 2 * count; i++) {
            second.add(all_spots.get(i));
        }
        cardStackAdapterSecond = new CardStackAdapter(second);

        for (int i = 2 * count; i < all_spots.size(); i++) {
            dop_spots.add(all_spots.get(i));
        }

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

        progress.setProgress(current_progress);
        progress_title.setText(current_question + "/" + count_questions);
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
        if (index_of_spot < first.size()) {
            dop_spots.add(first.get(index_of_spot));
        }
    }

    public void chooseSecond() {
        cardManagerSecond.setSwipeAnimationSetting(right);
        cardStackViewSecond.swipe();
        if (index_of_spot < second.size()) {
            dop_spots.add(second.get(index_of_spot));
        }
    }

    public void closeFirst() {
        cardManagerFirst.setSwipeAnimationSetting(left);
        cardStackViewFirst.swipe();
    }

    public void closeSecond() {
        cardManagerSecond.setSwipeAnimationSetting(left);
        cardStackViewSecond.swipe();
    }

    @SuppressLint("SetTextI18n")
    public void nextProgress() {
        index_of_spot++;
        if (index_of_spot == first.size() || index_of_spot == second.size()) {
            updateStacks();
        }

        if (!is_finished) {
            current_question += 1;
            if (current_question == count_questions) {
                current_progress = 100;
            } else {
                current_progress += step;
            }
            progress_title.setText(current_question + "/" + count_questions);
            progress.setProgress(current_progress, true);
        }
    }

    public void updateStacks() {
        if (dop_spots.size() == 1) {
            finishSurvey();
            return;
        }

        index_of_spot = 0;
        int count = dop_spots.size() / 2;

        first = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            first.add(dop_spots.get(i));
            cardStackAdapterFirst.addSpot(dop_spots.get(i));
        }

        second = new ArrayList<>();
        for (int i = count; i < 2 * count; i++) {
            second.add(dop_spots.get(i));
            cardStackAdapterSecond.addSpot(dop_spots.get(i));
        }

        List<Spot> dop = new ArrayList<>();
        for (int i = 2 * count; i < dop_spots.size(); i++) {
            dop.add(dop_spots.get(i));
        }

        dop_spots = new ArrayList<>();
        dop_spots.addAll(dop);
    }

    public void finishSurvey() {
        is_finished = true;
        Toast.makeText(getContext(), "ФИНИШ!", Toast.LENGTH_SHORT).show();
    }
}
