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
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.choiceitsamsungschool.APIServer;
import com.example.choiceitsamsungschool.AppDatabase;
import com.example.choiceitsamsungschool.InternalStorage;
import com.example.choiceitsamsungschool.MainActivity;
import com.example.choiceitsamsungschool.R;
import com.example.choiceitsamsungschool.db.Friend;
import com.example.choiceitsamsungschool.db.Person;
import com.google.android.material.appbar.MaterialToolbar;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonPage extends Fragment {
    private String person_id;
    private static PersonPage page = null;
    private View person_page;
    private static LayoutInflater inflater;
    private static APIServer apiServer;
    private InputMethodManager manager;
    private String first_name = "Test";
    private String second_name = "Test";
    private int count_friends = 0;
    private int count_surveys = 0;
    private Drawable bitmap;
    private TextView full_name;
    private CircleImageView image;
    private MaterialToolbar toolbar;
    private AnimatedVectorDrawable menu;
    private AnimatedVectorDrawable close;
    private boolean to_close = false;

    public PersonPage() {

    }

    public PersonPage(Person person) {
        this.person_id = person.person_id;
        this.first_name = person.first_name;
        this.second_name = person.second_name;
        this.count_friends = person.count_friends;
        this.count_surveys = person.count_surveys;
        this.bitmap = InternalStorage.getInternalStorage().load(person.person_id, InternalStorage.PROFILE_IMAGE);
    }

    public PersonPage(Friend friend) {
        this.person_id = friend.friend_id;
        this.first_name = friend.first_name;
        this.second_name = friend.second_name;
        this.count_friends = friend.count_friends;
        this.count_surveys = friend.count_surveys;
        this.bitmap = InternalStorage.getInternalStorage().load(friend.friend_id, InternalStorage.PROFILE_IMAGE);
    }

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (page == null) {
            person_page = inflater.inflate(R.layout.person_page, container, false);
            Context context = getContext();

            PersonPage.inflater = inflater;
            manager = (InputMethodManager) MainActivity.get().getSystemService(Activity.INPUT_METHOD_SERVICE);

            full_name = person_page.findViewById(R.id.person_page_full_name);
            full_name.setText(first_name + " " + second_name);

            image = person_page.findViewById(R.id.person_page_image);
            // image.setImageDrawable(bitmap);

            toolbar = person_page.findViewById(R.id.person_tool_bar);
            toolbar.setNavigationOnClickListener(v -> changeState());

            menu = (AnimatedVectorDrawable) context.getDrawable(R.drawable.ic_menu_animated);
            close = (AnimatedVectorDrawable) context.getDrawable(R.drawable.ic_close_animated);

            changeState();
            page = this;
        }
        return page.getPerson_page();
    }

    public View getPerson_page() {
        return person_page;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void changeState() {
        if (to_close) {
            NavigationItemListener.get().closePersonPage();
        } else {
            toolbar.setNavigationIcon(close);
            close.start();
            to_close = true;
        }
    }
}
