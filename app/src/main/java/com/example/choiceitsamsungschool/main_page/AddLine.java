package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

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

    private Drawable image;
    private View page;
    private CreatePage createPage;
    private MaterialButton delete;
    private AddLine addLine;
    private TextInputLayout titleInputLayout;
    private TextInputEditText titleInput;

    @SuppressLint("InflateParams")
    public AddLine(Context context, Drawable image, LayoutInflater inflater, CreatePage createPage) {
        super(context);

        this.image = image;
        this.createPage = createPage;
        this.addLine = this;

        page = inflater.inflate(R.layout.add_line, null);

        CircleImageView imageView = page.findViewById(R.id.add_line_image);
        imageView.setImageDrawable(image);

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
}
