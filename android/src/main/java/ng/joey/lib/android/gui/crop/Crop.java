package ng.joey.lib.android.gui.crop;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

/**
 * Created by Joey Dalughut on 8/14/16 at 2:46 PM,
 * Project: Litigy Libraries.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public class Crop {

    public static final int REQUEST_CROP = 6709;
    public static final int REQUEST_PICK = 9162;
    public static final int RESULT_ERROR = 404;

    public static final int CAMERA = 1;
    public static final int GALLERY = 2;
    public static final int FILE = 3;

    interface Extra {
        String ASPECT_X = "aspect_x";
        String ASPECT_Y = "aspect_y";
        String MAX_X = "max_x";
        String MAX_Y = "max_y";
        String ERROR = "error";
        String SOURCE = "source";
    }

    private Intent cropIntent;

    /**
     * Create a crop Intent builder with source and destination image Uris
     *
     * @param destination Uri for saving the cropped image
     */
    public static Crop to(Uri destination) {
        return new Crop(destination);
    }

    private Crop(Uri destination) {
        cropIntent = new Intent();
        fromGallery();
        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, destination);
    }

    /**
     * Set fixed aspect ratio for crop area
     *
     * @param x Aspect X
     * @param y Aspect Y
     */
    public Crop withAspect(int x, int y) {
        cropIntent.putExtra(Extra.ASPECT_X, x);
        cropIntent.putExtra(Extra.ASPECT_Y, y);
        return this;
    }

    /**
     * Crop area with fixed 1:1 aspect ratio
     */
    public Crop asSquare() {
        cropIntent.putExtra(Extra.ASPECT_X, 1);
        cropIntent.putExtra(Extra.ASPECT_Y, 1);
        return this;
    }

    public Crop fromCamera(){
        cropIntent.putExtra(Extra.SOURCE, CAMERA);
        return this;
    }

    public Crop fromGallery(){
        cropIntent.putExtra(Extra.SOURCE, GALLERY);
        return this;
    }

    public Crop fromFile(File file){
        cropIntent.putExtra(Extra.SOURCE, FILE);
        cropIntent.setData(Uri.fromFile(file));
        return this;
    }

    /**
     * Crop area with fixed 3:2 aspect ratio
     */
    public Crop asCover() {
        cropIntent.putExtra(Extra.ASPECT_X, 3);
        cropIntent.putExtra(Extra.ASPECT_Y, 2);
        return this;
    }

    /**
     * Set maximum crop size
     *
     * @param width  Max width
     * @param height Max height
     */
    public Crop withMaxSize(int width, int height) {
        cropIntent.putExtra(Extra.MAX_X, width);
        cropIntent.putExtra(Extra.MAX_Y, height);
        return this;
    }


    public void start(Context context, Media.CropResultListener cropResultListener){
        //CropManager.of(cropIntent, cropResultListener).start();
        cropIntent.setClass(context, CropActivity.class);
        CropActivity.delegate(cropResultListener);
        context.startActivity(cropIntent);
    }

    public static class Log {
        public static void d(String message){
            android.util.Log.d(LOG_TAG, message);
        }
    }

    public static final String LOG_TAG = "litigy.crop";

}
