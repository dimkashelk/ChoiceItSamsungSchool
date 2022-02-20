package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.choiceitsamsungschool.APIServer;
import com.example.choiceitsamsungschool.InternalStorage;
import com.example.choiceitsamsungschool.R;
import com.example.choiceitsamsungschool.db.Friend;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Vector;

public class UserPage extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static UserPage page = null;
    private View user_page;
    private BottomSheetBehavior sheetBehavior;
    private MaterialButton button;
    private LinearLayout friends;
    private APIServer apiServer;
    private LayoutInflater inflater;
    private InternalStorage internalStorage;

    @Nullable
    @Override
    @SuppressLint({"CutPasteId", "UseCompatLoadingForDrawables"})
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (page == null) {
            user_page = inflater.inflate(R.layout.user_page, container, false);
            this.inflater = inflater;
            Context context = getContext();

            apiServer = APIServer.getSingletonAPIServer();
            apiServer.setUserPage(this);
            apiServer.loadUserData();

            button = user_page.findViewById(R.id.user_page_button);
            LinearLayout contentLayout = user_page.findViewById(R.id.user_page_front);
            friends = user_page.findViewById(R.id.user_page_friends_list);

            ViewGroup parent_friends = (ViewGroup) user_page.findViewById(R.id.user_page_friends_list);

            for (int i = 0; i < 10; i++) {
                parent_friends.addView(new FriendCard(context, String.valueOf(i), context.getDrawable(R.mipmap.ic_launcher), inflater, null).getPage());
            }

            ViewGroup parent_survey = (ViewGroup) user_page.findViewById(R.id.user_page_survey_list);

            for (int i = 0; i < 10; i++) {
                parent_survey.addView(new SurveyCard(context, String.valueOf(i), context.getDrawable(R.mipmap.ic_launcher), inflater, null).getPage());
            }

            ViewGroup parent_favorite_survey = (ViewGroup) user_page.findViewById(R.id.user_page_survey_list_favorite);

            for (int i = 0; i < 10; i++) {
                SurveyCard card = new SurveyCard(context, String.valueOf(i), context.getDrawable(R.mipmap.ic_launcher), inflater, null);
                card.setFavorite();
                parent_favorite_survey.addView(card.getPage());
            }
            RoundedImageView imageView = user_page.findViewById(R.id.user_page_image);
            imageView.setImageDrawable(context.getDrawable(R.mipmap.ic_launcher));

            sheetBehavior = BottomSheetBehavior.from(contentLayout);
            sheetBehavior.setFitToContents(false);
            sheetBehavior.setHideable(false);
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeState();
                }
            });
            page = this;
            return user_page;
        } else {
            return page.getUser_page();
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void changeState() {
        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            button.setIcon(getResources().getDrawable(R.drawable.ic_close, null));
        } else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            button.setIcon(getResources().getDrawable(R.drawable.ic_menu, null));
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
}