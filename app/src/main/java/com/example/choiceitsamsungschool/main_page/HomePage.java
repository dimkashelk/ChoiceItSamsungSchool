package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.choiceitsamsungschool.APIServer;
import com.example.choiceitsamsungschool.AppDatabase;
import com.example.choiceitsamsungschool.MainActivity;
import com.example.choiceitsamsungschool.R;
import com.example.choiceitsamsungschool.db.Friend;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.List;
import java.util.Vector;

public class HomePage extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static HomePage page = null;
    private static List<Friend> friends = new Vector<>();
    private View home_page;
    private BottomSheetBehavior sheetBehavior;
    private LayoutInflater inflater;
    private APIServer apiServer;
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
    private boolean is_increasing_most_popular = false;
    private boolean is_increasing_active = false;
    private boolean is_increasing_date = false;
    private NumberPicker from;
    private NumberPicker to;
    private static ChipGroup friends_group = null;

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
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

//            if (friends.size() == 0) {
//                friends_group.removeAllViews();
//                for (int i = 0; i < 10; i++) {
//                    Chip friend = new Chip(context);
//                    friend.setText(String.valueOf(i) + " " + "chip");
//                    ChipDrawable chipDrawable = ChipDrawable.createFromAttributes(
//                            context,
//                            null,
//                            0,
//                            R.style.ChipStyle
//                    );
//                    friend.setChipDrawable(chipDrawable);
//                    friends_group.addView(friend);
//                }
//            }

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

            MaterialButton button_date = home_page.findViewById(R.id.home_page_check_box_date_button);
            button_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (is_increasing_date) {
                        button_date.setIcon(icon_expanded_date);
                        button_date.setText(R.string.decreasing);
                        icon_expanded_date.start();
                        is_increasing_date = false;
                    } else {
                        button_date.setIcon(icon_collapsed_date);
                        button_date.setText(R.string.increasing);
                        icon_collapsed_date.start();
                        is_increasing_date = true;
                    }
                }
            });

            MaterialButton button_active = home_page.findViewById(R.id.home_page_check_box_active_button);
            button_active.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (is_increasing_active) {
                        button_active.setIcon(icon_expanded_active);
                        button_active.setText(R.string.decreasing);
                        icon_expanded_active.start();
                        is_increasing_active = false;
                    } else {
                        button_active.setIcon(icon_collapsed_active);
                        button_active.setText(R.string.increasing);
                        icon_collapsed_active.start();
                        is_increasing_active = true;
                    }
                }
            });

            MaterialButton button_popular = home_page.findViewById(R.id.home_page_check_box_most_popular_button);
            button_popular.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (is_increasing_most_popular) {
                        button_popular.setIcon(icon_expanded_most_popular);
                        button_popular.setText(R.string.decreasing);
                        icon_expanded_most_popular.start();
                        is_increasing_most_popular = false;
                    } else {
                        button_popular.setIcon(icon_collapsed_most_popular);
                        button_popular.setText(R.string.increasing);
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
            reset_all.setOnClickListener(v -> changeState());

            MaterialButton apply = home_page.findViewById(R.id.home_page_apply);
            apply.setOnClickListener(v -> changeState());

            page = this;
            return home_page;
        } else {
            return page.getHome_page();
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
    }
}
