package co.zaven.digitalimageprocessing.activities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.zaven.digitalimageprocessing.R;
import co.zaven.digitalimageprocessing.helpers.ActivityHelper;
import co.zaven.digitalimageprocessing.helpers.BitmapHelper;

public class DetectEdgesActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.image_view)
    ImageView imageView;

    @Bind(R.id.detect_edges_image_view)
    ImageView detectEdgesImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_edges);
        ButterKnife.bind(this);
        ActivityHelper.setupToolbar(this, toolbar);

        Uri path = getIntent().getExtras().getParcelable(KEY_BITMAP);
        try {
            detectEdges(BitmapHelper.readBitmapFromPath(this, path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void detectEdges(Bitmap bitmap) {
        Mat rgba = new Mat();
        Utils.bitmapToMat(bitmap, rgba);

        Mat edges = new Mat(rgba.size(), CvType.CV_8UC1);
        Imgproc.cvtColor(rgba, edges, Imgproc.COLOR_RGB2GRAY, 4);
        Imgproc.Canny(edges, edges, 80, 100);

        // Don't do that at home or work it's for visualization purpose.
        BitmapHelper.showBitmap(this, bitmap, imageView);
        Bitmap resultBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(edges, resultBitmap);
        BitmapHelper.showBitmap(this, resultBitmap, detectEdgesImageView);
    }

}
