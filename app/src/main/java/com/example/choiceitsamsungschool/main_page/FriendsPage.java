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
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.choiceitsamsungschool.APIServer;
import com.example.choiceitsamsungschool.AppDatabase;
import com.example.choiceitsamsungschool.InternalStorage;
import com.example.choiceitsamsungschool.MainActivity;
import com.example.choiceitsamsungschool.R;
import com.example.choiceitsamsungschool.db.Friend;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Comparator;
import java.util.List;
import java.util.Vector;

public class FriendsPage extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static FriendsPage page = null;
    private View friends_page;
    private BottomSheetBehavior sheetBehavior;
    private static LayoutInflater inflater;
    private static APIServer apiServer;
    private MaterialToolbar toolbar;
    private AnimatedVectorDrawable menu;
    private AnimatedVectorDrawable close;
    private InputMethodManager manager;
//    private static NumberPicker from;
//    private static NumberPicker to;
    private ViewGroup friends_list = null;
    private MaterialRadioButton check_box_age;
    private MaterialRadioButton check_box_alphabet;
    private MaterialRadioButton check_box_count_surveys;
    private TextInputEditText friend_name_field;
    private TextInputLayout friend_name_layout;
    private static List<Friend> friends = new Vector<>();
    private static ViewGroup friends_group = null;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        friends_page = inflater.inflate(R.layout.friends_page, container, false);
        Context context = getContext();

        manager = (InputMethodManager) MainActivity.get().getSystemService(Activity.INPUT_METHOD_SERVICE);

        friends_page = inflater.inflate(R.layout.friends_page, container, false);
        FriendsPage.inflater = inflater;

        apiServer = APIServer.getSingletonAPIServer();
        apiServer.setFriendsPage(this);
        apiServer.loadUserData();

        toolbar = friends_page.findViewById(R.id.friends_tool_bar);
        toolbar.setNavigationOnClickListener(v -> changeState());

        menu = (AnimatedVectorDrawable) context.getDrawable(R.drawable.ic_menu_animated);
        close = (AnimatedVectorDrawable) context.getDrawable(R.drawable.ic_close_animated);

        LinearLayout contentLayout = friends_page.findViewById(R.id.friends_page_front);

        sheetBehavior = BottomSheetBehavior.from(contentLayout);
        sheetBehavior.setFitToContents(false);
        sheetBehavior.setHideable(false);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

//        from = friends_page.findViewById(R.id.friends_page_number_picker_from);
//        from.setMinValue(1);
//        from.setMaxValue(100);
//        from.setValue(1);
//        from.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                to.setMinValue(newVal);
//            }
//        });
//
//        to = friends_page.findViewById(R.id.friends_page_number_picker_to);
//        to.setMinValue(1);
//        to.setMaxValue(100);
//        to.setValue(100);
//        to.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                from.setMaxValue(newVal);
//            }
//        });

        friends_group = friends_page.findViewById(R.id.friends_page_content_layout);

        AppDatabase appDatabase = AppDatabase.getDatabase(context);
        friends = appDatabase.friendDao().getAllFriend();

        if (friends.size() != 0) {
            friends_group.removeAllViews();
            for (int i = 0; i < friends.size(); i++) {
                friends_group.addView(new FriendLine(
                        getContext(),
                        friends.get(i),
                        InternalStorage.getInternalStorage().load(
                                friends.get(i).friend_id,
                                InternalStorage.PROFILE_IMAGE
                        ),
                        inflater,
                        this).getPage());
            }
        }

        MaterialButton reset_all = friends_page.findViewById(R.id.friends_page_reset_all);
        reset_all.setOnClickListener(v -> resetAll());

        MaterialButton apply = friends_page.findViewById(R.id.friends_page_apply);
        apply.setOnClickListener(v -> saveChanges());

//        check_box_age = friends_page.findViewById(R.id.friends_page_check_box_age);
        check_box_alphabet = friends_page.findViewById(R.id.friends_page_check_box_alphabet);
        check_box_count_surveys = friends_page.findViewById(R.id.friends_page_check_box_count_surveys);

        friend_name_field = friends_page.findViewById(R.id.friends_page_friend_name_input);
        friend_name_field.addTextChangedListener(new TextInputWatcher(this));

        friend_name_layout = friends_page.findViewById(R.id.friends_page_friend_name_input_layout);

        SwipeRefreshLayout swipeRefreshLayout = friends_page.findViewById(R.id.friends_page_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateFriends();
            }
        });

        for (int i = 0; i < 10; i++) {
            friends_group.addView(new FriendLine(
                    context,
                    new Friend("1", "First", "Second"),
                    getResources().getDrawable(R.mipmap.ic_launcher),
                    inflater,
                    this
            ).getPage());
        }

        page = this;
        return friends_page;
    }

    private void saveChanges() {
        updateListFriends();
        changeState();
    }

    private void resetAll() {
//        from.setValue(1);
//        from.setMinValue(1);
//        from.setMaxValue(100);
//        to.setValue(100);
//        to.setMinValue(1);
//        to.setMaxValue(100);
//        check_box_age.setChecked(false);

        check_box_count_surveys.setChecked(false);
        updateListFriends();
        changeState();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void updateListFriends() {
        friends = AppDatabase.getDatabase(getContext()).friendDao().getAllFriend();
//        if (check_box_age.isChecked()) {
//            friends.sort(new Comparator<Friend>() {
//                @Override
//                public int compare(Friend o1, Friend o2) {
//                    return o1.age - o2.age;
//                }
//            });
//        } else
        if (check_box_count_surveys.isChecked()) {
            friends.sort(new Comparator<Friend>() {
                @Override
                public int compare(Friend o1, Friend o2) {
                    return o1.count_surveys - o2.count_surveys;
                }
            });
        } else if (check_box_alphabet.isChecked()) {
            friends.sort(new Comparator<Friend>() {
                @Override
                public int compare(Friend friend, Friend t1) {
                    return (friend.first_name + friend.second_name).compareTo(t1.first_name + t1.second_name);
                }
            });
        }
        friends_group.removeAllViews();
        for (Friend friend : friends) {
            friends_group.addView(new FriendLine(
                    getContext(),
                    friend,
                    InternalStorage.getInternalStorage().load(
                            friend.friend_id,
                            InternalStorage.PROFILE_IMAGE
                    ),
                    inflater,
                    this).getPage());
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
        }
        friend_name_layout.setEnabled(false);
        friend_name_layout.setEnabled(true);
        manager.hideSoftInputFromWindow(page.getView().getWindowToken(), 0);
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    public void filterFriends() {
//        int age_from = from.getValue();
//        int age_to = to.getValue();
//        String s = friend_name_field.getText().toString();
//        friends = AppDatabase.getDatabase(getContext()).friendDao().getAllFriend();
//        Vector<Friend> new_list = new Vector<>();
//        for (Friend friend : friends) {
//            String dop = friend.first_name + " " + friend.second_name;
//            if (dop.contains(s)) {
//                if (friend.age >= age_from && friend.age <= age_to) {
//                    new_list.add(friend);
//                }
//            }
//        }
//        friends = new_list;
//        friends_group.removeAllViews();
//        for (int i = 0; i < friends.size(); i++) {
//            friends_group.addView(new FriendLine(
//                    getContext(),
//                    friends.get(i),
//                    InternalStorage.getInternalStorage().load(
//                            friends.get(i).friend_id,
//                            InternalStorage.PROFILE_IMAGE
//                    ),
//                    inflater,
//                    this).getPage());
//        }
    }

    public View getFriends_page() {
        return friends_page;
    }

    private void updateFriends() {
        apiServer.loadFriends();
    }
}
