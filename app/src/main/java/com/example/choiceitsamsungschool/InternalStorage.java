package com.example.choiceitsamsungschool;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class InternalStorage {
    public static final String PROFILE_IMAGE = "profile";
    public static final String SURVEY_TITLE_IMAGE = "survey_title";

    private String internalStorageDir;
    private String profile_dir = "/profile/";
    private String survey_dir = "/survey/";
    private final String _profile = "_profile";
    private final String _survey_title = "_survey_title";
    private final String format = ".png";

    private static InternalStorage internalStorage = null;

    public InternalStorage(File filesDir) {
        internalStorageDir = filesDir.getAbsolutePath();

        File profile = new File(internalStorageDir + profile_dir);
        if (!profile.exists()) {
            profile.mkdirs();
        }

        File survey = new File(internalStorageDir + survey_dir);
        if (!survey.exists()) {
            survey.mkdirs();
        }
    }

    public static InternalStorage getInternalStorage() {
        if (internalStorage == null) {
            internalStorage = new InternalStorage(Environment.getExternalStorageDirectory());
        }
        return internalStorage;
    }

    public void saveUserProfileImage(Bitmap image, String id) {
        try {
            String file_dir = internalStorageDir + profile_dir;
            File dir = new File(file_dir);
            File file = new File(dir, id + _profile + format);
            FileOutputStream fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception ignored) {
        }
    }

    public void removeFriendsProfileImage(String id) {
        try {
            String file_dir = internalStorageDir + profile_dir;
            File dir = new File(file_dir);
            File file = new File(dir, id + _profile + format);
            file.delete();
        } catch (Exception ignored) {
        }
    }

    public Drawable load(String id, String mode) {
        String path;
        switch (mode) {
            case InternalStorage.PROFILE_IMAGE:
                path = internalStorageDir + profile_dir + id + _profile + format;
                return Drawable.createFromPath(path);
            case InternalStorage.SURVEY_TITLE_IMAGE:
                path = internalStorageDir + survey_dir + id + _survey_title + format;
                return Drawable.createFromPath(path);
        }
        return null;
    }

    public void saveSurveyTitleImage(Bitmap image, String id) {
        try {
            String file_dir = internalStorageDir + survey_dir;
            File dir = new File(file_dir);
            File file = new File(dir, id + _survey_title + format);
            FileOutputStream fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception ignored) {
        }
    }

    public void removeSurveyTitleImage(String id) {
        try {
            String file_dir = internalStorageDir + survey_dir;
            File dir = new File(file_dir);
            File file = new File(dir, id + _survey_title + format);
            file.delete();
        } catch (Exception ignored) {
        }
    }
}
