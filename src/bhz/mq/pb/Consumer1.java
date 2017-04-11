package bhz.mq.pb;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;


public class Consumer1 {
	
	private ConnectionFactory factory;
	private Connection connection;
	private Session session;
	private MessageConsumer consumer;
	
	public Consumer1(){
		try {
			factory = new ActiveMQConnectionFactory(
					"bhz",
					"bhz",
					"tcp://localhost:61616");
			connection = factory.createConnection();
			connection.start();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void reviceMSG() throws Exception{
		Destination destination = session.createTopic("topic01");
		consumer = session.createConsumer(destination);
		
		TextMessage message = (TextMessage) consumer.receive();
	
		System.out.println("收到的内容：" + message.getText());
		
	}
	
	public static void main(String[] args) throws Exception {
		Consumer1 c = new Consumer1();
		c.reviceMSG();
	}
}
