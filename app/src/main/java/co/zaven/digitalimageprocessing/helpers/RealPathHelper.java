package co.zaven.digitalimageprocessing.helpers;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.net.Uri;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 * Created on 21.03.2016.
 *
 * @author Sławomir Onyszko
 */
public class RealPathHelper {

    @SuppressLint("NewApi")
      public static String getRealPathFromUriApi19(Context context, Uri uri){
        String filePath = "";
        String wholeId = DocumentsContract.getDocumentId(uri);

        // Split at colon, use second item in the array
        String id = wholeId.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String selection = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, selection, new String[]{ id }, null);

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromUriApi21(Uri uri){
        return DocumentsContract.getDocumentId(uri);
    }

    @SuppressLint("NewApi")
    public static String getRealPathFromUriApi11to18(Context context, Uri contentUri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        String result = null;

        CursorLoader cursorLoader = new CursorLoader( context, contentUri, projection, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if(cursor != null){
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(columnIndex);
        }
        return result;
    }

    public static String getRealPathFromUriBelowApi11(Context context, Uri contentUri){
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(columnIndex);
    }
}