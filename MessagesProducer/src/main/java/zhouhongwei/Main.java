package zhouhongwei;

import javax.jms.JMSException;

public class Main {
    public static void main(String[] args) {
        Producer messProducer;
        try {
            messProducer = new Producer("admin", "admin", "tcp://127.0.0.1:61616");
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
        messProducer.run();
    }
}