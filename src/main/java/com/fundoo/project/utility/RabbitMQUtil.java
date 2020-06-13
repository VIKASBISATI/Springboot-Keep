package com.fundoo.project.utility;

import com.fundoo.project.model.MailModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Log
@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class RabbitMQUtil {
    @Autowired
    private JavaMailSender sender;

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value(value = "${spring.rabbitmq.template.routing-key}")
    private String myRoutingKey;

    @Value(value = "${spring.rabbitmq.template.exchange}")
    private String MyQueueExchange;

    public void sendMessageViaRabbitMq(MailModel mailModel){
        amqpTemplate.convertAndSend(MyQueueExchange, myRoutingKey,mailModel);
    }
    @Async
    @RabbitListener(queues = "MyQueue")
    public void sendMail(MailModel mailModel){
        SimpleMailMessage message=new SimpleMailMessage();
        try {
            message.setTo(mailModel.getTo());
            message.setText(mailModel.getText());
            message.setSubject(mailModel.getSubject());
            sender.send(message);
        }catch (Exception e){
            log.info("Errror in Sending the message");
            e.printStackTrace();
        }
    }
}
