package zhouhongwei;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

public class DrawListener implements MessageListener {
    private DrawerData drawerData;

    public int length;
    public double[] data;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = (ObjectMessage) message;
        try {
            drawerData = (DrawerData) objectMessage.getObject();
            length = drawerData.getLength();
            data = drawerData.getRealValue();
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }

    public DrawerData getDrawerData() {
        return drawerData;
    }
}
