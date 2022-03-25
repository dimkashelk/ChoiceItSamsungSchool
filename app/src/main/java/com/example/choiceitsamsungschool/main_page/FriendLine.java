package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.choiceitsamsungschool.R;
import com.example.choiceitsamsungschool.db.Friend;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendLine extends View {
    private View page;
    private Friend friend;
    private Drawable image;
    private LayoutInflater inflater;
    private FriendsPage friendsPage;

    public FriendLine(Context context) {
        super(context);
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    public FriendLine(Context context, Friend friend, Drawable image, LayoutInflater inflater, FriendsPage friendsPage) {
        super(context);

        this.friend = friend;
        this.image = image;
        this.inflater = inflater;
        this.friendsPage = friendsPage;

        page = inflater.inflate(R.layout.friend_line, null);

        page.setOnClickListener(v -> openFriend());

        CircleImageView imageView = page.findViewById(R.id.friend_line_image);
        imageView.setImageDrawable(image);

        TextView full_name = page.findViewById(R.id.friend_line_full_name);
        full_name.setText(friend.first_name + " " + friend.second_name);
    }

    public void openFriend() {
        Toast.makeText(getContext(), "Открыли страницу друга", Toast.LENGTH_LONG).show();
    }

    public View getPage() {
        return page;
    }
}
