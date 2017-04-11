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


//第一步：建立ConnectionFactory工厂，需要填写用户名，密码，以及要连接的地址，均使用默认即可，
//		 	默认端口为“tcp://localhost:61616”
//第二步: 通过ConnectionFactory工厂对象我们创建一个Connection连接，并且调用Connection的start方法开启连接，Connection默认连接是关闭的
//第三步：通过Connection对象创建Session会话(上下文环境对象)，用于接收消息，参数配置 1 为是否启用事务，参数配置  2 为签收模式。一般设置为1
//第四步：通过Session创建Destination对象，指的是一个客户端用来指定生产消息目标和消费消息来源的对象，在PTP模式中Destination被称作Q
//第五步：我们需要通过Session对象创建消息的发送和接收对象(生产者和消费者)MessageProducer/MessageConsumer
//第六步：我们可以使用MessageProducer的setDeliveryMode方法为其设置持久化特性(DeliveryMode)
//第七步：最后我们使用JMS规范的TextMessage形式创建数据(通过Session对象)，并用MessageProducer的send方法发送数据，同理客户端使用
public class Sender {
	
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
		
		//使用事务的方式进行消息的发送
		//Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
		
		//使用client端签收方式
		Session session = connection.createSession(Boolean.TRUE, Session.CLIENT_ACKNOWLEDGE);

		
		//第四步：通过Session创建Destination对象，指的是一个客户端用来指定生产消息目标和消费消息来源的对象，在PTP模式中Destination被称作Queue即队列：在Pub/Sub模式，Destination
		//         Destination被称作Topic即主体，在程序中可以使用多个Queue和Topic
		Destination destination = session.createQueue("queue1");
		
		//第五步：我们需要通过Session对象创建消息的发送和接收对象(生产者和消费者)MessageProducer/MessageConsumer
		MessageProducer messageProducer = session.createProducer(destination);
		
		//第六步：我们可以使用MessageProducer的setDeliveryMode方法为其设置持久化特性(DeliveryMode)
		//messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
		
		//第七步：最后我们使用JMS规范的TextMessage形式创建数据(通过Session对象)，并用MessageProducer的send方法发送数据，同理客户端使用receive方法进行接收数据，最后不要忘记关闭
		
		
		
		//发送数据
		for (int i = 0; i < 5; i++) {
			TextMessage textMessage = session.createTextMessage();
			textMessage.setText("我是消息内容，id为："+i);
			/*
			 * 第一个参数  ：  目的地
			 * 第二个参数  ： 消息
			 * 第三个参数   ： 是否持久化
			 * 第四个参数   ：优先级(0 - 9 0-4表示普通  5-9  加急)
			 * 第五个参数   ： 消息在mq上的优先级
			 * 
			 */
			messageProducer.send(destination, textMessage, DeliveryMode.NON_PERSISTENT, 1, 1000*60*2);
			System.err.println("生产者："+ textMessage.getText());
		}
		
		//使用事务提交
		session.commit();
		
		//关闭连接
		if(connection!=null){
			connection.close();
		}
	}
}
