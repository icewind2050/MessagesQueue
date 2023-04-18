package zhouhongwei;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class Calculator implements Runnable {
    private static Connection connection;
    private final MessageProducer messageProducer;
    private final Session session;
    private final GaussCalculator gaussCalculator;

    public Calculator(String userName, String password, String URL, int countOfNumber) {
        try {
            gaussCalculator = new GaussCalculator(countOfNumber);
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName, password, URL);
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destinationReceive = session.createQueue("queue_gauss");
            Destination destinationSent = session.createQueue("queue_drawer");
            messageProducer = session.createProducer(destinationSent);
            MessageConsumer messageConsumer = session.createConsumer(destinationReceive);
            messageConsumer.setMessageListener(gaussCalculator);
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void run() {
        int count = 0;
        do {
            Message message;
            try {
                message = session.createObjectMessage(new DrawerData(gaussCalculator.mean(), gaussCalculator.variance(),
                        gaussCalculator.getMaximum(), gaussCalculator.getMinimum(), gaussCalculator.getArrayDouble(), gaussCalculator.getCountOfFloat()));
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
            try {
                messageProducer.send(message);
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }

        } while (count++ < Integer.MAX_VALUE);
        try {
            connection.close();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator("admin", "admin", "tcp://127.0.0.1:61616", 500);
        calculator.run();
    }
}
