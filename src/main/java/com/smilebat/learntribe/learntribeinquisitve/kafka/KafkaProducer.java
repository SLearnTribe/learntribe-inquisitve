package com.smilebat.learntribe.learntribeinquisitve.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka Producer for the sending notifications.
 *
 * <p>Copyright &copy; 2022 Smile .Bat
 *
 * @author Pai,Sai Nandan
 */
@Component
@Slf4j
public class KafkaProducer {
  @Autowired private KafkaTemplate<String, Object> kafkaTemplate;

  /**
   * Sends a notifcation to assessment service.
   *
   * @param message the message to be sent
   */
  public void sendMessage(String message) {
    try {
      kafkaTemplate.send(ApplicationConstant.TOPIC_NAME, message);
    } catch (Exception e) {
      log.info("Unable to send message {} to {}", message, ApplicationConstant.TOPIC_NAME);
      throw new RuntimeException(e);
    }
  }
}
