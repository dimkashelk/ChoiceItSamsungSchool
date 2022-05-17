package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.example.choiceitsamsungschool.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddLine extends View {
    private final static String ALPHABET_RU_LARGE = "ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ";
    private final static String ALPHABET_RU_SMALL = "йцукенгшщзхъфывапролджэячсмитьбю";
    private final static String ALPHABET_RU = ALPHABET_RU_LARGE + ALPHABET_RU_SMALL;
    private final static String DIGITS = "0123456789";
    private final static String SYMBOLS = "!@#$%,^:&?()*/-+[]";

    private Bitmap image;
    private View page;
    private CreatePage createPage;
    private MaterialButton delete;
    private AddLine addLine;
    private TextInputLayout titleInputLayout;
    private TextInputEditText titleInput;
    private Context context;

    @SuppressLint("InflateParams")
    public AddLine(Context context, Bitmap image, LayoutInflater inflater, CreatePage createPage) {
        super(context);

        this.image = image;
        this.createPage = createPage;
        this.addLine = this;
        this.context = context;

        page = inflater.inflate(R.layout.add_line, null);

        CircleImageView imageView = page.findViewById(R.id.add_line_image);
        imageView.setImageDrawable(new BitmapDrawable(getResources(), image));

        delete = page.findViewById(R.id.add_line_delete);
        delete.setOnClickListener(v -> createPage.deleteLine(addLine));

        titleInput = page.findViewById(R.id.add_line_title);
        titleInput.addTextChangedListener(new TextInputWatcher(this));

        titleInputLayout = page.findViewById(R.id.add_line_title_layout);
    }

    public View getPage() {
        return page;
    }

    public void checkTitle() {
        String s = titleInput.getText().toString();
        String alphabet = ALPHABET_RU + DIGITS + SYMBOLS + " ";
        for (int i = 0; i < s.length(); i++) {
            if (!alphabet.contains(String.valueOf(s.charAt(i)))) {
                titleInputLayout.setErrorEnabled(true);
                return;
            }
        }
        titleInputLayout.setErrorEnabled(false);
    }

    public boolean isEmptyTitle() {
        return titleInput.getText().toString().isEmpty();
    }

    public Bitmap getBitmap() {
        return image;
    }

    public String getTitle() {
        return titleInput.getText().toString();
    }

    public void startShake(Animation animation) {
        titleInputLayout.startAnimation(animation);
    }
}
