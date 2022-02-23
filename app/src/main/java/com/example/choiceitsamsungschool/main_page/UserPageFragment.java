package com.example.choiceitsamsungschool.main_page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class UserPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static UserPageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        UserPageFragment fragment = new UserPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getInt(ARG_PAGE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        if (mPage == 0) {
            // Settings
            view = UserPageSettings.get(getActivity().getApplicationContext(), inflater, container);
        } else if (mPage == 1) {
            // Favorite
            view = UserPageFavorites.get(getActivity().getApplicationContext(), inflater, container);
        } else {
            // Archive
            view = UserPageArchive.get(getActivity().getApplicationContext(), inflater, container);
        }
        return view;
    }
}
