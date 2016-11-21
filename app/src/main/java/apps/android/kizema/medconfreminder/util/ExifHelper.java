package apps.android.kizema.medconfreminder.util;

import android.media.ExifInterface;

import java.io.IOException;

/**
 * Created by A.Kizema on 22.11.2016.
 */

public class ExifHelper {
    public static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }

    public static int getPhotoRotation(String filename){
        int rotationInDegrees = 0;
        ExifInterface exif = null;

        try {
            exif = new ExifInterface( filename);
            int rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            rotationInDegrees = ExifHelper.exifToDegrees(rotation);
        } catch (IOException e) {
        }

        return rotationInDegrees;
    }
}
