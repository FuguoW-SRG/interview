package topic;

import com.solacesystems.jms.message.SolBytesMessage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.annotation.JmsListener;

import javax.jms.Message;

/**
 * @ClassName HuaWeiTopicApplication
 * @Author yyl
 * @Date 2024-02-05 10:26:04
 * @Description HuaWeiTopicApplication
 * @Version 1.0
 */
@SpringBootApplication
public class HuaWeiTopicApplication {

    public static void main(String[] args) {
        SpringApplication.run(HuaWeiTopicApplication.class, args);
    }

    @JmsListener(destination = "orders")
    public void handle(Message message) {

        SolBytesMessage bytesMessage = (SolBytesMessage) message;
        byte[] backingArray = bytesMessage.getBackingArray();
        System.out.println(new String(backingArray));
    }

}
