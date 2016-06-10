package co.zaven.digitalimageprocessing.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;

/**
 * Created on 15.03.2016.
 *
 * @author Sławomir Onyszko
 */
public class BitmapHelper {

    private BitmapHelper() {
    }

    public static byte[] decodeByteFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        return baos.toByteArray();
    }

    public static void showBitmap(Context context, Bitmap bitmap, ImageView imageView) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] data = baos.toByteArray();
        Glide.with(context).load(data).into(imageView);
    }

    public static Bitmap readBitmapFromPath(Context context, Uri path) throws Exception {
        InputStream stream = context.getContentResolver().openInputStream(path);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        if (stream != null) {
            stream.close();
        }
        return bitmap;
    }

    public static void writeToPublicDirectory(String filename, byte[] data, String directory, String environmentDirectory) throws Exception {
        File publicDirectory = new File(Environment.getExternalStoragePublicDirectory(environmentDirectory), directory);
        boolean result = publicDirectory.mkdirs();
        File targetFile = new File(publicDirectory, filename);
        FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
        fileOutputStream.write(data);
        fileOutputStream.close();
    }

    public static void writeToPublicDirectory(String filename, String string, String directory, String environmentDirectory) throws Exception {
        File publicDirectory = new File(Environment.getExternalStoragePublicDirectory(environmentDirectory), directory);
        boolean result = publicDirectory.mkdirs();
        File file = new File(publicDirectory, filename);
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        outputStreamWriter.write(string);
        outputStreamWriter.close();
    }

}
