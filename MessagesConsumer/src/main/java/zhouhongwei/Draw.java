package zhouhongwei;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import javax.jms.*;

public class Draw implements Runnable{
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private MessageConsumer messageConsumer;
    private DrawListener drawListener;

    public Draw(String userName, String password, String URL) throws JMSException {
        connectionFactory = new ActiveMQConnectionFactory();
        connection = connectionFactory.createConnection();
        session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("queue_drawer");
        messageConsumer = session.createConsumer(destination);
        drawListener = new DrawListener();
        messageConsumer.setMessageListener(drawListener);
    }

    @Override
    public void run() {
        try {
            connection.start();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        int index =0;
        double[][] xyInitData = setData(drawListener.getDrawerData(),index,drawListener.getDrawerData().getLength());
        final XYChart chart = QuickChart.getChart("line chart","x","y"," ",
                xyInitData[0],xyInitData[1]);
        final SwingWrapper<XYChart> swingWrapper = new SwingWrapper<XYChart>(chart);
        swingWrapper.displayChart();
        while (true){
            index+=drawListener.getDrawerData().getLength();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            final double[][] data = setData(drawListener.getDrawerData(),index,drawListener.getDrawerData().getLength());
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    chart.updateXYSeries(" ",data[0],data[1],null);
                    swingWrapper.repaintChart();
                }
            });
        }

    }
    private static double[][] setData(DrawerData drawerData,int index,int length){
        double[] xData = new double[length];
        double[] yData = new double[length];
        for(int i=0;i<length;i++){
            xData[i]=(double) i+index;
        }
        yData = drawerData.getRealValue();
        return new double[][]{xData,yData};
    }
}
