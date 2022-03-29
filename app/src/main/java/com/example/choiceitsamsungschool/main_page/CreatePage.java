package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnMultiSelectedListener;
import gun0912.tedimagepicker.builder.type.MediaType;

public class CreatePage extends Fragment {
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
    private MaterialButton select_all;
    private MaterialButton show_all;
    private ChipGroup friends_group;
    private MaterialCheckBox add_to_favorites;
    private MaterialCheckBox add_to_archive;
    private MaterialCheckBox only_for_friends;
    private MaterialCheckBox anonymous;
    private MaterialCheckBox public_survey;
    private List<Chip> chips_friends = new Vector<>();
    private Vector<AddLine> add_lines = new Vector<>();
    private ViewGroup survey_content;
    private TextView empty_title;
    private MaterialButton add_images;

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (page == null) {
            create_page = inflater.inflate(R.layout.create_page, container, false);
            Context context = getContext();
            CreatePage.inflater = inflater;

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

            friends_group = create_page.findViewById(R.id.create_page_friends_chips_group);
            friends_group.removeAllViews();

            AppDatabase appDatabase = AppDatabase.getDatabase(context);
            List<Friend> friends = appDatabase.friendDao().getAllFriend();
            for (Friend friend : friends) {
                Chip chip = new Chip(context);
                chip.setText(friend.first_name + " " + friend.second_name);
                chips_friends.add(chip);
                friends_group.addView(chip);
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
            add_to_archive = create_page.findViewById(R.id.create_page_add_to_archive);
            only_for_friends = create_page.findViewById(R.id.create_page_for_friends_only);
            anonymous = create_page.findViewById(R.id.create_page_is_anonymous);
            public_survey = create_page.findViewById(R.id.create_page_is_public);

            survey_content = create_page.findViewById(R.id.create_page_survey_content);

            add_images = create_page.findViewById(R.id.create_page_add_to_survey);
            add_images.setOnClickListener(v -> startChooseImage());

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
        int min_count = 0;
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
                        Vector<Bitmap> images = new Vector<>();
                        for (Uri uri : list) {
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                                images.add(bitmap);
                            } catch (IOException ignored) {
                                Toast.makeText(getContext(), "Не получилось открыть изображения, попробуйте ещё раз", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        setSelectedImages(images);
                    }
                });
    }

    public void setSelectedImages(List<Bitmap> images) {
        add_lines = new Vector<>();
        survey_content.removeAllViews();

        for (int i = 0; i < images.size(); i++) {
            AddLine addLine = new AddLine(
                    getContext(),
                    new BitmapDrawable(getResources(), images.get(i)),
                    inflater,
                    this
            );
            survey_content.addView(addLine.getPage());
            add_lines.add(addLine);
        }

        if (add_lines.size() == 0) {
            survey_content.addView(empty_title);
        }
    }
}
