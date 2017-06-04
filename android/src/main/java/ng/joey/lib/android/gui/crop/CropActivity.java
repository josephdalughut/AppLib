package ng.joey.lib.android.gui.crop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;

import ng.joey.lib.android.R;
import ng.joey.lib.java.util.Value;

/**
 * Created by Joey Dalughut on 8/14/16 at 3:19 PM,
 * Project: Litigy Libraries.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public class CropActivity extends AppCompatActivity {

    private static Media.CropResultListener resultListener;

    public static void delegate(Media.CropResultListener resultListener){
        CropActivity.resultListener = resultListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Crop.Log.d("CropActivity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        Intent intent = getIntent();
        switch (intent.getIntExtra(Crop.Extra.SOURCE, Crop.GALLERY)){
            case Crop.GALLERY:
                Crop.Log.d("Crop intent: pick from gallery");
                Media.startImagePickIntent(this, Crop.GALLERY);
                break;
            case Crop.CAMERA:
                Crop.Log.d("Crop intent: capture from camera");
                Media.startImageCaptureIntent(this, Crop.CAMERA);
                break;
            case Crop.FILE:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, CropManager.of(getIntent(), new Media.CropResultListener() {
                    @Override
                    public void onResult(String filePath) {
                        Crop.Log.d("Crop finish, file path is "+filePath);
                        finish();
                        resultListener.onResult(filePath);
                        releaseListener();
                    }

                    @Override
                    public void onError() {
                        Crop.Log.d("Crop finished with error");
                        finish();
                        resultListener.onError();
                        releaseListener();
                    }
                })).commitAllowingStateLoss();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Crop.Log.d("Crop activity result");
        if(resultCode!=RESULT_OK ){
            switch (requestCode){
                case Crop.GALLERY:
                    if(Value.IS.ANY.nullValue(data, data.getData())){
                        resultListener.onError();
                        Crop.Log.d("Crop activity result error!");
                        return;
                    }
                    getIntent().setData(data.getData());
                    break;
                case Crop.CAMERA:
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    if(Value.IS.nullValue(imageBitmap)){
                        resultListener.onError();
                        Crop.Log.d("Crop activity result error!");
                        return;
                    }
                    getIntent().putExtras(extras);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container, CropManager.of(getIntent(), new Media.CropResultListener() {
                @Override
                public void onResult(String filePath) {
                    Crop.Log.d("Crop finish, file path is "+filePath);
                    finish();
                    resultListener.onResult(filePath);
                    releaseListener();
                }

                @Override
                public void onError() {
                    Crop.Log.d("Crop finished with error");
                    finish();
                    resultListener.onError();
                    releaseListener();
                }
            })).commitAllowingStateLoss();
        }

    }

    private void releaseListener(){
        resultListener = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        resultListener = null;
    }
}
