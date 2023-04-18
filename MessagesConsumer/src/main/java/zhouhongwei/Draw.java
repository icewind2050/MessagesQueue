package zhouhongwei;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import javax.jms.*;

public class Draw implements Runnable{
    private final Connection connection;
    private final DrawListener drawListener;

    public Draw(String userName, String password, String URL) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName,password,URL);
        connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("queue_drawer");
        MessageConsumer messageConsumer = session.createConsumer(destination);
        drawListener = new DrawListener();
        messageConsumer.setMessageListener(drawListener);
    }

    @Override
    public void run() {
        int index =0;
        double[][] xyInitData = setData(drawListener.getDrawerData(),index,drawListener.getDrawerData().getLength());
        final XYChart chart = QuickChart.getChart("line chart","x","y"," ",
                xyInitData[0],xyInitData[1]);
        final SwingWrapper<XYChart> swingWrapper = new SwingWrapper<>(chart);
        swingWrapper.displayChart();
        while (index<Integer.MAX_VALUE){
            index+=drawListener.getDrawerData().getLength();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            final double[][] data = setData(drawListener.getDrawerData(),index,drawListener.getDrawerData().getLength());
            javax.swing.SwingUtilities.invokeLater(() -> {
                chart.updateXYSeries(" ",data[0],data[1],null);
                swingWrapper.repaintChart();
            });
        }

    }
    private static double[][] setData(DrawerData drawerData,int index,int length){
        double[] xData = new double[length];
        double[] yData;
        for(int i=0;i<length;i++){
            xData[i]=(double) i+index;
        }
        yData = drawerData.getRealValue();
        return new double[][]{xData,yData};
    }
}
