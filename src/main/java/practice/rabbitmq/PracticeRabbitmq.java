package practice.rabbitmq;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.MessageProperties;

/**
 * https://www.rabbitmq.com/tutorials/tutorial-one-java.html
 * 
 * Hello World
 * 
 * https://www.rabbitmq.com/tutorials/tutorial-two-java.html
 * 
 * Work Queues
 * 
 * 
 * https://www.rabbitmq.com/tutorials/tutorial-five-java.html
 * 
 * Topic exchange
 * 
 * Topic exchange is powerful and can behave like other exchanges.
 * 
 * When a queue is bound with "#" (hash) binding key - it will receive all the
 * messages, regardless of the routing key - like in fanout exchange.
 * 
 * When special characters "*" (star) and "#" (hash) aren't used in bindings,
 * the topic exchange will behave just like a direct one.
 *
 * 
 */
public class PracticeRabbitmq {

	private final static String QUEUE_NAME = "hello";
	private final static String HOST = "localhost";
	private final static boolean durable = true;

	public static void main(String[] argv)
			throws java.io.IOException, java.lang.InterruptedException, TimeoutException {

		consumer();
	}

	@Test
	public void producer() throws IOException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
		String message = "Hello World 3!";
		channel.basicPublish("", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
		System.out.println(" [x] Sent '" + message + "'");

		channel.close();
		connection.close();
	}

	public static void consumer() throws java.io.IOException, java.lang.InterruptedException, TimeoutException {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(HOST);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

		Consumer consumer = new DefaultConsumer(channel) {
			@Override
			public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
					byte[] body) throws IOException {
				String message = new String(body, "UTF-8");

				try {
					System.out.println(" [x] Received '" + message + "'");
				} finally {
					System.out.println(" [x] Done");
					channel.basicAck(envelope.getDeliveryTag(), false);
				}
			}
		};
		channel.basicConsume(QUEUE_NAME, true, consumer);

	}
}