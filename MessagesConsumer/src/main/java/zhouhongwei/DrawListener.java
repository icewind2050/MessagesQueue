package zhouhongwei;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class DrawListener implements MessageListener {
    private DrawerData drawerData;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            drawerData = (DrawerData) objectMessage.getObject();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public DrawerData getDrawerData() {
        return drawerData;
    }
}
