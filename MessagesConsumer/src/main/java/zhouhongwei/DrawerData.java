package zhouhongwei;

import java.io.Serializable;

public class DrawerData implements Serializable {
    private double meanValue;
    private double varianceValue;
    private double maxValue;
    private double minValue;
    private double[] realValue;
    private int length;
    public DrawerData(double mean, double variance, double max, double min, double[] value,int Length) {
        meanValue = mean;
        varianceValue = variance;
        maxValue = max;
        minValue = min;
        realValue = value;
        length =Length;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
    }

    public void setMinValue(double minValue) {
        this.minValue = minValue;
    }

    public void setMeanValue(double meanValue) {
        this.meanValue = meanValue;
    }

    public void setVarianceValue(double varianceValue) {
        this.varianceValue = varianceValue;
    }

    public void setRealValue(double[] realValue) {
        this.realValue = realValue;
    }

    public double getMeanValue() {
        return meanValue;
    }

    public double getVarianceValue() {
        return varianceValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getMinValue() {
        return minValue;
    }

    public double[] getRealValue() {
        return realValue;
    }
}
