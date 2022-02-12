package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.choiceitsamsungschool.R;
import com.makeramen.roundedimageview.RoundedImageView;

public class FriendCard extends View {
    private String friend_id;
    private Drawable image;
    private View page;

    public FriendCard(Context context, String friend_id, Drawable image, LayoutInflater inflater, ViewGroup container) {
        super(context);

        this.friend_id = friend_id;
        this.image = image;

        page = inflater.inflate(R.layout.friend_card, null);
        RoundedImageView imageView = page.findViewById(R.id.friend_image);
        imageView.setImageDrawable(image);
    }

    public View getPage() {
        return page;
    }
}
