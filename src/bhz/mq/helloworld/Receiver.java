package bhz.mq.helloworld;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * ������
 * @author Administrator
 *
 */
public class Receiver {
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
		
		//ʹ��client��ǩ�շ�ʽ
		Session session = connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);

		//���Ĳ���ͨ��Session����Destination����ָ����һ���ͻ�������ָ��������ϢĿ���������Ϣ��Դ�Ķ�����PTPģʽ��Destination������Queue�����У���Pub/Subģʽ��Destination
		//         Destination������Topic�����壬�ڳ����п���ʹ�ö��Queue��Topic
		Destination destination = session.createQueue("queue1");

		//���岽��������Ҫͨ��Session���󴴽���Ϣ�ķ��ͺͽ��ն���(�����ߺ�������)MessageProducer/MessageConsumer
		//MessageProducer messageProducer = session.createProducer(destination);
		MessageConsumer messageConsumer = session.createConsumer(destination);

		while(true){
			TextMessage msg = (TextMessage) messageConsumer.receive();
			
			//ʹ��Clientǩ��ģʽ  ��Ҫ�ֶ�ȥǩ����Ϣ������һ���߳�(TCP)ȥ֪ͨMQ����  ȷ��ǩ��
			msg.acknowledge();
			if(msg == null) break;
			System.out.println("�յ������ݣ�" + msg.getText());
		}

		//�ر�����
		if(connection!=null){
			connection.close();
		}
	}
}
