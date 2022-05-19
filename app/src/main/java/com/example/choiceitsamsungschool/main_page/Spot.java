package com.example.choiceitsamsungschool.main_page;

import android.graphics.drawable.Drawable;

public class Spot {
    public int id;
    public String title;
    public Drawable drawable;

    public Spot(int id, String title, Drawable drawable) {
        this.id = id;
        this.title = title;
        this.drawable = drawable;
    }

    public Drawable getDrawable() {
        return drawable;
    }

    public String getTitle() {
        return title;
    }
}
