package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.choiceitsamsungschool.R;
import com.example.choiceitsamsungschool.db.Friend;
import com.example.choiceitsamsungschool.db.Person;
import com.example.choiceitsamsungschool.db.Survey;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationItemListener implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager manager;
    private AppActivity mainActivity;
    private HomePage homePage;
    private FriendsPage friendsPage;
    private CreatePage createPage;
    private SearchPage searchPage;
    private UserPage userPage;
    private PersonPage personPage;
    private SurveyPage surveyPage;
    private static NavigationItemListener listener = null;
    private int last_fragment;
    private ViewPager2 viewPager;
    private ViewPagerAdapter adapter;

    public NavigationItemListener(AppActivity mainActivity, BottomNavigationView bottomNavigationView) {
        this.mainActivity = mainActivity;
        this.bottomNavigationView = bottomNavigationView;
        manager = mainActivity.getSupportFragmentManager();

        homePage = new HomePage();
        friendsPage = new FriendsPage();
        createPage = new CreatePage();
        searchPage = new SearchPage();
        userPage = new UserPage();
        personPage = new PersonPage();
        surveyPage = new SurveyPage();

        viewPager = mainActivity.findViewById(R.id.content);
        viewPager.setOffscreenPageLimit(10);
        viewPager.setUserInputEnabled(false);

        adapter = new ViewPagerAdapter(mainActivity.getSupportFragmentManager(), mainActivity.getLifecycle());
        adapter.addFragment(homePage);
        adapter.addFragment(friendsPage);
        adapter.addFragment(createPage);
        adapter.addFragment(searchPage);
        adapter.addFragment(userPage);
        adapter.addFragment(personPage);
        adapter.addFragment(surveyPage);

        viewPager.setAdapter(adapter);

        listener = this;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.page_main:
                viewPager.setCurrentItem(0, false);
                last_fragment = 0;
                break;
            case R.id.page_friends:
                viewPager.setCurrentItem(1, false);
                last_fragment = 1;
                break;
            case R.id.page_create:
                viewPager.setCurrentItem(2, false);
                last_fragment = 2;
                break;
            case R.id.page_search:
                viewPager.setCurrentItem(3, false);
                last_fragment = 3;
                break;
            case R.id.page_user:
                viewPager.setCurrentItem(4, false);
                last_fragment = 4;
                break;
        }
        return true;
    }

    public static NavigationItemListener get() {
        return listener;
    }

    public void openPersonPage(Person person) {
        viewPager.setCurrentItem(6, false);
    }

    public void openPersonPage(Friend friend) {
        viewPager.setCurrentItem(6, false);
    }

    public void openSurvey(Survey survey) {
        surveyPage.setSurvey(survey);
        viewPager.setCurrentItem(6, false);
    }

    public void closePersonPage() {
        viewPager.setCurrentItem(last_fragment, false);
    }

    public void closeSurvey() {
        viewPager.setCurrentItem(last_fragment, false);
    }
}
