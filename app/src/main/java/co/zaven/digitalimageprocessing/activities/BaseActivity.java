package co.zaven.digitalimageprocessing.activities;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;

/**
 * Created on 18.03.2016.
 *
 * @author Sławomir Onyszko
 */
public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();

    protected String[] requestedPermissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    protected static final String KEY_BITMAP = "IMAGE_PATH";
    protected static final int SELECT_PICTURE = 1;
    protected static final int PERMISSIONS_REQUEST = 9;

}
