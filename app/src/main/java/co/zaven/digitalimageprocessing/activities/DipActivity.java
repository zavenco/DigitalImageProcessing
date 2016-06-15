package co.zaven.digitalimageprocessing.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.opencv.android.OpenCVLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.zaven.digitalimageprocessing.R;
import co.zaven.digitalimageprocessing.helpers.ActivityHelper;

public class DipActivity extends BaseActivity {

    private static final String TAG = DipActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.root)
    CoordinatorLayout root;

    @Bind(R.id.sample_image)
    ImageView sampleImage;

    static {
        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCV initialize success");
        } else {
            Log.i(TAG, "OpenCV initialize failed");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dip);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        requestPermission();

        Glide.with(this).load(R.drawable.sun_mountain).into(sampleImage);

    }

    @OnClick(R.id.select_option)
    void onClick() {
        ActivityHelper.startActivity(this, ProcessingOptionsActivity.class);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar.make(root, getResources().getString(R.string.request_permission_rationale), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getResources().getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(DipActivity.this, requestedPermissions, PERMISSIONS_REQUEST);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, requestedPermissions, PERMISSIONS_REQUEST);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  @NonNull final String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(root, getResources().getString(R.string.request_permission_rationale), Snackbar.LENGTH_INDEFINITE)
                            .setAction(getResources().getString(R.string.ok), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ActivityCompat.requestPermissions(DipActivity.this, requestedPermissions, PERMISSIONS_REQUEST);
                                }
                            }).show();
                }
            }
        }
    }
}
