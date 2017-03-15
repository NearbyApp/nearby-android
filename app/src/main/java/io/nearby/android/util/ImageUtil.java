package io.nearby.android.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.DrawableRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatDrawableManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * Created by Marc on 2017-01-29.
 */

public class ImageUtil {

    private ImageUtil(){}

    public static Bitmap vectorDrawableToBitmap(Context context, @DrawableRes int drawableId){
        Drawable drawable = AppCompatDrawableManager.get().getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static File createImageFile(Context context) throws IOException{
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMG_" + timeStamp ;

        // creates the folder nearby in pictures if it doesn't exists
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Nearby");
        folder.mkdir();

        // creates the file for the bitmap
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Nearby",imageFileName + ".jpg");

        file.createNewFile();

        return file;
    }

    public static Bitmap createBitmapFromFile(File file){
        return createBitmapFromFile(file.getAbsolutePath());
    }

    public static Bitmap createBitmapFromFile(String filePath){
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, bmOptions);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, bmOptions);
    }

    /**
     * Compresses a JPEG with a compression value of 75.
     * @param file The file to compress (Only Jpeg are supported)
     * @return file of the compressed image or null if the image was not compressed.
     */
    public static File compressBitmap(File file){
        File compressedPictureFile = null;

        // Create Streams
        FileOutputStream fileOutputStream;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        // Create a bitmap from the passed file
        Bitmap bitmap = createBitmapFromFile(file.getAbsolutePath());
        // Compress the bitmap
        boolean compressed = bitmap.compress(Bitmap.CompressFormat.JPEG,75,stream);

        try {
            if(compressed){

                String fileName = file.getName().split("\\.")[0] + "_temp";
                File tempFile = File.createTempFile(fileName, ".jpg");

                fileOutputStream = new FileOutputStream(tempFile);
                fileOutputStream.write(stream.toByteArray());

                fileOutputStream.flush();
                fileOutputStream.close();

                stream.flush();
                stream.close();

                compressedPictureFile = tempFile;
            }
        } catch (IOException e) {
            Timber.e(e);
        }

        return compressedPictureFile;
    }
}
