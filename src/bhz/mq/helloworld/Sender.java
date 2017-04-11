package bhz.mq.helloworld;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;


//��һ��������ConnectionFactory��������Ҫ��д�û��������룬�Լ�Ҫ���ӵĵ�ַ����ʹ��Ĭ�ϼ��ɣ�
//		 	Ĭ�϶˿�Ϊ��tcp://localhost:61616��
//�ڶ���: ͨ��ConnectionFactory�����������Ǵ���һ��Connection���ӣ����ҵ���Connection��start�����������ӣ�ConnectionĬ�������ǹرյ�
//��������ͨ��Connection���󴴽�Session�Ự(�����Ļ�������)�����ڽ�����Ϣ���������� 1 Ϊ�Ƿ��������񣬲�������  2 Ϊǩ��ģʽ��һ������Ϊ1
//���Ĳ���ͨ��Session����Destination����ָ����һ���ͻ�������ָ��������ϢĿ���������Ϣ��Դ�Ķ�����PTPģʽ��Destination������Q
//���岽��������Ҫͨ��Session���󴴽���Ϣ�ķ��ͺͽ��ն���(�����ߺ�������)MessageProducer/MessageConsumer
//�����������ǿ���ʹ��MessageProducer��setDeliveryMode����Ϊ�����ó־û�����(DeliveryMode)
//���߲����������ʹ��JMS�淶��TextMessage��ʽ��������(ͨ��Session����)������MessageProducer��send�����������ݣ�ͬ��ͻ���ʹ��
public class Sender {
	
	public static void main(String[] args) throws Exception {
		//��һ��������ConnectionFactory��������Ҫ��д�û��������룬�Լ�Ҫ���ӵĵ�ַ����ʹ��Ĭ�ϼ��ɣ�
		//		 	Ĭ�϶˿�Ϊ��tcp://localhost:61616��
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnectionFactory.DEFAULT_USER,
				ActiveMQConnectionFactory.DEFAULT_PASSWORD,
				"tcp://192.168.1.191:61616");
		
		//�ڶ���: ͨ��ConnectionFactory�����������Ǵ���һ��Connection���ӣ����ҵ���Connection��start�����������ӣ�ConnectionĬ�������ǹرյ�
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		//��������ͨ��Connection���󴴽�Session�Ự(�����Ļ�������)�����ڽ�����Ϣ���������� 1 Ϊ�Ƿ��������񣬲�������  2 Ϊǩ��ģʽ��һ������Ϊ�Զ�ǩ��ģʽ
		//Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
		
		//ʹ������ķ�ʽ������Ϣ�ķ���
		//Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
		
		//ʹ��client��ǩ�շ�ʽ
		Session session = connection.createSession(Boolean.TRUE, Session.CLIENT_ACKNOWLEDGE);

		
		//���Ĳ���ͨ��Session����Destination����ָ����һ���ͻ�������ָ��������ϢĿ���������Ϣ��Դ�Ķ�����PTPģʽ��Destination������Queue�����У���Pub/Subģʽ��Destination
		//         Destination������Topic�����壬�ڳ����п���ʹ�ö��Queue��Topic
		Destination destination = session.createQueue("queue1");
		
		//���岽��������Ҫͨ��Session���󴴽���Ϣ�ķ��ͺͽ��ն���(�����ߺ�������)MessageProducer/MessageConsumer
		MessageProducer messageProducer = session.createProducer(destination);
		
		//�����������ǿ���ʹ��MessageProducer��setDeliveryMode����Ϊ�����ó־û�����(DeliveryMode)
		//messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		//���߲����������ʹ��JMS�淶��TextMessage��ʽ��������(ͨ��Session����)������MessageProducer��send�����������ݣ�ͬ��ͻ���ʹ��receive�������н������ݣ����Ҫ���ǹر�
		
		
		
		//��������
		for (int i = 0; i < 5; i++) {
			TextMessage textMessage = session.createTextMessage();
			textMessage.setText("������Ϣ���ݣ�idΪ��"+i);
			/*
			 * ��һ������  ��  Ŀ�ĵ�
			 * �ڶ�������  �� ��Ϣ
			 * ����������   �� �Ƿ�־û�
			 * ���ĸ�����   �����ȼ�(0 - 9 0-4��ʾ��ͨ  5-9  �Ӽ�)
			 * ���������   �� ��Ϣ��mq�ϵ����ȼ�
			 * 
			 */
			messageProducer.send(destination, textMessage, DeliveryMode.NON_PERSISTENT, 1, 1000*60*2);
			System.err.println("�����ߣ�"+ textMessage.getText());
		}
		
		//ʹ�������ύ
		session.commit();
		
		//�ر�����
		if(connection!=null){
			connection.close();
		}
	}
}
