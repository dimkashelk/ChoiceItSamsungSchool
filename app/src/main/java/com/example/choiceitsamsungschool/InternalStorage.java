package com.example.choiceitsamsungschool;

import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class InternalStorage {
    private MainActivity mainActivity;

    private String internalStorageDir;
    private String profile_dir = "/profile/";
    private final String _profile = "_profile";
    private final String format = ".png";

    public InternalStorage(MainActivity mainActivity, File filesDir) {
        this.mainActivity = mainActivity;
        internalStorageDir = filesDir.getAbsolutePath();

        File profile = new File(internalStorageDir + profile_dir);
        if (!profile.exists()) {
            profile.mkdirs();
        }
    }

    public void save_user_profile_image(Bitmap image, String id) {
        try {
            String file_dir = internalStorageDir + profile_dir;
            File dir = new File(file_dir);
            File file = new File(dir, id + _profile + format);
            FileOutputStream fOut = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            mainActivity.getInternalStoragePermission();
        }
    }
}
