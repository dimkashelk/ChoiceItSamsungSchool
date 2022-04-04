package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.choiceitsamsungschool.APIServer;
import com.example.choiceitsamsungschool.AppDatabase;
import com.example.choiceitsamsungschool.MainActivity;
import com.example.choiceitsamsungschool.R;
import com.example.choiceitsamsungschool.db.Friend;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;
import gun0912.tedimagepicker.builder.type.MediaType;

public class CreatePage extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    @SuppressLint("StaticFieldLeak")
    private static CreatePage page = null;
    private View create_page;
    private BottomSheetBehavior sheetBehavior;
    private static LayoutInflater inflater;
    private static APIServer apiServer;
    private MaterialToolbar toolbar;
    private AnimatedVectorDrawable menu;
    private AnimatedVectorDrawable close;
    private InputMethodManager manager;
    private TextInputEditText friend_name;
    private TextInputLayout friend_name_layout;
    private TextInputEditText title;
    private TextInputLayout title_layout;
    private TextInputEditText description;
    private TextInputLayout description_layout;
    private MaterialButton select_all;
    private MaterialButton show_all;
    private ChipGroup friends_group;
    private MaterialCheckBox add_to_favorites;
    private MaterialCheckBox only_for_friends;
    private MaterialRadioButton anonymous;
    private MaterialRadioButton public_survey;
    private List<Chip> chips_friends = new Vector<>();
    private Vector<AddLine> add_lines = new Vector<>();
    private List<Friend> friends_list = new Vector<>();
    private ViewGroup survey_content;
    private TextView empty_title;
    private MaterialButton add_images;
    private MaterialButton change_date;
    private CircularProgressButton create_survey;
    private SwipeRefreshLayout swipe_refresh;
    private Vibrator vibrator = (Vibrator) MainActivity.get().getSystemService(Context.VIBRATOR_SERVICE);
    private Animation shake;
    private boolean is_empty_title = true;
    private TextView datetime;
    private int selected_day = -1;
    private int selected_month = -1;
    private int selected_year = -1;

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (page == null) {

            create_page = inflater.inflate(R.layout.create_page, container, false);
            Context context = getContext();
            CreatePage.inflater = inflater;

            shake = AnimationUtils.loadAnimation(context, R.anim.shake);

            LinearLayout contentLayout = create_page.findViewById(R.id.create_page_front);

            sheetBehavior = BottomSheetBehavior.from(contentLayout);
            sheetBehavior.setFitToContents(false);
            sheetBehavior.setHideable(false);
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            manager = (InputMethodManager) MainActivity.get().getSystemService(Activity.INPUT_METHOD_SERVICE);

            apiServer = APIServer.getSingletonAPIServer();
            apiServer.setCreatePage(this);
            apiServer.loadUserData();

            toolbar = create_page.findViewById(R.id.create_tool_bar);
            toolbar.setNavigationOnClickListener(v -> changeState());

            menu = (AnimatedVectorDrawable) context.getDrawable(R.drawable.ic_menu_animated);
            close = (AnimatedVectorDrawable) context.getDrawable(R.drawable.ic_close_animated);

            empty_title = create_page.findViewById(R.id.create_page_survey_create_title);
            empty_title.setOnClickListener(v -> startChooseImage());

            friend_name = create_page.findViewById(R.id.create_page_friend_name_input);
            friend_name_layout = create_page.findViewById(R.id.create_page_friend_name_input_layout);

            friends_group = create_page.findViewById(R.id.create_page_friends_chips_group);

            AppDatabase appDatabase = AppDatabase.getDatabase(context);
            friends_list = appDatabase.friendDao().getAllFriend();

            if (friends_list.size() != 0) {
                friends_group.removeAllViews();
                for (Friend friend : friends_list) {
                    Chip chip = new Chip(context);
                    chip.setText(friend.first_name + " " + friend.second_name);
                    chips_friends.add(chip);
                    friends_group.addView(chip);
                }
            }

            select_all = create_page.findViewById(R.id.create_page_select_all_friends);
            select_all.setOnClickListener(new View.OnClickListener() {
                private boolean select_all_boolean = true;

                @Override
                public void onClick(View v) {
                    if (select_all_boolean) {
                        for (Chip chip : chips_friends) {
                            chip.setChecked(true);
                        }
                        select_all.setText(R.string.remove_all);
                        select_all_boolean = false;
                    } else {
                        for (Chip chip : chips_friends) {
                            chip.setChecked(false);
                        }
                        select_all.setText(R.string.select_all);
                        select_all_boolean = true;
                    }
                }
            });

            show_all = create_page.findViewById(R.id.create_page_show_all_friends);
            show_all.setOnClickListener(new View.OnClickListener() {
                private boolean show_all_bool = true;

                @Override
                public void onClick(View v) {
                    ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) friends_group.getLayoutParams();
                    if (show_all_bool) {
                        lp.matchConstraintMaxHeight = 0;
                        friends_group.setLayoutParams(lp);
                        show_all.setText("Спрятать");
                        show_all_bool = false;
                    } else {
                        lp.matchConstraintMaxHeight = 170;
                        friends_group.setLayoutParams(lp);
                        show_all.setText("Показать все");
                        show_all_bool = true;
                    }
                }
            });

            add_to_favorites = create_page.findViewById(R.id.create_page_add_to_favorites);
            only_for_friends = create_page.findViewById(R.id.create_page_for_friends_only);
            anonymous = create_page.findViewById(R.id.create_page_is_anonymous);
            public_survey = create_page.findViewById(R.id.create_page_is_public);

            survey_content = create_page.findViewById(R.id.create_page_survey_content);

            add_images = create_page.findViewById(R.id.create_page_add_to_survey);
            add_images.setOnClickListener(v -> startChooseImage());

            create_survey = create_page.findViewById(R.id.create_page_create_survey_button);
            create_survey.setOnClickListener(v -> createSurvey());

            swipe_refresh = create_page.findViewById(R.id.create_page_refresh);
            swipe_refresh.setOnRefreshListener(this);

            title = create_page.findViewById(R.id.create_page_title_survey);
            title_layout = create_page.findViewById(R.id.create_page_title_survey_layout);

            description = create_page.findViewById(R.id.create_page_description_survey);
            description_layout = create_page.findViewById(R.id.create_page_description_survey_layout);

            change_date = create_page.findViewById(R.id.create_page_change_date_button);
            change_date.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startChooseDate();
                }
            });

            datetime = create_page.findViewById(R.id.create_page_date_picker_text);

            page = this;
            return create_page;
        } else {
            return page.getCreate_page();
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
        friend_name_layout.clearFocus();
        title_layout.clearFocus();
        description_layout.clearFocus();
        manager.hideSoftInputFromWindow(page.getView().getWindowToken(), 0);
    }

    public View getCreate_page() {
        return create_page;
    }

    public void deleteLine(AddLine line) {
        add_lines.remove(line);

        survey_content.removeAllViews();
        for (int i = 0; i < add_lines.size(); i++) {
            survey_content.addView(add_lines.get(i).getPage());
        }

        if (add_lines.size() == 0) {
            survey_content.addView(empty_title);
        }
    }

    public void startChooseImage() {
        int max_count = 16 - add_lines.size();
        int min_count;
        if (max_count <= 1) {
            min_count = 0;
        } else {
            min_count = 2;
        }
        TedImagePicker.with(getContext()).
                title("Выберите изображения").
                buttonText("Выбрать").
                mediaType(MediaType.IMAGE).
                image().
                min(min_count, "Выберите не менее" + min_count + "изображений").
                max(max_count, "Выберите не более " + max_count + " изображений").
                startMultiImage(new OnMultiSelectedListener() {
                    @Override
                    public void onSelected(@NonNull List<? extends Uri> list) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (Uri uri : list) {
                                    try {
                                        Bitmap b = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                                        Bitmap bitmap = Bitmap.createScaledBitmap(b, b.getWidth() / 5, b.getHeight() / 5, false);
                                        MainActivity.get().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                addSelectedImage(bitmap);
                                            }
                                        });
                                    } catch (IOException ignored) {
                                        MainActivity.get().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getContext(), "Не получилось открыть изображения, попробуйте ещё раз", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        return;
                                    }
                                }
                            }
                        }).start();
                    }
                });
    }

    public void addSelectedImage(Bitmap image) {
        if (is_empty_title) {
            survey_content.removeAllViews();
            is_empty_title = false;
        }
        AddLine addLine = new AddLine(
                getContext(),
                image,
                inflater,
                this
        );
        survey_content.addView(addLine.getPage());
        add_lines.add(addLine);

        if (add_lines.size() == 16) {
            add_images.setClickable(false);
            add_images.setFocusable(false);
        } else {
            add_images.setClickable(true);
            add_images.setFocusable(true);
        }
    }

    public void setSelectedImages(List<Bitmap> images) {
        add_lines = new Vector<>();
        survey_content.removeAllViews();

        for (int i = 0; i < images.size(); i++) {
            AddLine addLine = new AddLine(
                    getContext(),
                    images.get(i),
                    inflater,
                    this
            );
            survey_content.addView(addLine.getPage());
            add_lines.add(addLine);
        }

        if (add_lines.size() == 0) {
            survey_content.addView(empty_title);
            is_empty_title = true;
        }

        if (add_lines.size() == 16) {
            add_images.setClickable(false);
            add_images.setFocusable(false);
        } else {
            add_images.setClickable(true);
            add_images.setFocusable(true);
        }
    }

    public void createSurvey() {
        String title_text = title.getText().toString();
        if (title_text.length() == 0) {
            title_layout.startAnimation(shake);
            vibrate(150);
            return;
        }
        if (add_lines.size() < 2) {
            empty_title.startAnimation(shake);
            vibrate(150);
            return;
        }
        String description_text = description.getText().toString();
        boolean empty_line = false;
        for (AddLine addLine : add_lines) {
            if (addLine.isEmptyTitle()) {
                vibrate(150);
                addLine.startShake(shake);
                empty_line = true;
            }
        }
        if (empty_line) {
            return;
        }
        Vector<Bitmap> images = new Vector<>();
        for (AddLine addLine : add_lines) {
            images.add(addLine.getBitmap());
        }
        Vector<String> friends = new Vector<>();
        for (Integer integer : friends_group.getCheckedChipIds()) {
            friends.add(friends_list.get(integer).friend_id);
        }
        apiServer.createSurvey(
                title_text,
                description_text,
                images,
                getSelectedDay(),
                getSelectedMonth(),
                getSelectedYear(),
                add_to_favorites.isChecked(),
                only_for_friends.isChecked(),
                anonymous.isChecked(),
                friends
        );
        create_survey.startAnimation();
    }

    private void vibrate(int milliseconds) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(milliseconds);
        }
    }

    public void successfulUpload() {
        create_survey.revertAnimation();
        Snackbar snackbar = Snackbar.make(AppActivity.get().getLayout(), "Опрос успешно создан", Snackbar.LENGTH_SHORT);
        snackbar.setAnchorView(R.id.bottomNavigationView);
        snackbar.show();
    }

    public void unsuccessfulUpload() {
        create_survey.revertAnimation();
        Snackbar snackbar = Snackbar.make(AppActivity.get().getLayout(), "Не получилось создать опрос, попробуйте ещё раз позже", Snackbar.LENGTH_SHORT);
        snackbar.setAnchorView(R.id.bottomNavigationView);
        snackbar.show();
    }

    @Override
    public void onRefresh() {
        swipe_refresh.setRefreshing(false);
    }

    public void startChooseDate() {
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                R.style.DatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if (checkDate(year, monthOfYear, dayOfMonth) < 0) {
                            datetime.setText("Доступно до " + dayOfMonth + "/" + monthOfYear + "/" + year + " 23:59:59");
                            selected_day = dayOfMonth;
                            selected_month = monthOfYear;
                            selected_year = year;
                        } else {
                            datetime.setText(R.string.no_limits);
                            selected_day = -1;
                            selected_month = -1;
                            selected_year = -1;
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    private int checkDate(int year, int month, int day) {
        Calendar today = Calendar.getInstance();
        Calendar new_day = Calendar.getInstance();
        new_day.set(year, month, day);
        return today.compareTo(new_day);
    }

    public int getSelectedDay() {
        return selected_day;
    }

    public int getSelectedMonth() {
        return selected_month;
    }

    public int getSelectedYear() {
        return selected_year;
    }
}
