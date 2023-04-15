package zhouhongwei;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class Consumer{
    private static Connection connection;
    private final MessageConsumer messageConsumer;
    private Message message;
    public Consumer(String userName, String password, String URL) throws JMSException {
        try {
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
    public void begin() throws JMSException, IOException {
        messageConsumer.setMessageListener(new Listener());
        System.in.read();
    }
    public void close()throws JMSException{
        if(connection!=null){
            connection.close();
        }
    }

}
