package zhouhongwei;

import org.apache.activemq.ActiveMQConnectionFactory;

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

    }
    private static double[][] setData(DrawerData drawerData,double index,int length){
        double[] xData = new double[length];
        double[] yData = new double[length];

        return new double[][]{xData,yData};
    }
}
