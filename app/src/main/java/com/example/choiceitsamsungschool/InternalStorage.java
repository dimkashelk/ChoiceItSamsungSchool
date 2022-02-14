package com.example.choiceitsamsungschool;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileOutputStream;

public class InternalStorage {
    public static final String PROFILE_IMAGE = "profile";

    private String internalStorageDir;
    private String profile_dir = "/profile/";
    private final String _profile = "_profile";
    private final String format = ".png";

    private static InternalStorage internalStorage = null;

    public InternalStorage(File filesDir) {
        internalStorageDir = filesDir.getAbsolutePath();

        File profile = new File(internalStorageDir + profile_dir);
        if (!profile.exists()) {
            profile.mkdirs();
        }
    }

    public static InternalStorage getInternalStorage(File filesDir) {
        if (internalStorage == null) {
            internalStorage = new InternalStorage(filesDir);
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

    public void removeFriendsProfileImages(String id) {
        try {
            String file_dir = internalStorageDir + profile_dir;
            File dir = new File(file_dir);
            File file = new File(dir, id + _profile + format);
            file.delete();
        } catch (Exception ignored) {
        }
    }

    public Drawable load(String path, String mode) {
        switch (mode) {
            case InternalStorage.PROFILE_IMAGE:
                return Drawable.createFromPath(path);
            default:
                return null;
        }
    }
}
