package ng.joey.lib.android.gui.crop;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import ng.joey.lib.java.util.Time;
import ng.joey.lib.java.util.Value;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;


/**
 * Created by Joey Dalughut on 8/14/16 at 2:46 PM,
 * Project: Litigy Libraries.
 * Copyright (c) 2016 LITIGY. All rights reserved.
 * http://www.litigy.com
 */
public class Media {
    public static boolean canKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    public abstract static class CropResultListener {

        public abstract void onResult(String filePath);
        public abstract void onError();

    }

    private static Intent getImagePickIntent(Context context){
        Intent intent;
        if(canKitKat){
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        }else{
            intent = new Intent(Intent.ACTION_PICK);
        }
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            return intent;
        }
        return null;
    }

    private static Intent getImageCaptureIntent(Context context){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            return intent;
        }
        return null;
    }

    public static int startImagePickIntent(Activity activity, int requestCode){
        Intent intent = getImagePickIntent(activity);
        activity.startActivityForResult(intent, requestCode);
        return requestCode;
    }

    public static int startImageCaptureIntent(Activity activity, int requestCode){
        Intent intent = getImageCaptureIntent(activity);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, createImageFile(activity))
        activity.startActivityForResult(intent, requestCode);
        return requestCode;
    }

    private static File createImageFile(Context context) throws IOException {
        String fileName = "IMG_"+Value.TO.stringValue(Time.now())+"_";
        File file = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imgFile = File.createTempFile(fileName, ".jpg", file);
        return imgFile;
    }

    /**
     * A class for getting the real uri for the content uri returned by Intent.ACTION_OPEN_DOCUMENT or Intent.ACTION_PICK
     */
    public static class UriParser {

        /**
         * @param contentUri the content uri returned by Intent.ACTION_OPEN_DOCUMENT or Intent.ACTION_PICK
         * @return the real file path of the item referenced by the {@param contentUri}
         */
        public static String toFilePath(final Context context, Uri contentUri){
            try {
                return new AsyncTask<Uri, Void, String>(){

                    @Override
                    protected String doInBackground(Uri... params) {
                        if(canKitKat){
                            return UriParser.getKitKat(context, params[0]);
                        }else{
                            return UriParser.get(context, params[0]);
                        }
                    }
                }.execute(contentUri).get();
            } catch (InterruptedException e) {
                return null;
            } catch (ExecutionException e) {
                return null;
            }
        };


        /** for devices with api <= 11
         private static String get11(Uri uri){
         String[] filePathColumn = { MediaStore.Images.Media.DATA };
         Cursor cursor = CBActivity.get().managedQuery(uri, filePathColumn, null, null, null);
         cursor.moveToFirst();
         int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
         String filePath = cursor.getString(columnIndex);
         cursor.close();
         return filePath;
         }
         **/

        /**
         * For devices running Android Versions < KITKAT but > HONEYCOMB
         * @param contentUri the uri to work with
         * @return a String whose path is that of the real file represented by the passed in {@param contentUri}
         */
        private static String get(Context context, Uri contentUri){
            String[] projection = { MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(context, contentUri,
                    projection, null, null, null);
            Cursor cursor = loader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }

        /**
         * For devices running Android Versions >= KITKAT
         * @param uri the uri to work with
         * @return a String whose path is that of the real file represented by the passed in {@param uri}
         */
        @TargetApi(Build.VERSION_CODES.KITKAT)
        private static String getKitKat(Context context, Uri uri){
            if (DocumentsContract.isDocumentUri(context, uri)) {

                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/"
                                + split[1];
                    }
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"),
                            Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[] { split[1] };

                    return getDataColumn(context, contentUri, selection,
                            selectionArgs);
                }
            }
            // MediaStore (and general)
            else if ("content".equalsIgnoreCase(uri.getScheme())) {

                // Return the remote address
                if (isGooglePhotosUri(uri))
                    return uri.getLastPathSegment();

                return getDataColumn(context, uri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }

            return null;
        }

        public static boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri
                    .getAuthority());
        }

        public static boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri
                    .getAuthority());
        }

        public static boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri
                    .getAuthority());
        }

        public static boolean isGooglePhotosUri(Uri uri) {
            return "com.google.android.apps.photos.content".equals(uri
                    .getAuthority());
        }
        public static String getDataColumn(Context context, Uri uri,
                                           String selection, String[] selectionArgs) {

            Cursor cursor = null;
            final String column = "_data";
            final String[] projection = { column };

            try {
                cursor = context.getContentResolver().query(uri, projection,
                        selection, selectionArgs, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }
    }

}
