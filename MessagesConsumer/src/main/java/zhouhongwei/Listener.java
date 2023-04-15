package zhouhongwei;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class Listener implements MessageListener {
    GaussCalculator gaussCalculator;
    public Listener(){
        gaussCalculator = new GaussCalculator(50);
    }
    @Override
    public void onMessage(Message message) {
        try{
            TextMessage textMessage=(TextMessage)message;
            gaussCalculator.insert(Double.valueOf(textMessage.getText()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
