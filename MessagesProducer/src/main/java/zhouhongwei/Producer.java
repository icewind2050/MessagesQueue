package zhouhongwei;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Random;

public class Producer implements Runnable{
    private static Connection connection;
    private final Session session;
    private final MessageProducer messageProducer;

    public Producer(String userName, String password, String URL) throws JMSException {
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName, password, URL);
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("queue_gauss");
            messageProducer = session.createProducer(destination);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }

    public void sentMessageToActiveMQ() throws JMSException {
        try {
            int count = 50000;
            Random gauss = new Random();
            double average = 5.0;
            double sigma = 2.0;
            while (count-- != 0) {
                Thread.sleep(1);
                Message message = session.createTextMessage(String.valueOf(sigma * gauss.nextGaussian() + average));
                messageProducer.send(message);
            }
        } catch (JMSException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        try {
            sentMessageToActiveMQ();
            close();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
