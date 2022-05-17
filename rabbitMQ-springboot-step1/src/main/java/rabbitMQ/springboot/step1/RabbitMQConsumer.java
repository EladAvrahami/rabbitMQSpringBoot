package rabbitMQ.springboot.step1;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

@Service
public class RabbitMQConsumer {

    // consume from url http://localhost:8080/api/v1/test/bor
   /*@RabbitListener(queues = "Mobile")
	public void getMessage(Person p) {
		System.out.println(p.getName());
	}*/


	//consumer for the Q which effected by header exchange:
	@RabbitListener(queues = "Mobile")
	public void getMessage(byte[] message) throws IOException, ClassNotFoundException {
		//byte into string obj function
		ByteArrayInputStream bis = new ByteArrayInputStream(message);
		ObjectInput in = new ObjectInputStream(bis);
		Person p = (Person) in.readObject();
		in.close();
		bis.close();
		System.out.println(p.getName());
	}
}
