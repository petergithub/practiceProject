package practice.rabbitmq.four.routing;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * https://www.rabbitmq.com/tutorials/tutorial-four-java.html
 */
public class EmitLogDirect {

	private static final String EXCHANGE_NAME = "direct_logs";

	@Test
	public void send() throws IOException, TimeoutException {
		String[] argv = { "message.info", "info" };
		emit(argv);
		argv = new String[] { "message.info2", "info" };
		emit(argv);

		argv = new String[] { "message.warning", "warning" };
		emit(argv);

		argv = new String[] { "message.error", "error" };
		emit(argv);
	}

	public void emit(String[] argv) throws java.io.IOException, TimeoutException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();

		channel.exchangeDeclare(EXCHANGE_NAME, "direct");

		String severity = getSeverity(argv);
		String message = getMessage(argv);

		channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
		System.out.println(" [x] Sent '" + severity + "':'" + message + "'");

		channel.close();
		connection.close();
	}

	private static String getMessage(String[] argv) {
		return argv[0];
	}

	private static String getSeverity(String[] argv) {
		return argv[1];
	}
}