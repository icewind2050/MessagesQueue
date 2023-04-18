package zhouhongwei;

import javax.jms.JMSException;

public class Main {
    public static void main(String[] args) throws JMSException, InterruptedException {
        Consumer consumer = new Consumer("admin","admin","tcp://127.0.0.1:61616",500);

        consumer.run();
    }

}