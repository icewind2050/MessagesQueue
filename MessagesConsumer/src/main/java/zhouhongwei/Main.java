package zhouhongwei;

import javax.jms.JMSException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws JMSException, IOException {
        Consumer consumer = new Consumer("admin","admin","tcp://127.0.0.1:61616");
        consumer.begin();
    }

}