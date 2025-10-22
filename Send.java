import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;

public class Send {
    private final static String EXCHANGE_NAME = "direct_logs"; // 1. New: Define Exchange Name

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            String queueName = "error_queue";
            channel.queueDeclare(queueName, false, false, false, null);
            String routingKey = "error";
            channel.queueBind(queueName, EXCHANGE_NAME, routingKey);
            String message = "A critical error occurred!";
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "' with routing key '" + routingKey + "'");
        }
    }
}