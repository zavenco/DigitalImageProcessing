package co.zaven.digitalimageprocessing.activities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.zaven.digitalimageprocessing.R;
import co.zaven.digitalimageprocessing.helpers.ActivityHelper;
import co.zaven.digitalimageprocessing.helpers.BitmapHelper;
import co.zaven.digitalimageprocessing.helpers.HistogramHelper;

public class HistogramActivity extends BaseActivity {

    private static final String TAG = HistogramActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.image_view)
    ImageView imageView;

    @Bind(R.id.histogram_image_view)
    ImageView histogramView;

    Uri path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histogram);
        ButterKnife.bind(this);
        ActivityHelper.setupToolbar(this, toolbar);

        path = getIntent().getExtras().getParcelable(KEY_BITMAP);
        try {
            drawHistogram(BitmapHelper.readBitmapFromPath(this, path));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawHistogram(Bitmap bitmap) {
        try {
            Mat rgba = new Mat();
            Utils.bitmapToMat(bitmap, rgba);

            Size rgbaSize = rgba.size();
            int histSize = 256;
            MatOfInt histogramSize = new MatOfInt(histSize);

            int histogramHeight = (int) rgbaSize.height;
            int binWidth = 5;

            MatOfFloat histogramRange = new MatOfFloat(0f, 256f);

            Scalar[] colorsRgb = new Scalar[]{new Scalar(200, 0, 0, 255), new Scalar(0, 200, 0, 255), new Scalar(0, 0, 200, 255)};
            MatOfInt[] channels = new MatOfInt[]{new MatOfInt(0), new MatOfInt(1), new MatOfInt(2)};

            Mat[] histograms = new Mat[]{new Mat(), new Mat(), new Mat()};
            Mat histMatBitmap = new Mat(rgbaSize, rgba.type());

            for (int i = 0; i < channels.length; i++) {
                Imgproc.calcHist(Collections.singletonList(rgba), channels[i], new Mat(), histograms[i], histogramSize, histogramRange);
                Core.normalize(histograms[i], histograms[i], histogramHeight, 0, Core.NORM_INF);
                for (int j = 0; j < histSize; j++) {
                    Point p1 = new Point(binWidth * (j - 1), histogramHeight - Math.round(histograms[i].get(j - 1, 0)[0]));
                    Point p2 = new Point(binWidth * j, histogramHeight - Math.round(histograms[i].get(j, 0)[0]));
                    Imgproc.line(histMatBitmap, p1, p2, colorsRgb[i], 2, 8, 0);
                }
            }

            for (int i = 0; i < histograms.length; i++) {
                calculationsOnHistogram(histograms[i]);
            }

            // Don't do that at home or work it's for visualization purpose.
            BitmapHelper.showBitmap(this, bitmap, imageView);

            Bitmap histBitmap = Bitmap.createBitmap(histMatBitmap.cols(), histMatBitmap.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(histMatBitmap, histBitmap);
            BitmapHelper.showBitmap(this, histBitmap, histogramView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void calculationsOnHistogram(Mat histogram) {
        SparseArray<ArrayList<Float>> compartments = HistogramHelper.createCompartments(histogram);
        float sumAll = HistogramHelper.sumCompartmentsValues(compartments);
        float averageAll = HistogramHelper.averageValueOfCompartments(compartments);
        Log.i(TAG, "Sum: " + Core.sumElems(histogram));
        Log.i(TAG, "Sum of all compartments " + String.valueOf(sumAll));
        Log.i(TAG, "Average value of all compartments " + String.valueOf(averageAll));
        Log.i(TAG, " ");

        for (int i = 0; i < compartments.size(); i++) {
            float sumLast = HistogramHelper.sumCompartmentValues(i, compartments);
            float averageLast = HistogramHelper.averageValueOfCompartment(i, compartments);
            float averagePercentageLastCompartment = HistogramHelper.averagePercentageOfCompartment(i, compartments);
            float percentageLastCompartment = HistogramHelper.percentageOfCompartment(i, compartments);
            Log.i(TAG, "Sum of " + (i + 1) + " compartment " + String.valueOf(sumLast));
            Log.i(TAG, "Average value of the " + (i + 1) + " compartment " + String.valueOf(averageLast));
            Log.i(TAG, "Average percentage of the " + (i + 1) + " compartment " + String.valueOf(averagePercentageLastCompartment));
            Log.i(TAG, "Percentage of the " + (i + 1) + " compartment " + String.valueOf(percentageLastCompartment));
            Log.i(TAG, " ");
        }
        Log.i(TAG, " ");
    }

}
