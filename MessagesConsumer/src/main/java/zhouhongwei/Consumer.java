package zhouhongwei;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Timer;
import java.util.TimerTask;

public class Consumer implements Runnable{
    private static Connection connection;
    private final MessageConsumer messageConsumer;
    GaussCalculator gaussCalculator;
    public Consumer(String userName, String password, String URL,int countOfNumber) {
        try {
            gaussCalculator = new GaussCalculator(countOfNumber);
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName, password, URL);
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("queue_gauss");
            messageConsumer = session.createConsumer(destination);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public void close()throws JMSException{
        if(connection!=null){
            connection.close();
        }
    }

    @Override
    public void run() {
        try {
            messageConsumer.setMessageListener(gaussCalculator);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println(gaussCalculator.mean());
                System.out.println(gaussCalculator.variance());
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(task,50,10*1000);
        try {
            close();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
