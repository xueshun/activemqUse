package bhz.mq.p2p;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

public class Producer {
	
	//����ģʽ
	
	//1.���ӹ���
	private ConnectionFactory connectionFactory;
	
	//2.���Ӷ���
	private Connection connection;
	
	//3.Session����
	private Session session;
	
	//4.������
	private MessageProducer messageProducer;
	
	public Producer(){
		try {
			this.connectionFactory = new ActiveMQConnectionFactory(
					"bhz",//ActiveMQConnectionFactory.DEFAULT_USER,
					"bhz",//ActiveMQConnectionFactory.DEFAULT_PASSWORD,
					"tcp://localhost:61616");
			this.connection = this.connectionFactory.createConnection();
			this.connection.start();
			this.session = this.connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
			this.messageProducer = this.session.createProducer(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Session getSession(){
		return this.session;
	}
	
	public void send1(/*String QueueName, Message message*/){
		try {
			Destination destination = this.session.createQueue("first");
			
			MapMessage mag1 = this.session.createMapMessage();
			mag1.setString("name", "����");
			mag1.setString("age", "23");
			mag1.setStringProperty("color", "blue");
			mag1.setIntProperty("sal", 2200);
			
			MapMessage mag2 = this.session.createMapMessage();
			mag2.setString("name", "����");
			mag2.setString("age", "26");
			mag2.setStringProperty("color", "red");
			mag2.setIntProperty("sal", 1300);
			
			
			MapMessage mag3 = this.session.createMapMessage();
			mag3.setString("name", "����");
			mag3.setString("age", "28");
			mag3.setStringProperty("color", "green");
			mag3.setIntProperty("sal", 1500);
			
			
			MapMessage mag4 = this.session.createMapMessage();
			mag4.setString("name", "����");
			mag4.setString("age", "28");
			mag4.setStringProperty("color", "green");
			mag4.setIntProperty("sal", 1500);			
			
			/*MapMessage mag1 = this.session.createMapMessage();
			mag1.setString("name", "����");
			mag1.setString("age", "23");
			mag1.setStringProperty("color", "blue");
			mag1.setIntProperty("sal", 2200);
			int id = 1;
			mag1.setInt("id", id);
			String receiver = id%2 == 0 ? "A" : "B" ;
			mag1.setStringProperty("receiver", receiver);
			
			MapMessage mag2 = this.session.createMapMessage();
			mag2.setString("name", "����");
			mag2.setString("age", "26");
			mag2.setStringProperty("color", "red");
			mag2.setIntProperty("sal", 1300);
			id = 2;
			mag2.setInt("id", id);
			receiver = id%2 ==0 ? "A" : "B";
			mag2.setStringProperty("receiver", receiver);
			
			MapMessage mag3 = this.session.createMapMessage();
			mag3.setString("name", "����");
			mag3.setString("age", "28");
			mag3.setStringProperty("color", "green");
			mag3.setIntProperty("sal", 1500);
			id = 3;
			mag3.setInt("id", id);
			receiver = id%2 ==0 ? "A" : "B";
			mag3.setStringProperty("receiver", receiver);
			
			MapMessage mag4 = this.session.createMapMessage();
			mag4.setString("name", "����");
			mag4.setString("age", "28");
			mag4.setStringProperty("color", "green");
			mag4.setIntProperty("sal", 1500);
			id = 4;
			mag4.setInt("id", id);
			receiver = id%2 ==0 ? "A" : "B";
			mag4.setStringProperty("receiver", receiver);*/
			
			this.messageProducer.send(destination,mag1,DeliveryMode.NON_PERSISTENT,2,1000*60*10L);
			this.messageProducer.send(destination,mag2,DeliveryMode.NON_PERSISTENT,3,1000*60*10L);
			this.messageProducer.send(destination,mag3,DeliveryMode.NON_PERSISTENT,6,1000*60*10L);
			this.messageProducer.send(destination,mag4,DeliveryMode.NON_PERSISTENT,9,1000*60*10L);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void send2(){
		try {
			Destination destination = this.session.createQueue("frist");
			TextMessage msg = this.session.createTextMessage("����һ���ַ�������");
			this.messageProducer.send(destination , msg, DeliveryMode.NON_PERSISTENT, 9, 1000*60*10L);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		Producer producer = new Producer();
		producer.send1();
	}
}
