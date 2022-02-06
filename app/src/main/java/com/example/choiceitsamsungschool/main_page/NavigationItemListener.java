package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.example.choiceitsamsungschool.MainActivity;
import com.example.choiceitsamsungschool.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationItemListener implements BottomNavigationView.OnNavigationItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private FragmentManager manager;
    private MainActivity mainActivity;
    private HomePage homePage;
    private FriendsPage friendsPage;
    private CreatePage createPage;
    private SearchPage searchPage;

    public NavigationItemListener(MainActivity mainActivity, BottomNavigationView bottomNavigationView) {
        this.mainActivity = mainActivity;
        this.bottomNavigationView = bottomNavigationView;
        manager = mainActivity.getSupportFragmentManager();

        homePage = new HomePage();
        friendsPage = new FriendsPage();
        createPage = new CreatePage();
        searchPage = new SearchPage();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.page_main:
                manager.beginTransaction().replace(R.id.content, homePage).commit();
                break;
            case R.id.page_friends:
                manager.beginTransaction().replace(R.id.content, friendsPage).commit();
                break;
            case R.id.page_create:
                manager.beginTransaction().replace(R.id.content, createPage).commit();
                break;
            case R.id.page_search:
                manager.beginTransaction().replace(R.id.content, searchPage).commit();
                break;
//                    case R.id.page_user:
//                        getSupportFragmentManager().beginTransaction().replace(R.id.content, userBackFragment).commit();
//                        break;
        }
        return true;
    }
}
