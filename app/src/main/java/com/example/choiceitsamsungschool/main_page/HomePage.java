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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.choiceitsamsungschool.APIServer;
import com.example.choiceitsamsungschool.AppDatabase;
import com.example.choiceitsamsungschool.InternalStorage;
import com.example.choiceitsamsungschool.MainActivity;
import com.example.choiceitsamsungschool.R;
import com.example.choiceitsamsungschool.db.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePage extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static HomePage page = null;
    private View home_page;
    private BottomSheetBehavior sheetBehavior;
    private MaterialButton button;
    private LayoutInflater inflater;
    private APIServer apiServer;
    private MaterialToolbar toolbar;
    private AnimatedVectorDrawable menu;
    private AnimatedVectorDrawable close;

    @SuppressLint("UseCompatLoadingForDrawables")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (page == null) {
            Context context = getContext();

            home_page = inflater.inflate(R.layout.home_page, container, false);
            this.inflater = inflater;

            apiServer = APIServer.getSingletonAPIServer();
            apiServer.setHomePage(this);
            apiServer.loadUserData();

            toolbar = home_page.findViewById(R.id.home_tool_bar);
            toolbar.setNavigationOnClickListener(v -> changeState());

            menu = (AnimatedVectorDrawable) context.getDrawable(R.drawable.ic_menu_animated);
            close = (AnimatedVectorDrawable) context.getDrawable(R.drawable.ic_close_animated);

            LinearLayout contentLayout = home_page.findViewById(R.id.home_page_front);

            sheetBehavior = BottomSheetBehavior.from(contentLayout);
            sheetBehavior.setFitToContents(false);
            sheetBehavior.setHideable(false);
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            ChipGroup friends_group = home_page.findViewById(R.id.home_page_friends_chips_group);

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
        }
    }

    public View getHome_page() {
        return home_page;
    }
}
