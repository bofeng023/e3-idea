package cn.e3mall.service.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

public class ActiveMQSpring {
    @Test
    public void sendMessage() {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:spring/applicationContext-activemq.xml");
        JmsTemplate jmsTemplate = context.getBean(JmsTemplate.class);
        Destination queueDestination = (Destination) context.getBean("queueDestination");
        jmsTemplate.send(queueDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("send activemq message");
            }
        });
    }
}
