package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.choiceitsamsungschool.APIServer;
import com.example.choiceitsamsungschool.AppDatabase;
import com.example.choiceitsamsungschool.InternalStorage;
import com.example.choiceitsamsungschool.MainActivity;
import com.example.choiceitsamsungschool.R;
import com.example.choiceitsamsungschool.db.SpotDB;
import com.example.choiceitsamsungschool.db.Survey;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.snackbar.Snackbar;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.Duration;
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;

public class SurveyPage extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private Survey current_survey;
    private static SurveyPage page = null;
    private static LayoutInflater inflater;
    private static APIServer apiServer;
    private View survey_page;
    private BottomSheetBehavior sheetBehavior;
    private BottomSheetBehavior sheetBehaviorMain;
    private BottomSheetBehavior sheetBehaviorEnd;
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
    private List<Spot> dop_spots = new ArrayList<>();
    private List<Spot> all_spots = new ArrayList<>();
    private List<Spot> first = new ArrayList<>();
    private List<Spot> second = new ArrayList<>();
    private List<Spot> all_spots_list = new ArrayList<>();
    private boolean is_finished = false;
    private List<HistoryLine> history = new ArrayList<>();
    private ViewGroup history_group;
    private boolean swiped_first = false;
    private boolean swiped_second = false;
    private TextView description;
    private TextView count_questions_view;
    private CircularProgressButton start_survey;
    private MaterialButton reset_all;
    private MaterialButton end_survey;
    private MaterialButton close_survey;
    private HashMap<Spot, Integer> map_res = new HashMap<>();
    private HashMap<Integer, List<Spot>> map_res_dop = new HashMap<>();
    private ViewGroup listView;
    private SwipeRefreshLayout page_refresh;
    private SwipeRefreshLayout main_refresh;
    private SwipeRefreshLayout end_refresh;

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SurveyPage.inflater = inflater;

        survey_page = inflater.inflate(R.layout.survey_page, container, false);
        Context context = getContext();

        LinearLayout contentLayout = survey_page.findViewById(R.id.survey_page_front);

        sheetBehavior = BottomSheetBehavior.from(contentLayout);
        sheetBehavior.setFitToContents(false);
        sheetBehavior.setHideable(false);

        LinearLayout mainLayout = survey_page.findViewById(R.id.survey_page_main);
        sheetBehaviorMain = BottomSheetBehavior.from(mainLayout);
        sheetBehaviorMain.setFitToContents(false);
        sheetBehaviorMain.setHideable(false);
        sheetBehaviorMain.setState(BottomSheetBehavior.STATE_EXPANDED);

        LinearLayout endLayout = survey_page.findViewById(R.id.survey_page_end);
        sheetBehaviorEnd = BottomSheetBehavior.from(endLayout);
        sheetBehaviorEnd.setFitToContents(false);
        sheetBehaviorEnd.setHideable(false);

        manager = (InputMethodManager) MainActivity.get().getSystemService(Activity.INPUT_METHOD_SERVICE);

        toolbar = survey_page.findViewById(R.id.survey_tool_bar);

        menu = (AnimatedVectorDrawable) context.getDrawable(R.drawable.ic_menu_animated);
        close = (AnimatedVectorDrawable) context.getDrawable(R.drawable.ic_close_animated);

        progress = survey_page.findViewById(R.id.survey_page_progress);
        progress_title = survey_page.findViewById(R.id.survey_page_progress_title);

        cardStackViewFirst = survey_page.findViewById(R.id.survey_page_first_layout);
        cardStackViewSecond = survey_page.findViewById(R.id.survey_page_second_layout);

        cardManagerFirst = new CardStackLayoutManager(getContext(), new MyCardStackListener(1, this));
        cardManagerFirst.setSwipeThreshold(0.6f);
        cardManagerSecond = new CardStackLayoutManager(getContext(), new MyCardStackListener(2, this));
        cardManagerSecond.setSwipeThreshold(0.6f);

        cardManagerFirst.setCanScrollHorizontal(true);
        cardManagerFirst.setCanScrollVertical(false);

        cardStackViewFirst.setLayoutManager(cardManagerFirst);
        cardStackViewSecond.setLayoutManager(cardManagerSecond);

        Drawable drawable = getContext().getDrawable(R.mipmap.ic_launcher);
        all_spots.add(new Spot(0, "Test0", drawable));
        all_spots.add(new Spot(1, "Test1", drawable));
        all_spots.add(new Spot(2, "Test2", drawable));
        all_spots.add(new Spot(3, "Test3", drawable));
        all_spots.add(new Spot(4, "Test4", drawable));
        all_spots.add(new Spot(5, "Test5", drawable));
        all_spots.add(new Spot(6, "Test6", drawable));
        all_spots.add(new Spot(7, "Test7", drawable));
        all_spots.add(new Spot(8, "Test8", drawable));
        all_spots.add(new Spot(9, "Test9", drawable));
        all_spots.add(new Spot(10, "Test10", drawable));
        all_spots.add(new Spot(11, "Test11", drawable));
        all_spots.add(new Spot(12, "Test12", drawable));
        all_spots.add(new Spot(13, "Test13", drawable));
        all_spots.add(new Spot(14, "Test14", drawable));
        all_spots.add(new Spot(15, "Test15", drawable));

        all_spots_list.addAll(all_spots);

        int s = 1;
        for (Spot spot: all_spots_list) {
            map_res.put(spot, s);
            s += 1;
        }

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

        history_group = survey_page.findViewById(R.id.survey_page_history_list);

        description = survey_page.findViewById(R.id.survey_page_main_description);
        count_questions_view = survey_page.findViewById(R.id.survey_page_main_count_questions);
        start_survey = survey_page.findViewById(R.id.survey_page_start_survey);

        description.setText("Нет описанияНет описанияНет описанияНет описанияНет описанияНет описанияНет описанияНет описанияНет описанияНет описанияНет описанияНет описанияНет описанияНет описанияНет описанияНет описанияНет описанияНет описанияНет описанияНет описанияНет описания");
        count_questions_view.setText(String.valueOf(count_questions));
        start_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSurvey();
            }
        });

        reset_all = survey_page.findViewById(R.id.survey_page_reset_all);
        reset_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetAll();
            }
        });

        listView = survey_page.findViewById(R.id.survey_page_res_list);

        page_refresh = survey_page.findViewById(R.id.survey_page_refresh);
        main_refresh = survey_page.findViewById(R.id.survey_page_main_refresh);
        end_refresh = survey_page.findViewById(R.id.survey_page_end_refresh);

        page_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                page_refresh.setRefreshing(false);
            }
        });
        main_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                check();
                main_refresh.setRefreshing(false);
            }
        });
        end_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                end_refresh.setRefreshing(false);
            }
        });

        end_survey = survey_page.findViewById(R.id.survey_page_end_late_button);
        end_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endLate();
            }
        });

        close_survey = survey_page.findViewById(R.id.survey_page_close_survey);
        close_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                end();
            }
        });

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


    private void startSurvey() {
        sheetBehaviorMain.setState(BottomSheetBehavior.STATE_COLLAPSED);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeState();
            }
        });
    }

    public void chooseFirst() {
        Log.i("CardEvent", "Choose First");
        cardManagerFirst.setSwipeAnimationSetting(right);
        cardStackViewFirst.swipe();
        if (index_of_spot < first.size()) {
            if (history_group.getChildCount() != current_question) {
                dop_spots.add(first.get(index_of_spot));
                HistoryLine historyLine = new HistoryLine(
                        getContext(),
                        first.get(index_of_spot).getDrawable(),
                        second.get(index_of_spot).getDrawable(),
                        HistoryLine.LEFT,
                        inflater,
                        this
                );
                history_group.addView(historyLine.getPage());
            }
        }
    }

    public void chooseSecond() {
        Log.i("CardEvent", "Choose Second");
        cardManagerSecond.setSwipeAnimationSetting(right);
        cardStackViewSecond.swipe();
        if (index_of_spot < second.size()) {
            if (history_group.getChildCount() != current_question) {
                dop_spots.add(second.get(index_of_spot));
                HistoryLine historyLine = new HistoryLine(
                        getContext(),
                        first.get(index_of_spot).getDrawable(),
                        second.get(index_of_spot).getDrawable(),
                        HistoryLine.RIGHT,
                        inflater,
                        this
                );
                history_group.addView(historyLine.getPage());
            }
        }
    }

    public void closeFirst() {
        Log.i("CardEvent", "Close First");
        cardManagerFirst.setSwipeAnimationSetting(left);
        cardStackViewFirst.swipe();
        if (history_group.getChildCount() != current_question) {
            dop_spots.add(second.get(index_of_spot));
            HistoryLine historyLine = new HistoryLine(
                    getContext(),
                    first.get(index_of_spot).getDrawable(),
                    second.get(index_of_spot).getDrawable(),
                    HistoryLine.RIGHT,
                    inflater,
                    this
            );
            history_group.addView(historyLine.getPage());
        }
    }

    public void closeSecond() {
        Log.i("CardEvent", "Close Second");
        cardManagerSecond.setSwipeAnimationSetting(left);
        cardStackViewSecond.swipe();
        if (history_group.getChildCount() != current_question) {
            dop_spots.add(first.get(index_of_spot));
            HistoryLine historyLine = new HistoryLine(
                    getContext(),
                    first.get(index_of_spot).getDrawable(),
                    second.get(index_of_spot).getDrawable(),
                    HistoryLine.LEFT,
                    inflater,
                    this
            );
            history_group.addView(historyLine.getPage());
        }
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
        if (dop_spots.size() <= 1) {
            if (dop_spots.size() == 1) {
                map_res.put(dop_spots.get(0), map_res.get(dop_spots.get(0)) + 1);
            }
            finishSurvey();
            return;
        }

        index_of_spot = 0;
        int count = dop_spots.size() / 2;

        first = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            first.add(dop_spots.get(i));
            cardStackAdapterFirst.addSpot(dop_spots.get(i));
            map_res.put(dop_spots.get(i), map_res.get(dop_spots.get(i)) + 1);
        }

        second = new ArrayList<>();
        for (int i = count; i < 2 * count; i++) {
            second.add(dop_spots.get(i));
            cardStackAdapterSecond.addSpot(dop_spots.get(i));
            map_res.put(dop_spots.get(i), map_res.get(dop_spots.get(i)) + 1);
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
        toolbar.setNavigationOnClickListener(null);

        HashMap<Integer, List<Spot>> dop = new HashMap<>();
        for (Spot spot : map_res.keySet()) {
            int count_voice = AppDatabase.getDatabase(getContext()).spotDao().getSpotCountVoice(spot.id);
            if (!dop.containsKey(map_res.get(spot) + count_voice)) {
                dop.put(map_res.get(spot), new ArrayList<>());
            }
            dop.get(map_res.get(spot) + count_voice).add(spot);
        }

        ArrayList<Integer> keys = new ArrayList<>(dop.keySet());
        keys.sort((o1, o2) -> o2 - o1);

        int start = 1;

        for (Integer integer : keys) {
            boolean fl = true;
            for (Spot spot : dop.get(integer)) {
                if (fl) {
                    listView.addView(new ResultLine(
                            getContext(),
                            spot,
                            String.valueOf(start),
                            getLayoutInflater(),
                            this
                    ).getPage());
                    fl = false;
                    List<Spot> d = new ArrayList<>();
                    d.add(spot);
                    map_res_dop.put(start, d);
                } else {
                    listView.addView(new ResultLine(
                            getContext(),
                            spot,
                            "",
                            getLayoutInflater(),
                            this
                    ).getPage());
                    map_res_dop.get(start).add(spot);
                }
            }
            start++;
        }

        sheetBehaviorEnd.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public boolean isSwiped_first() {
        return swiped_first;
    }

    public void setSwiped_first(boolean swiped_first) {
        this.swiped_first = swiped_first;
    }

    public boolean isSwiped_second() {
        return swiped_second;
    }

    public void setSwiped_second(boolean swiped_second) {
        this.swiped_second = swiped_second;
    }

    public void changeStateFirst() {
        setSwiped_first(!isSwiped_first());
    }

    public void changeStateSecond() {
        setSwiped_second(!isSwiped_second());
    }

    public void freezeFirst() {
        cardManagerFirst.setSwipeableMethod(SwipeableMethod.Automatic);
    }

    public void unfreezeFirst() {
        cardManagerFirst.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
    }

    public void freezeFirstCardView() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        freezeFirst();
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
                        unfreezeFirst();
                    }
                });
            }
        });
        thread.start();
    }

    public void freezeSecond() {
        cardManagerSecond.setSwipeableMethod(SwipeableMethod.Automatic);
    }

    public void unfreezeSecond() {
        cardManagerSecond.setSwipeableMethod(SwipeableMethod.AutomaticAndManual);
    }

    public void freezeSecondCardView() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MainActivity.get().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        freezeSecond();
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
                        unfreezeSecond();
                    }
                });
            }
        });
        thread.start();
    }

    public void freezeCardViews() {
        freezeFirstCardView();
        freezeSecondCardView();
    }

    @SuppressLint("SetTextI18n")
    private void resetAll() {
        all_spots = new ArrayList<>();
        all_spots.addAll(all_spots_list);

        updateSpots();

        changeState();
    }

    private void refresh() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Начать сначала?");
        builder.setMessage("Хотите начать опрос сначала?");
        builder.setCancelable(true);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void check() {

    }

    public void setSurvey(Survey survey) {
        toolbar.setTitle(survey.title);
        description.setText(survey.description);

        current_survey = survey;
        List<SpotDB> spots = AppDatabase.getDatabase(getContext()).spotDao().getSpot(survey.survey_id);

        count_questions_view.setText(spots.size());

        InternalStorage internalStorage = InternalStorage.getInternalStorage();

        all_spots = new ArrayList<>();

        for (SpotDB spotDB : spots) {
            all_spots.add(new Spot(
                    spotDB.id,
                    spotDB.title,
                    internalStorage.load(spotDB.spot_id, InternalStorage.SPOT_IMAGE)
            ));
        }

        all_spots_list.addAll(all_spots);

        updateSpots();

        toolbar.setNavigationOnClickListener(null);
    }

    @SuppressLint("SetTextI18n")
    private void updateSpots() {
        int count = all_spots.size() / 2;

        cardStackAdapterFirst.clear();
        first = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            first.add(all_spots.get(i));
        }
        cardStackAdapterFirst.addAllSpot(first);

        cardStackAdapterSecond.clear();
        second = new ArrayList<>();
        for (int i = count; i < 2 * count; i++) {
            second.add(all_spots.get(i));
        }
        cardStackAdapterSecond.addAllSpot(second);

        for (int i = 2 * count; i < all_spots.size(); i++) {
            dop_spots.add(all_spots.get(i));
        }

        index_of_spot = 0;
        current_question = 1;
        step = (int) (current_question * 1.0 / count_questions * 100);
        current_progress = step;

        progress_title.setText(current_question + "/" + count_questions);
        progress.setProgress(current_progress, true);
    }

    public void endLate() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Закончить?");
        builder.setMessage("При завершении прогресс не сохранится. Закончить?");
        builder.setCancelable(true);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                NavigationItemListener.get().closeSurvey();
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                sheetBehaviorMain.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void end() {
        NavigationItemListener.get().closeSurvey();

        APIServer apiServer = APIServer.getSingletonAPIServer();
        apiServer.endSurvey(map_res_dop);

        sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        sheetBehaviorEnd.setState(BottomSheetBehavior.STATE_COLLAPSED);
        sheetBehaviorMain.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    public void errorSave() {
        Snackbar snackbar = Snackbar.make(AppActivity.get().getLayout(), "Не получилось сохранить результат, попробуйте позже", Snackbar.LENGTH_SHORT);
        snackbar.setAnchorView(R.id.bottomNavigationView);
        snackbar.show();
    }
}
