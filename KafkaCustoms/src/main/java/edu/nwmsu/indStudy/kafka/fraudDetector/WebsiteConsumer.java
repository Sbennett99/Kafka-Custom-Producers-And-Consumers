package edu.nwmsu.indStudy.kafka.fraudDetector;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.jsoup.Jsoup;
import org.slf4j.*;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;


public class WebsiteConsumer {

    private static Scanner in;
    public static void main(String[] argv)throws Exception{
        String topicName = "";
        String groupId = "";
        if (argv.length != 2) {
            if (argv.length == 0) {
                topicName = "StreamOutput";
                groupId = "group1";
            }else {
                System.err.printf("Usage: %s <topicName> <groupId>\n",
                    Consumer.class.getSimpleName());
                System.exit(-1);
            }
        }else{
            topicName = argv[0];
            groupId = argv[1];
        }
        in = new Scanner(System.in);


        WebsiteConsumer.ConsumerThread consumerRunnable = new WebsiteConsumer.ConsumerThread(topicName,groupId);
        consumerRunnable.start();
        String line = "";
        while (!line.equals("exit")) {
            line = in.next();
        }
        consumerRunnable.getKafkaConsumer().wakeup();
        System.out.println("Stopping consumer .....");
        consumerRunnable.join();
    }
    private static class ConsumerThread extends Thread {
        private String topicName;
        private String groupId;
        private KafkaConsumer<String, Long> kafkaConsumer;

        public ConsumerThread(String topicName, String groupId) {
            this.topicName = topicName;
            this.groupId = groupId;
        }

        public void run() {
            Properties configProperties = new Properties();
            configProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            configProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.LongDeserializer");
            configProperties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
            configProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, "simple");

            //Figure out where to start processing messages from
            kafkaConsumer = new KafkaConsumer<String, Long>(configProperties);
            kafkaConsumer.subscribe(Arrays.asList(topicName));
            //Start processing messages
            try {

                while (true) {
                    ConsumerRecords<String, Long> records = kafkaConsumer.poll(100);
                    for (ConsumerRecord<String, Long> record : records) {

                        System.out.println(record.key() + " " + record.value());
                    }
                }
            } catch (WakeupException ex) {
                System.out.println("Exception caught " + ex.getMessage());
            } finally {
                kafkaConsumer.close();
                System.out.println("After closing KafkaConsumer");
            }
        }

        public KafkaConsumer<String, Long> getKafkaConsumer() {
            return this.kafkaConsumer;
        }

        public static String htmlRemover(String input) {
            return Jsoup.parse(input).text();
        }
    }
}
