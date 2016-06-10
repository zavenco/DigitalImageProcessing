package co.zaven.digitalimageprocessing.helpers;

import android.util.Log;
import android.util.SparseArray;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created on 15.03.2016.
 *
 * @author Sławomir Onyszko
 */
public class HistogramHelper {

    private static final String TAG = HistogramHelper.class.getSimpleName();

    private HistogramHelper() {

    }

    public static SparseArray<ArrayList<Float>> createCompartments(Mat histogram) {
        int binsCount = 256;
        float[] histData = new float[binsCount];
        histogram.get(0, 0, histData);

        int compartmentsCount = 5;
        int interval = binsCount / compartmentsCount;

        SparseArray<ArrayList<Float>> compartments = new SparseArray<>();
        for (int i = 0; i < compartmentsCount; i++) {
            int start = interval * i;
            int end = start + interval;
            ArrayList<Float> tmp = new ArrayList<>();
            for (int j = start; j < end; j++) {
                tmp.add(histData[j]);
            }
            compartments.put(i, tmp);
        }
        return compartments;
    }

    public static SparseArray<Float> compartmentsMaxValues(SparseArray<ArrayList<Float>> compartments) {
        SparseArray<Float> maxValues = new SparseArray<>();
        for (int i = 0; i < compartments.size(); i++) {
            maxValues.put(i, Collections.max(compartments.get(i)));
        }
        return maxValues;
    }

    public static float compartmentMaxValue(int index, SparseArray<ArrayList<Float>> compartments) {
        if (index < 0 || index > compartments.size()) {
            throw new ArrayIndexOutOfBoundsException("index ∈ <0;" + (compartments.size() - 1) + ">");
        }
        return Collections.max(compartments.get(index));
    }

    public static float sumCompartmentValues(int index, SparseArray<ArrayList<Float>> compartments) {
        float sum = 0L;
        if (index < 0 || index > compartments.size()) {
            throw new ArrayIndexOutOfBoundsException("index ∈ <0;" + (compartments.size() - 1) + ">");
        } else {
            for (int i = 0; i < compartments.get(index).size(); i++) {
                sum += compartments.get(index).get(i);
            }
        }
        return sum;
    }

    public static float sumCompartmentsValues(SparseArray<ArrayList<Float>> compartments) {
        float sum = 0L;
        for (int i = 0; i < compartments.size(); i++) {
            for (int j = 0; j < compartments.get(i).size(); j++) {
                sum += compartments.get(i).get(j);
            }
        }
        return sum;
    }

    public static float averageValueOfCompartment(int index, SparseArray<ArrayList<Float>> compartments) {
        if (index < 0 || index > compartments.size()) {
            throw new ArrayIndexOutOfBoundsException("index ∈ <0;" + (compartments.size() - 1) + ">");
        } else {
            return sumCompartmentValues(index, compartments) / compartments.get(index).size();
        }
    }

    public static float averageValueOfCompartments(SparseArray<ArrayList<Float>> compartments) {
        return sumCompartmentsValues(compartments) / compartments.size();
    }

    public static float percentageOfCompartment(int index, SparseArray<ArrayList<Float>> compartments) {
        if (index < 0 || index > compartments.size()) {
            throw new ArrayIndexOutOfBoundsException("index ∈ <0;" + (compartments.size() - 1) + ">");
        } else {
            return (sumCompartmentValues(index, compartments) * 100) / sumCompartmentsValues(compartments);
        }
    }

    public static float percentageRangeOfCompartments(int start, int end, SparseArray<ArrayList<Float>> compartments) {
        if ((start < 0 || start > compartments.size()) && (end < 0 || end > compartments.size())) {
            throw new ArrayIndexOutOfBoundsException("start ∈ <0;" + (compartments.size() - 1) + "> and end ∈ <0;\" + (compartments.size() - 1) + \">");
        } else {
            float sum = 0L;
            for (int i = start; i <= end; i++) {
                sum += sumCompartmentValues(i, compartments);
            }
            return (sum * 100) / sumCompartmentsValues(compartments);
        }
    }

    public static float averagePercentageOfCompartment(int index, SparseArray<ArrayList<Float>> compartments) {
        if (index < 0 || index > compartments.size()) {
            throw new ArrayIndexOutOfBoundsException("index ∈ <0;" + (compartments.size() - 1) + ">");
        } else {
            return (averageValueOfCompartment(index, compartments) * 100) / averageValueOfCompartments(compartments);
        }
    }

    public static float varianceCompartments(SparseArray<ArrayList<Float>> compartments) {
        float variance = 0f;
        float average = averageValueOfCompartments(compartments);
        int size = 0;
        for (int i = 0; i < compartments.size(); i++) {
            for (int j = 0; j < compartments.get(i).size(); j++) {
                variance += (compartments.get(i).get(j) - average) * (compartments.get(i).get(j) - average);
            }
            size += compartments.get(i).size();
        }
        Log.i(TAG, "");
        return variance / size;
    }

    public static float varianceCompartment(int index, SparseArray<ArrayList<Float>> compartments) {
        float variance = 0f;
        float average = averageValueOfCompartment(index, compartments);
        if (index < 0 || index > compartments.size()) {
            throw new ArrayIndexOutOfBoundsException("index ∈ <0;" + (compartments.size() - 1) + ">");
        } else {
            for (int j = 0; j < compartments.get(index).size(); j++) {
                variance += (compartments.get(index).get(j) - average) * (compartments.get(index).get(j) - average);
            }
            Log.i(TAG, "");
            return variance / compartments.get(index).size();
        }
    }

}
