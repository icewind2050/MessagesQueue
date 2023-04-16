package zhouhongwei;

import java.io.Serializable;

public class drawerData implements Serializable {
    private final double meanValue;
    private final double varianceValue;
    private final double maxValue;
    private final double minValue;
    public drawerData(double mean,double variance,double max,double min){
        meanValue=mean;
        varianceValue=variance;
        maxValue = max;
        minValue =min;
    }
    public double getMeanValue(){
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
}
