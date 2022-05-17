package rabbitMQ.springboot.step1;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

@RestController
@RequestMapping("/api/v1")
public class RabbitMQProducer {

    @Autowired
    RabbitTemplate rabbitTemplate;
/*
    //http://localhost:8080/api/v1/test/entername
   @GetMapping("/test/{name}")
	public String testAPI(@PathVariable("name") String name) {
		Person p = new Person(1L, name);
		//convertAndSend- simple massage convertor-only support string bytArray and Serializable obj(impli Serializable inter)
		//                                   Q-name👇     obj
		rabbitTemplate.convertAndSend("Mobile",   p);
		//                         types of Exchange👇
		//rabbitTemplate.convertAndSend("Direct-Exchange", "mobile", p);
		//rabbitTemplate.convertAndSend("Fanout-Exchange", "", p);
		//rabbitTemplate.convertAndSend("Topic-Exchange", "tv.mobile.ac", p);
		return "Success";
	}
*/

	//produce using Headers-Exchange :
	@GetMapping("/test/{name}")
	public String testAPI(@PathVariable("name") String name) throws IOException {
		Person p = new Person(1L, name);

		//byte obj stream-> convert obj class into byte-array
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = new ObjectOutputStream(bos);
		out.writeObject(p);
		out.flush();
		out.close();
		byte[] byteMessage = bos.toByteArray();
		bos.close();

		Message message = MessageBuilder.withBody(byteMessage)
				.setHeader("item1", "mobile")
				.setHeader("item2", "television").build();

		rabbitTemplate.send("Headers-Exchange", "", message);

		return "Success";
	}

}
