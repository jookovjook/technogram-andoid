package com.jookovjook.imagepicker.utils;
/**
 * Created by guillermoguerrero on 2/6/16.
 */

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageUtils {

    private static final String TAG = "[ScreenUtils]";

    private ImageUtils() {
        //empty contructor
    }

    public static void loadImageFromUri(Context context, String imageUri, ImageView imageView, int width, int height) {
        try {
            Glide.with(context).load(imageUri).override(width, height).into(imageView);
        } catch (Exception e) {
            Log.e(TAG, "[loadImageFromUri]", e);
        }
    }
}
