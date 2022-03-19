package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.choiceitsamsungschool.APIServer;
import com.example.choiceitsamsungschool.AppDatabase;
import com.example.choiceitsamsungschool.InternalStorage;
import com.example.choiceitsamsungschool.MainActivity;
import com.example.choiceitsamsungschool.R;
import com.example.choiceitsamsungschool.db.Friend;
import com.example.choiceitsamsungschool.db.User;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import java.io.IOException;
import java.util.Vector;

import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedimagepicker.builder.TedImagePicker;
import gun0912.tedimagepicker.builder.listener.OnSelectedListener;
import gun0912.tedimagepicker.builder.type.MediaType;

public class UserPage extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static UserPage page = null;
    private View user_page;
    private BottomSheetBehavior sheetBehavior;
    private MaterialToolbar toolbar;
    private LinearLayout friends;
    private APIServer apiServer;
    private LayoutInflater inflater;
    private InternalStorage internalStorage;
    private AnimatedVectorDrawable menu;
    private AnimatedVectorDrawable close;
    private InputMethodManager manager;
    private static Vector<SurveyCard> friends_list = new Vector<>();
    private static Vector<SurveyCard> user_surveys = new Vector<>();
    private static Vector<SurveyCard> favorites_surveys = new Vector<>();
    private static Vector<SurveyCard> archive_surveys = new Vector<>();

    @Nullable
    @Override
    @SuppressLint({"CutPasteId", "UseCompatLoadingForDrawables", "SetTextI18n"})
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (page == null) {
            UserPageSettings.userPage = this;

            user_page = inflater.inflate(R.layout.user_page, container, false);
            this.inflater = inflater;
            Context context = getContext();

            apiServer = APIServer.getSingletonAPIServer();
            apiServer.setUserPage(this);
            apiServer.loadUserData();

            toolbar = user_page.findViewById(R.id.user_tool_bar);
            toolbar.setNavigationOnClickListener(v -> changeState());

            menu = (AnimatedVectorDrawable) context.getDrawable(R.drawable.ic_menu_animated);
            close = (AnimatedVectorDrawable) context.getDrawable(R.drawable.ic_close_animated);

            LinearLayout contentLayout = user_page.findViewById(R.id.user_page_front);
            friends = user_page.findViewById(R.id.user_page_friends_list);

            ViewGroup parent_friends = (ViewGroup) user_page.findViewById(R.id.user_page_friends_list);

            if (friends_list.size() != 0) {
                for (int i = 0; i < 10; i++) {
                    parent_friends.addView(new FriendCard(context, String.valueOf(i), context.getDrawable(R.mipmap.ic_launcher), inflater, null).getPage());
                }
            }

            ViewGroup parent_survey = (ViewGroup) user_page.findViewById(R.id.user_page_survey_list);

            if (user_surveys.size() != 0) {
                for (int i = 0; i < 10; i++) {
                    parent_survey.addView(new SurveyCard(context, String.valueOf(i), context.getDrawable(R.mipmap.ic_launcher), inflater, null).getPage());
                }
            }

            ViewPager viewPager = user_page.findViewById(R.id.user_page_view_pager);
            viewPager.setAdapter(new MyFragmentPagerAdapter(getActivity().getSupportFragmentManager(), context));

            TabLayout tabLayout = user_page.findViewById(R.id.user_page_tab_layout);
            tabLayout.setupWithViewPager(viewPager);

            CircleImageView imageView = user_page.findViewById(R.id.user_page_image);
            imageView.setImageDrawable(context.getDrawable(R.mipmap.ic_launcher));

            sheetBehavior = BottomSheetBehavior.from(contentLayout);
            sheetBehavior.setFitToContents(false);
            sheetBehavior.setHideable(false);
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            // user info
            AppDatabase appDatabase = AppDatabase.getDatabase(context);
            User user = appDatabase.userDao().getAllUsers().get(0);
            TextView full_name = user_page.findViewById(R.id.user_page_full_name);
            full_name.setText(user.second_name + " " + user.first_name);

            toolbar.setTitle("@" + user.login);

            MaterialButton change_data = user_page.findViewById(R.id.user_page_change_data);
            change_data.setOnClickListener(v -> editProfile());

            manager = (InputMethodManager) MainActivity.get().getSystemService(Activity.INPUT_METHOD_SERVICE);

            page = this;
            return user_page;
        } else {
            return page.getUser_page();
        }
    }

    private void editProfile() {
        changeState();
        TabLayout tabLayout = user_page.findViewById(R.id.user_page_tab_layout);
        tabLayout.getTabAt(0).select();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void changeState() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            toolbar.setNavigationIcon(menu);
            menu.start();
        } else {
            UserPageSettings.changeState();
            manager.hideSoftInputFromWindow(page.getView().getWindowToken(), 0);
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            toolbar.setNavigationIcon(close);
            close.start();
        }
    }

    public View getUser_page() {
        return user_page;
    }

    public void updateFriendsList(Vector<Friend> friends) {
        ViewGroup parent = (ViewGroup) user_page.findViewById(R.id.user_page_friends_list);
        parent.removeAllViews();
        for (Friend friend : friends) {
            parent.addView(
                    new FriendCard(getContext(),
                            String.valueOf(friend.friend_id),
                            internalStorage.load(friend.friend_id,
                                    InternalStorage.PROFILE_IMAGE),
                            inflater,
                            null).getPage()
            );
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.toolbar_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void startChooseImage() {
        TedImagePicker.with(getContext()).
                title("Выберите изображение").
                buttonText("Выбрать").
                mediaType(MediaType.IMAGE).
                image().
                start(new OnSelectedListener() {
                    @Override
                    public void onSelected(@NonNull Uri uri) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                            UserPageSettings.changeUserImage(bitmap);
                        } catch (IOException e) {
                            Toast.makeText(getContext(), "Не получилось открыть изображение, попробуйте ещё раз", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void setLoginToken(String login, String token) {

    }

    public static Vector<SurveyCard> getUser_surveys() {
        return user_surveys;
    }

    public static void setUser_surveys(Vector<SurveyCard> user_surveys) {
        UserPage.user_surveys = user_surveys;
    }

    public static Vector<SurveyCard> getFavorites_surveys() {
        return favorites_surveys;
    }

    public static void setFavorites_surveys(Vector<SurveyCard> favorites_surveys) {
        UserPage.favorites_surveys = favorites_surveys;
    }

    public static Vector<SurveyCard> getArchive_surveys() {
        return archive_surveys;
    }

    public static void setArchive_surveys(Vector<SurveyCard> archive_surveys) {
        UserPage.archive_surveys = archive_surveys;
    }
}