package com.example.choiceitsamsungschool.main_page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.choiceitsamsungschool.R;

import java.util.Vector;

public class UserPageFragment extends Fragment {
    public static final String ARG_PAGE = "ARG_PAGE";
    public Vector<SurveyCard> cards = new Vector<>();

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
        View view = inflater.inflate(R.layout.user_page_favorites, container, false);
        ViewGroup parent_favorite_survey = (ViewGroup) view.findViewById(R.id.user_page_favorites_list);
        for (int i = 0; i < 10; i++) {
            parent_favorite_survey.addView(new SurveyCard(getActivity().getApplicationContext(), String.valueOf(i), getActivity().getApplicationContext().getDrawable(R.mipmap.ic_launcher), inflater, null).getPage());
        }
        return view;
    }
}

















