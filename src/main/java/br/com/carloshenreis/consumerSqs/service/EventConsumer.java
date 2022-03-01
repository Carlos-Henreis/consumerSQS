package br.com.carloshenreis.consumerSqs.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;

@Service
public class EventConsumer {
    private static final Logger log = LoggerFactory.getLogger(
    		EventConsumer.class);

    @JmsListener(destination = "${aws.sqs.queue..name}")
    public void receiveEvent(TextMessage textMessage)
            throws JMSException, IOException {

        log.info("event received - Event: {}", textMessage.getText());
    }
}













