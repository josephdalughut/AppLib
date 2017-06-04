/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ng.joey.lib.android.gui.crop;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageButton;

import ng.joey.lib.android.R;
import ng.joey.lib.android.gui.fragment.Fragtivity;
import ng.joey.lib.java.util.Value;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.CountDownLatch;

/*
 * Modified from original in AOSP.
 */
public class CropManager extends Fragtivity {

    private Media.CropResultListener cropResultListener;

    public static CropManager of(Intent intent, Media.CropResultListener resultListener){
        return new CropManager().setIntent(intent).setCropResultListener(resultListener);
    }


    public CropManager setIntent(Intent intent){
        this.result = intent; return this;
    }

    public CropManager setCropResultListener(Media.CropResultListener listener) {
        this.cropResultListener = listener; return this;
    }

    private static final int SIZE_DEFAULT = 2048;
    private static final int SIZE_LIMIT = 4096;

    private final Handler handler = new Handler();

    private int aspectX;
    private int aspectY;

    // Output image
    private int maxX, maxY, resultCode = Activity.RESULT_CANCELED;
    private int exifRotation;

    private Uri sourceUri;
    private Uri saveUri;

    private boolean isSaving;

    private int sampleSize;
    private RotateBitmap rotateBitmap;
    private CropImageView imageView;
    private HighlightView cropView;

    private Intent result;

    public void setupViews() {
        imageView = (CropImageView) findViewById(R.id.crop_image);
        imageView.setCropManager(this);
        imageView.setRecycler(new ImageViewTouchBase.Recycler() {
            @Override
            public void recycle(Bitmap b) {
                b.recycle();
            }
        });

        findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultCode = Activity.RESULT_CANCELED;
                finish();
            }
        });

        final FloatingActionButton done = (FloatingActionButton)findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveClicked();
            }
        });
        done.postDelayed(new Runnable() {
            @Override
            public void run() {
                done.show();
            }
        }, 400);

        loadInput();

    }

    private void loadInput() {
        Bundle extras = result.getExtras();

        if (extras != null) {
            aspectX = extras.getInt(Crop.Extra.ASPECT_X);
            aspectY = extras.getInt(Crop.Extra.ASPECT_Y);
            maxX = extras.getInt(Crop.Extra.MAX_X);
            maxY = extras.getInt(Crop.Extra.MAX_Y);
            saveUri = extras.getParcelable(MediaStore.EXTRA_OUTPUT);
        }

        switch (result.getIntExtra(Crop.Extra.SOURCE, Crop.GALLERY)){
            case Crop.GALLERY:case Crop.FILE:
                sourceUri = result.getData();
                if (sourceUri != null) {
                    exifRotation = CropUtil.getExifRotation(CropUtil.getFromMediaUri(
                            CropManager.this.getActivity(), getContext().getApplicationContext().getContentResolver(), sourceUri));

                    InputStream is = null;
                    try {
                        sampleSize = calculateBitmapSampleSize(sourceUri);
                        is = getActivity().getApplicationContext().getContentResolver().openInputStream(sourceUri);
                        BitmapFactory.Options option = new BitmapFactory.Options();
                        option.inSampleSize = sampleSize;
                        rotateBitmap = new RotateBitmap(BitmapFactory.decodeStream(is, null, option), exifRotation);
                    } catch (IOException e) {
                        Crop.Log.d("Error reading image: " + e.getMessage());
                        setResultException(e);
                    } catch (OutOfMemoryError e) {
                        Crop.Log.d("OOM reading image: " + e.getMessage());
                        setResultException(e);
                    } finally {
                        CropUtil.closeSilently(is);
                    }
                }

                break;
            case Crop.CAMERA:
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                try {
                    sampleSize = calculateBitmapSampleSize(imageBitmap);
                    rotateBitmap = new RotateBitmap(imageBitmap, 0);
                }catch (IOException e){
                    imageBitmap.recycle();
                }
                break;
        }

        if (rotateBitmap == null) {
            finish();
            return;
        }
        startCrop();
    }

    private int calculateBitmapSampleSize(Uri bitmapUri) throws IOException {
        InputStream is = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            is = getActivity().getApplicationContext().getContentResolver().openInputStream(bitmapUri);
            BitmapFactory.decodeStream(is, null, options); // Just get image size
        } finally {
            CropUtil.closeSilently(is);
        }

        int maxSize = getMaxImageSize();
        int sampleSize = 1;
        while (options.outHeight / sampleSize > maxSize || options.outWidth / sampleSize > maxSize) {
            sampleSize = sampleSize << 1;
        }
        return sampleSize;
    }

    private int calculateBitmapSampleSize(Bitmap bitmap) throws IOException {
        int maxSize = getMaxImageSize();
        int sampleSize = 1;
        while (bitmap.getHeight() / sampleSize > maxSize || bitmap.getWidth() / sampleSize > maxSize) {
            sampleSize = sampleSize << 1;
        }
        return sampleSize;
    }

    private int getMaxImageSize() {
        int textureLimit = getMaxTextureSize();
        if (textureLimit == 0) {
            return SIZE_DEFAULT;
        } else {
            return Math.min(textureLimit, SIZE_LIMIT);
        }
    }

    private int getMaxTextureSize() {
        // The OpenGL texture size is the maximum size that can be drawn in an ImageView
        int[] maxSize = new int[1];
        GLES10.glGetIntegerv(GLES10.GL_MAX_TEXTURE_SIZE, maxSize, 0);
        return maxSize[0];
    }

    private void startCrop() {
        if (isRemoving()) {
            return;
        }
        imageView.setImageRotateBitmapResetBase(rotateBitmap, true);
        CropUtil.startBackgroundJob(getActivity(), getResources().getString(R.string.loading),
                new Runnable() {
                    public void run() {
                        final CountDownLatch latch = new CountDownLatch(1);
                        handler.post(new Runnable() {
                            public void run() {
                                if (imageView.getScale() == 1F) {
                                    imageView.center();
                                }
                                latch.countDown();
                            }
                        });
                        try {
                            latch.await();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        new Cropper().crop();
                    }
                }, handler
        );
    }

    private class Cropper {

        private void makeDefault() {
            if (rotateBitmap == null) {
                return;
            }

            HighlightView hv = new HighlightView(imageView);
            final int width = rotateBitmap.getWidth();
            final int height = rotateBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            // Make the default size about 4/5 of the width or height
            int cropWidth = Math.min(width, height) * 4 / 5;
            @SuppressWarnings("SuspiciousNameCombination")
            int cropHeight = cropWidth;

            if (aspectX != 0 && aspectY != 0) {
                if (aspectX > aspectY) {
                    cropHeight = cropWidth * aspectY / aspectX;
                } else {
                    cropWidth = cropHeight * aspectX / aspectY;
                }
            }

            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
            hv.setup(imageView.getUnrotatedMatrix(), imageRect, cropRect, aspectX != 0 && aspectY != 0);
            imageView.add(hv);
        }

        public void crop() {
            handler.post(new Runnable() {
                public void run() {
                    makeDefault();
                    imageView.invalidate();
                    if (imageView.highlightViews.size() == 1) {
                        cropView = imageView.highlightViews.get(0);
                        cropView.setFocus(true);
                    }
                }
            });
        }
    }

    private void onSaveClicked() {
        if (cropView == null || isSaving) {
            return;
        }
        isSaving = true;

        Bitmap croppedImage;
        Rect r = cropView.getScaledCropRect(sampleSize);
        int width = r.width();
        int height = r.height();

        int outWidth = width;
        int outHeight = height;
        if (maxX > 0 && maxY > 0 && (width > maxX || height > maxY)) {
            float ratio = (float) width / (float) height;
            if ((float) maxX / (float) maxY > ratio) {
                outHeight = maxY;
                outWidth = (int) ((float) maxY * ratio + .5f);
            } else {
                outWidth = maxX;
                outHeight = (int) ((float) maxX / ratio + .5f);
            }
        }

        try {
            croppedImage = decodeRegionCrop(r, outWidth, outHeight);
        } catch (IllegalArgumentException e) {
            setResultException(e);
            finish();
            return;
        }

        if (croppedImage != null) {
            imageView.setImageRotateBitmapResetBase(new RotateBitmap(croppedImage, exifRotation), true);
            imageView.center();
            imageView.highlightViews.clear();
        }
        saveImage(croppedImage);
    }

    private void saveImage(Bitmap croppedImage) {
        if (croppedImage != null) {
            final Bitmap b = croppedImage;
            CropUtil.startBackgroundJob(getActivity(), getResources().getString(R.string.saving),
                    new Runnable() {
                        public void run() {
                            saveOutput(b);
                        }
                    }, handler
            );
        } else {
            finish();
        }
    }

    private Bitmap decodeRegionCrop(Rect rect, int outWidth, int outHeight) {
        // Release memory now
        clearImageView();

        InputStream is = null;
        Bitmap croppedImage = null;
        try {
            is = getActivity().getApplicationContext().getContentResolver().openInputStream(sourceUri);
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is, false);
            final int width = decoder.getWidth();
            final int height = decoder.getHeight();

            if (exifRotation != 0) {
                // Adjust crop area to account for image rotation
                Matrix matrix = new Matrix();
                matrix.setRotate(-exifRotation);

                RectF adjusted = new RectF();
                matrix.mapRect(adjusted, new RectF(rect));

                // Adjust to account for origin at 0,0
                adjusted.offset(adjusted.left < 0 ? width : 0, adjusted.top < 0 ? height : 0);
                rect = new Rect((int) adjusted.left, (int) adjusted.top, (int) adjusted.right, (int) adjusted.bottom);
            }

            try {
                croppedImage = decoder.decodeRegion(rect, new BitmapFactory.Options());
                if (croppedImage != null && (rect.width() > outWidth || rect.height() > outHeight)) {
                    Matrix matrix = new Matrix();
                    matrix.postScale((float) outWidth / rect.width(), (float) outHeight / rect.height());
                    croppedImage = Bitmap.createBitmap(croppedImage, 0, 0, croppedImage.getWidth(), croppedImage.getHeight(), matrix, true);
                }
            } catch (IllegalArgumentException e) {
                // Rethrow with some extra information
                throw new IllegalArgumentException("Rectangle " + rect + " is outside of the image ("
                        + width + "," + height + "," + exifRotation + ")", e);
            }

        } catch (IOException e) {
            Crop.Log.d("Error cropping image: " + e.getMessage());
            setResultException(e);
        } catch (OutOfMemoryError e) {
            Crop.Log.d("OOM cropping image: " + e.getMessage());
            setResultException(e);
        } finally {
            CropUtil.closeSilently(is);
        }
        return croppedImage;
    }

    private void clearImageView() {
        imageView.clear();
        if (rotateBitmap != null) {
            rotateBitmap.recycle();
        }
        System.gc();
    }

    private void saveOutput(Bitmap croppedImage) {
        if (saveUri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getActivity().getApplicationContext().getContentResolver().openOutputStream(saveUri);
                if (outputStream != null) {
                    croppedImage.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
                }
            } catch (IOException e) {
                setResultException(e);
                Crop.Log.d("Cannot open file: " + saveUri + " "+e.getMessage());
            } finally {
                CropUtil.closeSilently(outputStream);
            }

            CropUtil.copyExifRotation(
                    CropUtil.getFromMediaUri(getActivity().getApplicationContext(), getActivity().getApplicationContext().getContentResolver(), sourceUri),
                    CropUtil.getFromMediaUri(getActivity().getApplicationContext(), getActivity().getApplicationContext().getContentResolver(), saveUri)
            );

            setResultUri(saveUri);
        }

        final Bitmap b = croppedImage;
        handler.post(new Runnable() {
            public void run() {
                imageView.clear();
                b.recycle();
            }
        });
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    private void setResultException(Throwable e){
        result = new Intent();
        result.putExtra(Crop.Extra.ERROR, e.getMessage());
    }

    private void setResultUri(Uri uri){
        resultCode = Activity.RESULT_OK;
        result = new Intent();
        result.setData(uri);
    }

    private void finish(){
        Crop.Log.d("Finishing fragment");
        if(!Value.IS.nullValue(cropResultListener)) {
            Crop.Log.d("Activity result listener is still "+ cropResultListener.toString());
            final String filePath = Media.UriParser.toFilePath(getContext(), saveUri);
           getRootView().post(new Runnable() {
               @Override
               public void run() {
                   Crop.Log.d("Dispatching to "+ cropResultListener.toString());
                   if(!Value.IS.emptyValue(filePath)) {
                       cropResultListener.onResult(filePath);
                   }else {
                       cropResultListener.onError();
                   }
               }
           });
        }
    }


    @Override
    public int layoutId() {
        return R.layout.fragment_crop_manager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rotateBitmap != null) {
            rotateBitmap.recycle();
        }
    }


    @Override
    public void findViews() {
        Crop.Log.d("Finding Views");
    }

    public boolean isSaving() {
        return isSaving;
    }


}
