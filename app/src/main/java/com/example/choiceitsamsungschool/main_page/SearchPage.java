package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.choiceitsamsungschool.APIServer;
import com.example.choiceitsamsungschool.AppDatabase;
import com.example.choiceitsamsungschool.InternalStorage;
import com.example.choiceitsamsungschool.MainActivity;
import com.example.choiceitsamsungschool.R;
import com.example.choiceitsamsungschool.db.Friend;
import com.example.choiceitsamsungschool.db.Person;
import com.example.choiceitsamsungschool.db.Survey;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class SearchPage extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static SearchPage page = null;
    private View search_page;
    private BottomSheetBehavior sheetBehavior;
    private static LayoutInflater inflater;
    private static APIServer apiServer;
    private MaterialToolbar toolbar;
    private AnimatedVectorDrawable menu;
    private AnimatedVectorDrawable close;
    private InputMethodManager manager;
    private MaterialCheckBox persons_check_box;
    private MaterialCheckBox surveys_check_box;
    private MaterialCheckBox by_friends_check_box;
    private NumberPicker age_from_picker;
    private NumberPicker age_to_picker;
    private NumberPicker count_questions_from_picker;
    private NumberPicker count_questions_to_picker;
    private MaterialButton reset_all;
    private MaterialButton apply_changes;
    private TextInputEditText search_field;
    private TextInputLayout search_layout;
    private ViewGroup parent_res;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (page == null) {
            search_page = inflater.inflate(R.layout.search_page, container, false);
            Context context = getContext();
            SearchPage.inflater = inflater;

            LinearLayout contentLayout = search_page.findViewById(R.id.search_page_front);

            sheetBehavior = BottomSheetBehavior.from(contentLayout);
            sheetBehavior.setFitToContents(false);
            sheetBehavior.setHideable(false);
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            manager = (InputMethodManager) MainActivity.get().getSystemService(Activity.INPUT_METHOD_SERVICE);

            apiServer = APIServer.getSingletonAPIServer();
            apiServer.setSearchPage(this);
            apiServer.loadUserData();

            toolbar = search_page.findViewById(R.id.search_tool_bar);
            toolbar.setNavigationOnClickListener(v -> changeState());

            menu = (AnimatedVectorDrawable) context.getDrawable(R.drawable.ic_menu_animated);
            close = (AnimatedVectorDrawable) context.getDrawable(R.drawable.ic_close_animated);

            persons_check_box = search_page.findViewById(R.id.search_page_persons_check_box);

            age_from_picker = search_page.findViewById(R.id.search_page_number_picker_age_from);
            age_from_picker.setMinValue(1);
            age_from_picker.setValue(1);
            age_from_picker.setMaxValue(100);
            age_from_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    age_to_picker.setMinValue(newVal);
                }
            });

            age_to_picker = search_page.findViewById(R.id.search_page_number_picker_age_to);
            age_to_picker.setMinValue(1);
            age_to_picker.setMaxValue(100);
            age_to_picker.setValue(100);
            age_to_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    age_from_picker.setMaxValue(newVal);
                }
            });

            surveys_check_box = search_page.findViewById(R.id.search_page_surveys_check_box);

            count_questions_from_picker = search_page.findViewById(R.id.search_page_number_picker_count_questions_from);
            count_questions_from_picker.setMinValue(1);
            count_questions_from_picker.setValue(1);
            count_questions_from_picker.setMaxValue(15);
            count_questions_from_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    count_questions_to_picker.setMinValue(newVal);
                }
            });

            count_questions_to_picker = search_page.findViewById(R.id.search_page_number_picker_count_questions_to);
            count_questions_to_picker.setMinValue(1);
            count_questions_to_picker.setMaxValue(15);
            count_questions_to_picker.setValue(15);
            count_questions_to_picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    count_questions_from_picker.setMaxValue(newVal);
                }
            });

            by_friends_check_box = search_page.findViewById(R.id.search_page_by_friends_surveys);

            reset_all = search_page.findViewById(R.id.search_page_reset_all);
            reset_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetAll();
                }
            });

            apply_changes = search_page.findViewById(R.id.search_page_apply);
            apply_changes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    applyChanges();
                }
            });

            search_field = search_page.findViewById(R.id.search_page_friend_name_input);
            search_layout = search_page.findViewById(R.id.search_page_friend_name_input_layout);
            search_field.addTextChangedListener(new TextInputWatcher(this));

            parent_res = search_page.findViewById(R.id.search_page_content_layout);

            page = this;
            return search_page;
        } else {
            return page.getSearch_page();
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
        manager.hideSoftInputFromWindow(page.getView().getWindowToken(), 0);
    }

    public View getSearch_page() {
        return search_page;
    }

    public void resetAll() {
        persons_check_box.setChecked(true);
        age_from_picker.setMinValue(1);
        age_from_picker.setValue(1);
        age_from_picker.setMaxValue(100);
        age_to_picker.setMinValue(1);
        age_to_picker.setValue(100);
        age_to_picker.setMaxValue(100);

        surveys_check_box.setChecked(true);
        count_questions_from_picker.setMinValue(1);
        count_questions_from_picker.setValue(1);
        count_questions_from_picker.setMaxValue(15);
        count_questions_to_picker.setMinValue(1);
        count_questions_to_picker.setValue(15);
        count_questions_to_picker.setMaxValue(15);

        by_friends_check_box.setChecked(false);

        changeState();
    }

    public void applyChanges() {
        search();
        changeState();
    }

    public void search() {
        String s = search_field.getText().toString();
        if (s.length() >= 5) {
            apiServer.search();
        } else {
            parent_res.removeAllViews();
            if (persons_check_box.isChecked()) {
                filterFriends();
            }
            if (surveys_check_box.isChecked()) {
                filterSurveys();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    public void filterFriends() {
        InternalStorage internalStorage = InternalStorage.getInternalStorage();
        String s = search_field.getText().toString();
        AppDatabase appDatabase = AppDatabase.getDatabase(getContext());
        List<Friend> friends_list = appDatabase.friendDao().getAllFriend(s);
        if (friends_list.size() != 0) {
            TextView textView = new TextView(getContext());
            textView.setText(R.string.friends);
            textView.setTextAppearance(R.style.TextViewStyle);
            textView.setTextSize(18);
            parent_res.addView(textView);
            for (int i = 0; i < friends_list.size(); i++) {
                parent_res.addView(new FriendLine(
                        getContext(),
                        new Person(friends_list.get(i)),
                        internalStorage.load(friends_list.get(i).friend_id, InternalStorage.PROFILE_IMAGE),
                        inflater,
                        this
                ).getPage());
            }
        }
    }

    public void filterSurveys() {
        InternalStorage internalStorage = InternalStorage.getInternalStorage();
        String s = search_field.getText().toString();
        AppDatabase appDatabase = AppDatabase.getDatabase(getContext());
        List<Survey> survey_list = appDatabase.surveyDao().getAllSurvey(s);
        if (survey_list.size() != 0) {
            TextView textView = new TextView(getContext());
            textView.setText(R.string.surveys);
            textView.setTextAppearance(R.style.TextViewStyle);
            textView.setTextSize(18);
            parent_res.addView(textView);
            for (int i = 0; i < survey_list.size(); i++) {
                parent_res.addView(new SurveyCard(
                        getContext(),
                        survey_list.get(i).survey_id,
                        internalStorage.load(survey_list.get(i).survey_id, InternalStorage.SURVEY_TITLE_IMAGE),
                        inflater,
                        this
                ).getPage());
            }
        }
    }

    public String getSearchText() {
        return search_field.getText().toString();
    }

    public boolean searchPersons() {
        return persons_check_box.isChecked();
    }

    public boolean searchSurveys() {
        return surveys_check_box.isChecked();
    }

    public boolean searchFriendsSurveys() {
        return by_friends_check_box.isChecked();
    }

    public int getAgeFrom() {
        return age_from_picker.getValue();
    }

    public int getAgeTo() {
        return age_to_picker.getValue();
    }

    public int getCountQuestionsFrom() {
        return count_questions_from_picker.getValue();
    }

    public int getCountQuestionsTo() {
        return count_questions_to_picker.getValue();
    }

    public void updateResults() {
        parent_res.removeAllViews();
        AppDatabase appDatabase = AppDatabase.getDatabase(getContext());
        List<Person> res_persons = appDatabase.personDao().getResults();
        for (int i = 0; i < res_persons.size(); i++) {
            FriendLine line = new FriendLine(
                    getContext(),
                    res_persons.get(i),
                    InternalStorage.getInternalStorage().load(
                            res_persons.get(i).person_id,
                            InternalStorage.PROFILE_IMAGE
                    ),
                    inflater,
                    this
            );
            parent_res.addView(line.getPage());
        }
        List<Survey> res_surveys = appDatabase.surveyDao().getResult();
        for (int i = 0; i < res_surveys.size(); i++) {
            SurveyCard line = new SurveyCard(
                    getContext(),
                    res_surveys.get(i).survey_id,
                    InternalStorage.getInternalStorage().load(
                            res_surveys.get(i).survey_id,
                            InternalStorage.SURVEY_TITLE_IMAGE
                    ),
                    inflater,
                    this
            );
            parent_res.addView(line.getPage());
        }
    }
}
