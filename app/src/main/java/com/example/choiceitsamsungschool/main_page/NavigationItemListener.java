package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.choiceitsamsungschool.R;
import com.example.choiceitsamsungschool.db.Friend;
import com.example.choiceitsamsungschool.db.Person;
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
    private static NavigationItemListener listener = null;
    private Fragment last_fragment;

    public NavigationItemListener(AppActivity mainActivity, BottomNavigationView bottomNavigationView) {
        this.mainActivity = mainActivity;
        this.bottomNavigationView = bottomNavigationView;
        manager = mainActivity.getSupportFragmentManager();

        homePage = new HomePage();
        friendsPage = new FriendsPage();
        createPage = new CreatePage();
        searchPage = new SearchPage();
        userPage = new UserPage();

        listener = this;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.page_main:
                manager.beginTransaction().replace(R.id.content, homePage).commit();
                last_fragment = homePage;
                break;
            case R.id.page_friends:
                manager.beginTransaction().replace(R.id.content, friendsPage).commit();
                last_fragment = friendsPage;
                break;
            case R.id.page_create:
                manager.beginTransaction().replace(R.id.content, createPage).commit();
                last_fragment = createPage;
                break;
            case R.id.page_search:
                manager.beginTransaction().replace(R.id.content, searchPage).commit();
                last_fragment = searchPage;
                break;
            case R.id.page_user:
                manager.beginTransaction().replace(R.id.content, userPage).commit();
                last_fragment = userPage;
                break;
        }
        return true;
    }

    public static NavigationItemListener get() {
        return listener;
    }

    public void openPersonPage(Person person) {
        PersonPage personPage = new PersonPage(person);
        manager.beginTransaction().replace(R.id.content, personPage).commit();
    }

    public void openPersonPage(Friend friend) {
        PersonPage personPage = new PersonPage(friend);
        manager.beginTransaction().replace(R.id.content, personPage).commit();
    }

    public void closePersonPage() {
        manager.beginTransaction().replace(R.id.content, last_fragment).commit();
    }
}
