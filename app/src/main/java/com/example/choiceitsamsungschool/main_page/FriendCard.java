package com.example.choiceitsamsungschool.main_page;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.choiceitsamsungschool.R;
import com.makeramen.roundedimageview.RoundedImageView;

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
        RoundedImageView imageView = page.findViewById(R.id.friend_image);
        imageView.setImageDrawable(image);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openFriend();
            }
        });
    }

    private void openFriend() {
        Toast.makeText(getContext(), "Открыли страницу друга", Toast.LENGTH_LONG).show();
    }

    public View getPage() {
        return page;
    }
}
