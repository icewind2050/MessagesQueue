package zhouhongwei;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.Random;

public class Producer {
    private static ConnectionFactory connectionFactory;
    private static Connection connection;
    private Session session;
    private Destination destination;
    private MessageProducer messageProducer;
    private Message message;

    public Producer(String userName, String password, String URL) throws JMSException {
        try {
            connectionFactory = new ActiveMQConnectionFactory(userName, password, URL);
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            destination = session.createQueue("queue_gauss");
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
            int count = 500;
            Random gauss = new Random();
            double average = 5.0;
            double sigma = 2.0;
            while (count-- != 0) {
                message = session.createTextMessage(String.valueOf(sigma*gauss.nextGaussian()+average));
                messageProducer.send(message);
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
