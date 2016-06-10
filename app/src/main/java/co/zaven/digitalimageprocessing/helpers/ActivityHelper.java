package co.zaven.digitalimageprocessing.helpers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

/**
 * @author Sławomir Onyszko
 */
public class ActivityHelper {

    /**
     * Log tag.
     */
    private static final String TAG = ActivityHelper.class.getSimpleName();

    /**
     * Starts an activity with flags:
     * <p/>
     * {@link Intent#FLAG_ACTIVITY_SINGLE_TOP}
     * {@link Intent#FLAG_ACTIVITY_NO_HISTORY}
     * {@link Intent#FLAG_ACTIVITY_NEW_TASK}
     * {@link Intent#FLAG_ACTIVITY_CLEAR_TASK}
     *
     * @param from an activity starts from.
     * @param to   type of activity which will be started.
     */
    public static void startActivityWithNoHistory(Activity from, Class<?> to) {
        Intent intent = new Intent(from, to);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        from.startActivity(intent);
    }

    /**
     * Starts specified activity.
     *
     * @param from an activity starts from.
     * @param to   type of activity which will be started.
     */
    public static void startActivity(Activity from, Class<?> to) {
        Intent intent = new Intent(from, to);
        from.startActivity(intent);
    }

    /**
     * Starts specified activity.
     *
     * @param from   an activity starts from.
     * @param to     type of activity which will be started.
     * @param bundle bundle with data.
     */
    public static void startActivity(Activity from, Class<?> to, Bundle bundle) {
        Intent intent = new Intent(from, to);
        intent.putExtras(bundle);
        from.startActivity(intent);
    }

    /**
     * Starts native gallery.
     *
     * @param from        an activity starts from.
     * @param requestCode the request code.
     */
    public static void startNativeGallery(Activity from, int requestCode) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        from.startActivityForResult(intent, requestCode);
    }

    /**
     * Sets toolbar with up navigation.
     *
     * @param activity an activity which toolbar will set on.
     * @param toolbar  {@link Toolbar}
     */
    public static void setupToolbar(AppCompatActivity activity, Toolbar toolbar) {
        try {
            activity.setSupportActionBar(toolbar);
            //noinspection ConstantConditions
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

}
