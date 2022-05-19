package com.example.choiceitsamsungschool.main_page;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

import com.example.choiceitsamsungschool.AppDatabase;
import com.example.choiceitsamsungschool.R;
import com.example.choiceitsamsungschool.db.Friend;
import com.example.choiceitsamsungschool.db.Person;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendCard extends View {
    private String friend_id;
    private Drawable image;
    private View page;
    private UserPage userPage;

    public FriendCard(Context context, String friend_id, Drawable image, LayoutInflater inflater, UserPage userPage) {
        super(context);

        this.friend_id = friend_id;
        this.image = image;
        this.userPage = userPage;

        page = inflater.inflate(R.layout.friend_card, null);
        CircleImageView imageView = page.findViewById(R.id.friend_image);
        imageView.setImageDrawable(image);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openFriend();
            }
        });
    }

    private void openFriend() {
        AppDatabase appDatabase = AppDatabase.getDatabase(getContext());
        List<Friend> friend = appDatabase.friendDao().getFriend(friend_id);
        if (friend.size() != 0) {
            NavigationItemListener.get().openPersonPage(friend.get(0));
        }
    }

    public View getPage() {
        return page;
    }
}
