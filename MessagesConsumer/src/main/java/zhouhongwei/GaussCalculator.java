package zhouhongwei;

import org.apache.commons.math3.stat.StatUtils;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class GaussCalculator implements MessageListener {
    double[] arrayDouble;
    int countOfFloat;
    int pointer;
    double maximum;
    double minimum;

    public GaussCalculator(int CountOfFloat) {
        countOfFloat = CountOfFloat;
        arrayDouble = new double[countOfFloat];
        pointer = 0;
        maximum = Double.MIN_VALUE;
        minimum = Double.MAX_VALUE;
    }

    public void insert(double a) {
        arrayDouble[pointer] = a;
        pointer = (pointer + 1) % countOfFloat;
        if (a > maximum) {
            maximum = a;
        }
        if (a < minimum) {
            minimum = a;
        }
    }

    public double mean() {
        return StatUtils.mean(arrayDouble);
    }

    public double variance() {
        return StatUtils.variance(arrayDouble);
    }

    public double getMaximum() {
        return maximum;
    }

    public double getMinimum() {
        return minimum;
    }


    @Override
    public void onMessage(Message message) {
        try {
            TextMessage textMessage = (TextMessage) message;
            insert(Double.parseDouble(textMessage.getText()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
