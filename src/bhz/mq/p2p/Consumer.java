package bhz.mq.p2p;

import java.awt.List;
import java.awt.font.TextMeasurer;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Consumer {
	public final String SELECTOR_1 = "color = 'blue'";
	//public final String SELECTOR_1 = "age > 25";
	
	public final String SELECTOR_2 = "color = 'blue' AND sal > 2000";
	
	public final String SELECTOR_3 = "receiver = 'A'";
	
	//public final String SELECTOR_4 = "receiver = 'B'";
	
	
	//1.���ӹ���
	private ConnectionFactory connectionFactory;
	
	//2.���Ӷ���
	private Connection connection;
	
	//3.Session����
	private Session session;
	
	//4.������
	private MessageConsumer messageConsumer;
	
	//5.Ŀ���ַ
	private Destination destination;
	  
	public Consumer(){
		try {
			this.connectionFactory = new ActiveMQConnectionFactory(
					"bhz",//ActiveMQConnectionFactory.DEFAULT_USER,
					"bhz",//ActiveMQConnectionFactory.DEFAULT_PASSWORD,
					"tcp://localhost:61616");
			this.connection = this.connectionFactory.createConnection();
			this.connection.start();
			this.session = this.connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			this.destination = this.session.createQueue("first");
			//���������ߵ�ʱ�����˸ı�
			this.messageConsumer = this.session.createConsumer(this.destination,SELECTOR_1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void receiver(){
		try {
			this.messageConsumer.setMessageListener(new Listener());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class Listener implements MessageListener{

		@Override
		public void onMessage(Message message) {
			try {
				if(message instanceof TextMessage){
					
				}
				if(message instanceof MapMessage){
					MapMessage ret = (MapMessage)message;
					System.out.println(ret.toString());
					System.out.println(ret.getString("name"));
					System.out.println(ret.getString("age"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}		
	}
	
	public static void main(String[] args) throws Exception {
		Consumer consumer = new Consumer();
		consumer.messageConsumer.receive();
	}
}