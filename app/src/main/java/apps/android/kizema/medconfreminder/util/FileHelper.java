package apps.android.kizema.medconfreminder.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import apps.android.kizema.medconfreminder.App;
import apps.android.kizema.medconfreminder.auth.helpers.ImageConstatnts;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class FileHelper {
    private static final String TAG = FileHelper.class.toString();

    public static File getImageFileJPG() {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + ImageConstatnts.APP_FOLDER_IMAGES);
        dir.mkdirs();

        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        return new File(dir, fileName);
    }

    public static File getImageFilePNG() {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + ImageConstatnts.APP_FOLDER_IMAGES);
        dir.mkdirs();

        String fileName = String.format("%d.png", System.currentTimeMillis());
        return new File(dir, fileName);
    }

    public static File getImageFileJPEG() {
        File sdCard = Environment.getExternalStorageDirectory();
        File dir = new File(sdCard.getAbsolutePath() + ImageConstatnts.APP_FOLDER_IMAGES);
        dir.mkdirs();

        String fileName = String.format("%d.jpg", System.currentTimeMillis());
        return new File(dir, fileName);
    }

    public static void writeBinaryToFile(File outFile, byte[]... data) {
        try {
            FileOutputStream outStream = new FileOutputStream(outFile);
            outStream.write(data[0]);
            outStream.flush();
            outStream.close();

            Log.d(TAG, "onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    public static boolean fileExists(String file) {
        if (file == null || file.length() == 0) {
            return false;
        }

        File f = new File(file);
        if (f.exists()) {
            return true;
        } else
            return false;
    }

    public static File writeRawToFile(int rawRes, String fileName) {
        File file = new File(App.getContext().getFilesDir() + File.separator + fileName);
        try {
            InputStream inputStream = App.getContext().getResources().openRawResource(rawRes);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            byte buf[] = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) {
                fileOutputStream.write(buf, 0, len);
            }

            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e1) {
        }

        return file;
    }

    public static String readFileAsString(String filePath) {

        String result = "";
        File file = new File(filePath);
        if (file.exists()) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                char current;
                while (fis.available() > 0) {
                    current = (char) fis.read();
                    result = result + String.valueOf(current);

                }

            } catch (Exception e) {
            } finally {
                if (fis != null)
                    try {
                        fis.close();
                    } catch (IOException ignored) {
                    }
            }
        }
        return result;
    }

    //TODO not needed
    public static String getThumbnailPath(Context context, String path) {
        long imageId = -1;

        String[] projection = new String[]{MediaStore.MediaColumns._ID};
        String selection = MediaStore.MediaColumns.DATA + "=?";
        String[] selectionArgs = new String[]{path};
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, null);
        if (cursor != null && cursor.moveToFirst()) {
            imageId = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
        }

        String result = null;
        cursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(context.getContentResolver(), imageId, MediaStore.Images.Thumbnails.MINI_KIND, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
            cursor.close();
        }

        return result;
    }

    //TODO not needed
    public static String getPath(Context c, Uri uri) {
        Cursor cursor = MediaStore.Images.Thumbnails.queryMiniThumbnails(
                c.getContentResolver(), uri,
                MediaStore.Images.Thumbnails.MINI_KIND,
                null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();//**EDIT**
            String uri1 = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
            return uri1;
        }

        return null;
    }
}
