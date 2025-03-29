package org.example;

import org.apache.kafka.clients.KafkaClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.internals.events.ConsumerRebalanceListenerCallbackCompletedEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;

public class FraudDetectorService {
    public static void main(String[] args) throws InterruptedException {
        var consumer = new KafkaConsumer<String, String>(properties());
        consumer.subscribe(Collections.singletonList("ECOMMERCE_NEW_ORDER"));

        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));

            if (!records.isEmpty()) {
                System.out.println("Encontrei " + records.count() + " registros");
            }

            for (var record : records) {
                System.out.println("--------");
                System.out.println("Processing new order, checking for fraud");
                System.out.println("Key: " + record.key());
                System.out.println("Value: " + record.value());
                System.out.println("Partition: " + record.partition());
                System.out.println("Offset: " + record.offset());

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Order processed");

            }
        }
    }

    public static Properties properties(){
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudDetectorService.class.getSimpleName());
        return properties;
    }
}
