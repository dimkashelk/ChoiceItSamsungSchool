package com.example.choiceitsamsungschool.main_page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.choiceitsamsungschool.APIServer;
import com.example.choiceitsamsungschool.AppDatabase;
import com.example.choiceitsamsungschool.R;
import com.example.choiceitsamsungschool.db.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;
import java.util.Vector;

import br.com.simplepass.loadingbutton.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("StaticFieldLeak")
public class UserPageSettings extends View {
    private final static String ALPHABET_RU_LARGE = "ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ";
    private final static String ALPHABET_RU_SMALL = "йцукенгшщзхъфывапролджэячсмитьбю";
    private final static String ALPHABET_RU = ALPHABET_RU_LARGE + ALPHABET_RU_SMALL;
    private final static String ALPHABET_EN_LARGE = "QWERTYUIOPASDFGHJKLZXCVBNM";
    private final static String ALPHABET_EN_SMALL = "qwertyuiopasdfghjklzxcvbnm";
    private final static String ALPHABET_EN = ALPHABET_EN_LARGE + ALPHABET_EN_SMALL;
    private final static String DIGITS = "0123456789";
    private final static String SYMBOLS = "!@#$%,^:&?()*/-+[]";

    public static final String FIRST_NAME = "first_name";
    private static boolean is_edit_first_name = false;
    public static final String SECOND_NAME = "second_name";
    private static boolean is_edit_second_name = false;
    public static final String LOGIN = "login";
    private static boolean is_edit_login = false;
    public static final String OLD_PASSWORD = "old_password";
    private static boolean is_edit_old_password = false;
    public static final String NEW_PASSWORD = "new_password";
    private static boolean is_edit_new_password = false;
    public static final String RE_NEW_PASSWORD = "re_new_password";
    private static boolean is_edit_re_new_password = false;

    private static APIServer apiServer;

    private static View page;
    public static UserPage userPage;
    private static boolean profile_image_to_save = false;
    private static Bitmap profile_image = null;

    private static TextInputEditText first_name;
    private static TextInputEditText second_name;
    private static TextInputEditText login_name;

    private static TextInputLayout first_name_layout;
    private static TextInputLayout second_name_layout;
    private static TextInputLayout login_layout;

    private static TextInputEditText old_password;
    private static TextInputEditText new_password;
    private static TextInputEditText re_new_password;

    private static TextInputLayout old_password_layout;
    private static TextInputLayout new_password_layout;
    private static TextInputLayout re_new_password_layout;

    public UserPageSettings(Context context) {
        super(context);
    }

    public static View get(Context context, LayoutInflater inflater, ViewGroup container) {
        if (page == null) {
            View view = inflater.inflate(R.layout.user_page_settings, container, false);
            page = view;

            page.findViewById(R.id.settings_page_edit_profile_image).setOnClickListener(v -> userPage.startChooseImage());

            page.findViewById(R.id.settings_page_reset).setOnClickListener(v -> resetAll());

            page.findViewById(R.id.settings_page_save).setOnClickListener(v -> saveChanges());

            apiServer = APIServer.getSingletonAPIServer();

            first_name = page.findViewById(R.id.settings_page_edit_first_name);
            second_name = page.findViewById(R.id.settings_page_edit_second_name);
            login_name = page.findViewById(R.id.settings_page_edit_login);

            first_name_layout = page.findViewById(R.id.settings_page_edit_first_name_layout);
            second_name_layout = page.findViewById(R.id.settings_page_edit_second_name_layout);
            login_layout = page.findViewById(R.id.settings_page_edit_login_layout);

            old_password = page.findViewById(R.id.settings_page_edit_old_password);
            new_password = page.findViewById(R.id.settings_page_edit_password);
            re_new_password = page.findViewById(R.id.settings_page_edit_re_password);

            old_password_layout = page.findViewById(R.id.settings_page_edit_old_password_layout);
            new_password_layout = page.findViewById(R.id.settings_page_edit_password_layout);
            re_new_password_layout = page.findViewById(R.id.settings_page_edit_re_password_layout);

            AppDatabase appDatabase = AppDatabase.getDatabase(context);
            User user = appDatabase.userDao().getAllUsers().get(0);

            first_name.setText(user.first_name);
            second_name.setText(user.second_name);
            login_name.setText(user.login);
        }
        return page;
    }

    private static void saveChanges() {
        if (page == null) {
            return;
        }
        SaveUserChanges saver = new SaveUserChanges();

        User user = AppDatabase.getDatabase(page.getContext()).userDao().getAllUsers().get(0);

        first_name_layout.setErrorEnabled(false);
        second_name_layout.setErrorEnabled(false);
        login_layout.setErrorEnabled(false);

        old_password_layout.setErrorEnabled(false);
        new_password_layout.setErrorEnabled(false);
        re_new_password_layout.setErrorEnabled(false);

        boolean is_first_name_ok = checkStringToAnotherChars(first_name.getText().toString(), ALPHABET_RU);
        boolean is_second_name_ok = checkStringToAnotherChars(second_name.getText().toString(), ALPHABET_RU);
        boolean is_login_ok = checkStringToAnotherChars(login_name.getText().toString(), ALPHABET_EN_SMALL + DIGITS);

        if (!first_name.getText().toString().equals("") &&
                !second_name.getText().toString().equals("") &&
                !login_name.getText().toString().equals("")) {
            if (is_first_name_ok &&
                    is_second_name_ok &&
                    is_login_ok &&
                    (!user.first_name.equals(first_name.getText().toString()) ||
                            !user.second_name.equals(second_name.getText().toString()) ||
                            !user.login.equals(login_name.getText().toString()))) {
                saver.setFirst_name(first_name.getText().toString());
                saver.setSecond_name(second_name.getText().toString());
                saver.setLogin(login_name.getText().toString());
            } else {
                if (!is_first_name_ok) {
                    first_name_layout.setError("Имя должно содержать только а-я, А-Я");
                    first_name_layout.setErrorEnabled(true);
                }
                if (!is_second_name_ok) {
                    second_name_layout.setError("Фамилия должна содержать только а-я, А-Я");
                    second_name_layout.setErrorEnabled(true);
                }
                if (!is_login_ok) {
                    login_layout.setError("Логин должен содержать только a–z, 0–9");
                    login_layout.setErrorEnabled(true);
                }
                return;
            }
        } else {
            if (first_name.getText().toString().equals("")) {
                first_name_layout.setError("Имя не может быть пустым");
                first_name_layout.setErrorEnabled(true);
            }
            if (second_name.getText().toString().equals("")) {
                second_name_layout.setError("Фамилия не может быть пустой");
                second_name_layout.setErrorEnabled(true);
            }
            if (login_name.getText().toString().equals("")) {
                login_layout.setError("Логин не может быть пустым");
                login_layout.setErrorEnabled(true);
            }
            return;
        }

        boolean is_old_password_empty = old_password.getText().toString().equals("");
        boolean is_new_password_empty = new_password.getText().toString().equals("");
        boolean is_re_new_password_empty = re_new_password.getText().toString().equals("");

        if (!is_old_password_empty &&
                !is_new_password_empty &&
                !is_re_new_password_empty) {
            if (checkNewPassword()) {
                saver.setOld_password(old_password.getText().toString());
                saver.setNew_password(new_password.getText().toString());
            }
            return;
        } else {
            if (is_old_password_empty &&
                    is_new_password_empty &&
                    is_re_new_password_empty) {
                // Nothing, user don't change password
            } else {
                if (is_old_password_empty) {
                    old_password_layout.setError("Заполните поле");
                    old_password_layout.setErrorEnabled(true);
                }
                if (is_new_password_empty) {
                    new_password_layout.setError("Заполните поле");
                    new_password_layout.setErrorEnabled(true);
                }
                if (is_re_new_password_empty) {
                    re_new_password_layout.setError("Заполните поле");
                    re_new_password_layout.setErrorEnabled(true);
                }
                return;
            }
        }
        if (profile_image_to_save) {
            saver.setProfile_image(profile_image);
        }

        CircularProgressButton button = page.findViewById(R.id.settings_page_save);
        button.startAnimation();

        first_name_layout.setEnabled(false);
        second_name_layout.setEnabled(false);
        login_layout.setEnabled(false);

        old_password_layout.setEnabled(false);
        new_password_layout.setEnabled(false);
        re_new_password_layout.setEnabled(false);

        APIServer apiServer = APIServer.getSingletonAPIServer();

        saver.execute(apiServer.getLogin(), apiServer.getToken());
    }

    private static boolean checkNewPassword() {
        old_password_layout.setErrorEnabled(false);
        new_password_layout.setErrorEnabled(false);
        re_new_password_layout.setErrorEnabled(false);

        boolean old_password_ok = old_password.getText().toString().equals("");
        boolean new_password_ok = new_password.getText().toString().equals("");
        boolean re_new_password_ok = re_new_password.getText().toString().equals("");

        if (old_password_ok && new_password_ok && re_new_password_ok) {
            // All fields is empty
            return false;
        } else {
            // At least one field is not empty
            if (!old_password_ok && !new_password_ok && !re_new_password_ok) {
                // Nothing do if all password fields not empty
            } else {
                if (old_password_ok) {
                    old_password_layout.setError("Заполните поле");
                    old_password_layout.setErrorEnabled(true);
                }
                if (new_password_ok) {
                    new_password_layout.setError("Заполните поле");
                    new_password_layout.setErrorEnabled(true);
                }
                if (re_new_password_ok) {
                    re_new_password_layout.setError("Заполните поле");
                    re_new_password_layout.setErrorEnabled(true);
                }
                return false;
            }
        }

        old_password_ok = checkStringToAnotherChars(
                old_password.getText().toString(),
                ALPHABET_EN + DIGITS + SYMBOLS
        );
        new_password_ok = checkStringToAnotherChars(
                new_password.getText().toString(),
                ALPHABET_EN + DIGITS + SYMBOLS
        );
        re_new_password_ok = checkStringToAnotherChars(
                re_new_password.getText().toString(),
                ALPHABET_EN + DIGITS + SYMBOLS
        );

        if (old_password_ok || new_password_ok || re_new_password_ok) {
            if (old_password_ok && new_password_ok && re_new_password_ok) {
                if (!new_password.getText().toString().equals(re_new_password.getText().toString())) {
                    re_new_password_layout.setError("Пароли должны совпадать");
                    re_new_password_layout.setErrorEnabled(true);
                    return false;
                }
            } else {
                if (!old_password_ok) {
                    old_password_layout.setError("Пароль должен содержать только a–z, A–Z, 0–9, !@#$%,^:&amp;?()*/-+[]");
                    old_password_layout.setErrorEnabled(true);
                }
                if (!new_password_ok) {
                    new_password_layout.setError("Пароль должен содержать только a–z, A–Z, 0–9, !@#$%,^:&amp;?()*/-+[]");
                    new_password_layout.setErrorEnabled(true);
                }
                if (!re_new_password_ok) {
                    re_new_password_layout.setError("Пароль должен содержать только a–z, A–Z, 0–9, !@#$%,^:&amp;?()*/-+[]");
                    re_new_password_layout.setErrorEnabled(true);
                }
                return false;
            }
        }
        return true;
    }

    private static boolean checkStringToAnotherChars(String stringToCheck, String alphabet) {
        Vector<Character> stringToCheck_chars = new Vector<>();
        for (int i = 0; i < stringToCheck.length(); i++) {
            stringToCheck_chars.add(stringToCheck.charAt(i));
        }
        for (Character i : stringToCheck_chars) {
            if (!alphabet.contains(i.toString())) {
                return false;
            }
        }
        return true;
    }

    private static void resetAll() {
        if (page == null) {
            return;
        }
        List<User> user = AppDatabase.getDatabase(page.getContext()).userDao().getAllUsers();
        User current_user = user.get(0);

        first_name.setText(current_user.first_name);
        second_name.setText(current_user.second_name);
        login_name.setText(current_user.login);

        first_name_layout.setErrorEnabled(false);
        second_name_layout.setErrorEnabled(false);
        login_layout.setErrorEnabled(false);

        old_password.setText("");
        new_password.setText("");
        re_new_password.setText("");

        old_password_layout.setErrorEnabled(false);
        new_password_layout.setErrorEnabled(false);
        re_new_password_layout.setErrorEnabled(false);
    }

    public static void changeUserImage(Bitmap image) {
        if (page == null) {
            return;
        }
        CircleImageView circularImageView = page.findViewById(R.id.settings_page_edit_profile_image);
        circularImageView.setImageDrawable(new BitmapDrawable(null, image));

        profile_image_to_save = true;
        profile_image = image;
    }

    public static void freeLogin() {
        login_layout.setErrorEnabled(false);
    }


    public static void notFreeLogin() {
        login_layout.setErrorEnabled(true);
        login_layout.setError("Логин занят, используйте другой");
    }

    public static void stopLoadingButton(String text) {
        Toast.makeText(page.getContext(), text, Toast.LENGTH_SHORT).show();
        CircularProgressButton button = page.findViewById(R.id.settings_page_save);
        button.revertAnimation();

        first_name_layout.setEnabled(true);
        second_name_layout.setEnabled(true);
        login_layout.setEnabled(true);

        old_password_layout.setEnabled(true);
        new_password_layout.setEnabled(true);
        re_new_password_layout.setEnabled(true);
    }
}