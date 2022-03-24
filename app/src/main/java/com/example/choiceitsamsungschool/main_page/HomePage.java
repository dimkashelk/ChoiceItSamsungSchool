package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import com.example.choiceitsamsungschool.APIServer;
import com.example.choiceitsamsungschool.AppDatabase;
import com.example.choiceitsamsungschool.InternalStorage;
import com.example.choiceitsamsungschool.MainActivity;
import com.example.choiceitsamsungschool.R;
import com.example.choiceitsamsungschool.db.Friend;
import com.example.choiceitsamsungschool.db.Survey;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Vector;

public class HomePage extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static HomePage page = null;
    private static List<Friend> friends = new Vector<>();
    private View home_page;
    private BottomSheetBehavior sheetBehavior;
    private static LayoutInflater inflater;
    private static APIServer apiServer;
    private MaterialToolbar toolbar;
    private AnimatedVectorDrawable icon_collapsed_date;
    private AnimatedVectorDrawable icon_expanded_date;
    private AnimatedVectorDrawable icon_collapsed_active;
    private AnimatedVectorDrawable icon_expanded_active;
    private AnimatedVectorDrawable icon_collapsed_most_popular;
    private AnimatedVectorDrawable icon_expanded_most_popular;
    private AnimatedVectorDrawable menu;
    private AnimatedVectorDrawable close;
    private InputMethodManager manager;
    private static boolean is_increasing_most_popular = false;
    private static boolean is_increasing_active = false;
    private static boolean is_increasing_date = false;
    private static NumberPicker from;
    private static NumberPicker to;
    private static ChipGroup friends_group = null;
    private static Vector<Friend> friends_list = new Vector<>();
    private static ViewGroup parent_surveys = null;
    private static List<Survey> surveys_list = new Vector<>();
    private MaterialCheckBox check_box_date;
    private MaterialCheckBox check_box_active;
    private MaterialCheckBox check_box_most_popular;
    private MaterialButton is_increasing_date_button;
    private MaterialButton is_increasing_most_popular_button;
    private MaterialButton is_increasing_active_button;
    private TextInputEditText friend_name_field;

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n", "CutPasteId"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (page == null) {
            Context context = getContext();

            manager = (InputMethodManager) MainActivity.get().getSystemService(Activity.INPUT_METHOD_SERVICE);

            home_page = inflater.inflate(R.layout.home_page, container, false);
            this.inflater = inflater;

            apiServer = APIServer.getSingletonAPIServer();
            apiServer.setHomePage(this);
            apiServer.loadUserData();

            toolbar = home_page.findViewById(R.id.home_tool_bar);
            toolbar.setNavigationOnClickListener(v -> changeState());

            icon_collapsed_date = (AnimatedVectorDrawable) context.getDrawable(R.drawable.avd_checkable_expandcollapse_collapsed_to_expanded);
            icon_expanded_date = (AnimatedVectorDrawable) context.getDrawable(R.drawable.avd_checkable_expandcollapse_expanded_to_collapsed);

            icon_collapsed_active = (AnimatedVectorDrawable) context.getDrawable(R.drawable.avd_checkable_expandcollapse_collapsed_to_expanded);
            icon_expanded_active = (AnimatedVectorDrawable) context.getDrawable(R.drawable.avd_checkable_expandcollapse_expanded_to_collapsed);

            icon_collapsed_most_popular = (AnimatedVectorDrawable) context.getDrawable(R.drawable.avd_checkable_expandcollapse_collapsed_to_expanded);
            icon_expanded_most_popular = (AnimatedVectorDrawable) context.getDrawable(R.drawable.avd_checkable_expandcollapse_expanded_to_collapsed);

            menu = (AnimatedVectorDrawable) context.getDrawable(R.drawable.ic_menu_animated);
            close = (AnimatedVectorDrawable) context.getDrawable(R.drawable.ic_close_animated);

            LinearLayout contentLayout = home_page.findViewById(R.id.home_page_front);

            sheetBehavior = BottomSheetBehavior.from(contentLayout);
            sheetBehavior.setFitToContents(false);
            sheetBehavior.setHideable(false);
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            friends_group = home_page.findViewById(R.id.home_page_friends_chips_group);
            AppDatabase appDatabase = AppDatabase.getDatabase(context);
            friends = appDatabase.friendDao().getAllFriend();

            if (friends.size() != 0) {
                friends_group.removeAllViews();
                for (int i = 0; i < friends.size(); i++) {
                    Chip friend = new Chip(context);
                    friend.setText(friends.get(i).first_name + " " + friends.get(i).second_name);
                    ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(
                            context,
                            null,
                            0,
                            R.style.ChipStyle
                    );
                    friend.setChipDrawable(chipDrawable);
                    friends_group.addView(friend);
                    friends_list.add(friends.get(i));
                }
            }

            MaterialButton show_all_friends = home_page.findViewById(R.id.home_page_show_all_friends);
            show_all_friends.setOnClickListener(new View.OnClickListener() {
                private boolean show_all = true;

                @Override
                public void onClick(View v) {
                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) friends_group.getLayoutParams();
                    if (show_all) {
                        lp.matchConstraintMaxHeight = 0;
                        friends_group.setLayoutParams(lp);
                        show_all_friends.setText("Спрятать");
                        show_all = false;
                    } else {
                        lp.matchConstraintMaxHeight = 170;
                        friends_group.setLayoutParams(lp);
                        show_all_friends.setText("Показать все");
                        show_all = true;
                    }
                }
            });

            is_increasing_date_button = home_page.findViewById(R.id.home_page_check_box_date_button);
            is_increasing_date_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (is_increasing_date) {
                        is_increasing_date_button.setIcon(icon_expanded_date);
                        is_increasing_date_button.setText(R.string.decreasing);
                        icon_expanded_date.start();
                        is_increasing_date = false;
                    } else {
                        is_increasing_date_button.setIcon(icon_collapsed_date);
                        is_increasing_date_button.setText(R.string.increasing);
                        icon_collapsed_date.start();
                        is_increasing_date = true;
                    }
                }
            });

            is_increasing_active_button = home_page.findViewById(R.id.home_page_check_box_active_button);
            is_increasing_active_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (is_increasing_active) {
                        is_increasing_active_button.setIcon(icon_expanded_active);
                        is_increasing_active_button.setText(R.string.decreasing);
                        icon_expanded_active.start();
                        is_increasing_active = false;
                    } else {
                        is_increasing_active_button.setIcon(icon_collapsed_active);
                        is_increasing_active_button.setText(R.string.increasing);
                        icon_collapsed_active.start();
                        is_increasing_active = true;
                    }
                }
            });

            is_increasing_most_popular_button = home_page.findViewById(R.id.home_page_check_box_most_popular_button);
            is_increasing_most_popular_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (is_increasing_most_popular) {
                        is_increasing_most_popular_button.setIcon(icon_expanded_most_popular);
                        is_increasing_most_popular_button.setText(R.string.decreasing);
                        icon_expanded_most_popular.start();
                        is_increasing_most_popular = false;
                    } else {
                        is_increasing_most_popular_button.setIcon(icon_collapsed_most_popular);
                        is_increasing_most_popular_button.setText(R.string.increasing);
                        icon_collapsed_most_popular.start();
                        is_increasing_most_popular = true;
                    }
                }
            });

            from = home_page.findViewById(R.id.home_page_number_picker_from);
            from.setMinValue(1);
            from.setMaxValue(15);
            from.setValue(1);
            from.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    to.setMinValue(newVal);
                }
            });

            to = home_page.findViewById(R.id.home_page_number_picker_to);
            to.setMinValue(1);
            to.setMaxValue(15);
            to.setValue(15);
            to.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    from.setMaxValue(newVal);
                }
            });

            MaterialButton reset_all = home_page.findViewById(R.id.home_page_reset_all);
            reset_all.setOnClickListener(v -> resetAll());

            MaterialButton apply = home_page.findViewById(R.id.home_page_apply);
            apply.setOnClickListener(v -> saveChanges());

            parent_surveys = (ViewGroup) home_page.findViewById(R.id.home_page_content_layout);
            parent_surveys.removeAllViews();

            if (surveys_list.size() != 0) {
                parent_surveys.removeAllViews();
                for (int i = 0; i < surveys_list.size(); i++) {
                    SurveyCard surveyCard = new SurveyCard(
                            context,
                            surveys_list.get(i).survey_id,
                            InternalStorage.getInternalStorage().load(surveys_list.get(i).survey_id, InternalStorage.SURVEY_TITLE_IMAGE),
                            inflater,
                            this);
                    parent_surveys.addView(surveyCard.getPage());
                }
            }

            check_box_date = home_page.findViewById(R.id.home_page_check_box_date);
            check_box_active = home_page.findViewById(R.id.home_page_check_box_active);
            check_box_most_popular = home_page.findViewById(R.id.home_page_check_box_most_popular);

            friend_name_field = home_page.findViewById(R.id.home_page_friend_name_input);
            friend_name_field.addTextChangedListener(new TextInputWatcher(this));

            page = this;
            return home_page;
        } else {
            return page.getHome_page();
        }
    }

    @SuppressLint("SetTextI18n")
    public void filterFriends() {
        String s = friend_name_field.getText().toString();
        Vector<Friend> new_list = new Vector<>();
        for (Friend friend: friends) {
            String dop = friend.first_name + " " + friend.second_name;
            if (dop.contains(s)) {
                new_list.add(friend);
            }
        }
        friends = new_list;
        friends_group.removeAllViews();
        for (int i = 0; i < friends.size(); i++) {
            Chip friend = new Chip(page.getContext());
            friend.setText(friends.get(i).first_name + " " + friends.get(i).second_name);
            ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(
                    page.getContext(),
                    null,
                    0,
                    R.style.ChipStyle
            );
            friend.setChipDrawable(chipDrawable);
            friends_group.addView(friend);
        }
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
            manager.hideSoftInputFromWindow(page.getView().getWindowToken(), 0);
        }
    }

    public View getHome_page() {
        return home_page;
    }

    @SuppressLint("SetTextI18n")
    public static void updateFriendsChips() {
        AppDatabase appDatabase = AppDatabase.getDatabase(MainActivity.get().getBaseContext());
        friends = appDatabase.friendDao().getAllFriend();
        if (friends_group != null) {
            friends_group.removeAllViews();
            if (friends.size() != 0) {
                for (int i = 0; i < friends.size(); i++) {
                    Chip friend = new Chip(page.getContext());
                    friend.setText(friends.get(i).first_name + " " + friends.get(i).second_name);
                    ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(
                            page.getContext(),
                            null,
                            0,
                            R.style.ChipStyle
                    );
                    friend.setChipDrawable(chipDrawable);
                    friends_group.addView(friend);
                }
            } else {
                ProgressBar progressBar = new ProgressBar(page.getContext());
                ViewGroup.LayoutParams params = new ViewPager.LayoutParams();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                progressBar.setLayoutParams(params);
                progressBar.setForegroundGravity(Gravity.CENTER);
                friends_group.addView(progressBar);
            }
        }
    }

    public static boolean get_increasing_most_popular() {
        return is_increasing_most_popular;
    }

    public static boolean get_increasing_active() {
        return is_increasing_active;
    }

    public static boolean get_increasing_date() {
        return is_increasing_date;
    }

    @SuppressLint("ResourceType")
    public static List<String> get_selected_friends() {
        if (friends_group != null) {
            Vector<String> friend_list = new Vector<>();
            for (Integer i : friends_group.getCheckedChipIds()) {
                friend_list.add(friends_list.get(i - 1).friend_id);
            }
            return friend_list;
        }
        return new Vector<>();
    }

    public static int get_min_count() {
        if (page == null) {
            return 1;
        } else {
            return from.getValue();
        }
    }

    public static int get_max_count() {
        if (page == null) {
            return 15;
        } else {
            return to.getValue();
        }
    }

    public static void updateNewsFeed() {
        if (parent_surveys == null) {
            AppDatabase appDatabase = AppDatabase.getDatabase(MainActivity.get().getBaseContext());
            surveys_list = appDatabase.surveyDao().getNews();
        } else {
            parent_surveys.removeAllViews();
            for (int i = 0; i < surveys_list.size(); i++) {
                SurveyCard surveyCard = new SurveyCard(
                        page.getContext(),
                        surveys_list.get(i).survey_id,
                        InternalStorage.getInternalStorage().load(surveys_list.get(i).survey_id, InternalStorage.SURVEY_TITLE_IMAGE),
                        inflater,
                        page);
                parent_surveys.addView(surveyCard.getPage());
            }
        }
    }

    private void resetAll() {
        updateFriendsChips();
        from.setValue(1);
        from.setMinValue(1);
        from.setMaxValue(15);
        to.setValue(15);
        to.setMinValue(1);
        to.setMaxValue(15);
        check_box_date.setChecked(false);
        check_box_active.setChecked(false);
        check_box_most_popular.setChecked(false);
        is_increasing_date_button.setIcon(icon_expanded_date);
        is_increasing_date_button.setText(R.string.decreasing);
        icon_expanded_date.start();
        is_increasing_date = false;
        is_increasing_active_button.setIcon(icon_expanded_active);
        is_increasing_active_button.setText(R.string.decreasing);
        icon_expanded_active.start();
        is_increasing_active = false;
        is_increasing_most_popular_button.setIcon(icon_expanded_most_popular);
        is_increasing_most_popular_button.setText(R.string.decreasing);
        icon_expanded_most_popular.start();
        is_increasing_most_popular = false;
        loadUserNewsFeed();
        changeState();
    }

    private void loadUserNewsFeed() {
        apiServer.loadUserNewsFeed();
        parent_surveys.removeAllViews();
        parent_surveys.addView(new ProgressBar(getContext()));
    }

    private void saveChanges() {
        loadUserNewsFeed();
        changeState();
    }
}
