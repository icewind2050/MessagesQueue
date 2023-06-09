package zhouhongwei;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.knowm.xchart.AnnotationTextPanel;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import javax.jms.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public class Draw implements Runnable {
    private final DrawListener drawListener;
    final MessageConsumer messageConsumer;

    public Draw(String userName, String password, String URL) throws JMSException {
        drawListener = new DrawListener();
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName, password, URL);
        connectionFactory.setTrustAllPackages(true);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("queue_drawer");
        messageConsumer = session.createConsumer(destination);
    }

    @Override
    public void run() {
        try {
            messageConsumer.setMessageListener(drawListener);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        int index = 0;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        double[][] xyInitData = setData(drawListener.getDrawerData(), index, drawListener.getDrawerData().getLength());
        final XYChart chart = QuickChart.getChart("line chart", "x", " ", " ",
                xyInitData[0], xyInitData[1]);
        final SwingWrapper<XYChart> swingWrapper = new SwingWrapper<>(chart);

        AtomicReference<String> analysisResult = new AtomicReference<>("mean is" +
                drawListener.getDrawerData().getMeanValue() +
                "\nvariance is " + drawListener.getDrawerData().getVarianceValue() +
                "\nMaximum is " + drawListener.getDrawerData().getMaxValue() +
                "\nMinimum is" + drawListener.getDrawerData().getMinValue());


        AnnotationTextPanel analysis = new AnnotationTextPanel(analysisResult.get(),0,0,true);

        chart.addAnnotation(analysis);
        swingWrapper.displayChart();
        while (index < Integer.MAX_VALUE) {
            index += drawListener.getDrawerData().getLength();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            final double[][] data = setData(drawListener.getDrawerData(), index, drawListener.getDrawerData().getLength());
            javax.swing.SwingUtilities.invokeLater(() -> {
                analysisResult.set("mean is" +
                        drawListener.getDrawerData().getMeanValue() +
                        "\nvariance is " + drawListener.getDrawerData().getVarianceValue() +
                        "\nMaximum is " + drawListener.getDrawerData().getMaxValue() +
                        "\nMinimum is" + drawListener.getDrawerData().getMinValue());
                chart.updateXYSeries(" ", data[0], data[1], null);
                analysis.setLines(Arrays.asList(analysisResult.get().split("\\n")));
                swingWrapper.repaintChart();
            });
        }

    }

    private static double[][] setData(DrawerData drawerData, int index, int length) {
        double[] xData = new double[length];
        double[] yData;
        for (int i = 0; i < length; i++) {
            xData[i] = (double) i + index;
        }
        yData = drawerData.getRealValue();
        return new double[][]{xData, yData};
    }


    public static void main(String[] args) throws JMSException {
        Draw draw = new Draw("admin", "admin", "tcp://127.0.0.1:61616");
        draw.run();
    }
}
