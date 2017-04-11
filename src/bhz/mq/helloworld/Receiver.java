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
 * 消费者
 * @author Administrator
 *
 */
public class Receiver {
	public static void main(String[] args) throws Exception {
		
		//第一步：建立ConnectionFactory工厂，需要填写用户名，密码，以及要连接的地址，均使用默认即可，
		//		 	默认端口为“tcp://localhost:61616”
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
				ActiveMQConnectionFactory.DEFAULT_USER,
				ActiveMQConnectionFactory.DEFAULT_PASSWORD,
				"tcp://192.168.1.191:61616");

		//第二步: 通过ConnectionFactory工厂对象我们创建一个Connection连接，并且调用Connection的start方法开启连接，Connection默认连接是关闭的
		Connection connection = connectionFactory.createConnection();
		connection.start();

		
		//第三步：通过Connection对象创建Session会话(上下文环境对象)，用于接收消息，参数配置 1 为是否启用事务，参数配置  2 为签收模式。一般设置为自动签收模式
		//Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
		
		//使用client端签收方式
		Session session = connection.createSession(Boolean.FALSE, Session.CLIENT_ACKNOWLEDGE);

		//第四步：通过Session创建Destination对象，指的是一个客户端用来指定生产消息目标和消费消息来源的对象，在PTP模式中Destination被称作Queue即队列：在Pub/Sub模式，Destination
		//         Destination被称作Topic即主体，在程序中可以使用多个Queue和Topic
		Destination destination = session.createQueue("queue1");

		//第五步：我们需要通过Session对象创建消息的发送和接收对象(生产者和消费者)MessageProducer/MessageConsumer
		//MessageProducer messageProducer = session.createProducer(destination);
		MessageConsumer messageConsumer = session.createConsumer(destination);

		while(true){
			TextMessage msg = (TextMessage) messageConsumer.receive();
			
			//使用Client签收模式  需要手动去签收消息，另起一个线程(TCP)去通知MQ服务  确认签收
			msg.acknowledge();
			if(msg == null) break;
			System.out.println("收到的内容：" + msg.getText());
		}

		//关闭连接
		if(connection!=null){
			connection.close();
		}
	}
}
