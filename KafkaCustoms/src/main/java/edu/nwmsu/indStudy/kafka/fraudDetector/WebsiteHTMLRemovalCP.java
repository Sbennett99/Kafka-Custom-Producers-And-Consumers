package edu.nwmsu.indStudy.kafka.fraudDetector;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.WakeupException;
import org.jsoup.Jsoup;

import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

public class WebsiteHTMLRemovalCP {
    private static Scanner in;
    public static void main(String[] argv)throws Exception{
        if (argv.length != 2) {
            System.err.printf("Usage: %s <inputTopicName> <groupId> <outputTopicName>\n",
                    BasicConsumer.class.getSimpleName());
            System.exit(-1);
        }
        in = new Scanner(System.in);
        String inputTopicName = argv[0];
        String groupId = argv[1];
        System.out.println("WARNING: CAN NOT RUN IN UNISON WITH PRIMARY PRODUCER ON STANDARD LOCAL KAFKA SERVER");
        //String outputTopicName = argv[3];

        WebsiteHTMLRemovalCP.ConsumerThread consumerRunnable = new WebsiteHTMLRemovalCP.ConsumerThread(inputTopicName,groupId);
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
        private String inputTopic;
        private String groupId;
        //private String outputTopic;
        private KafkaConsumer<String, String> kafkaConsumer;

        public ConsumerThread(String inputTopicName, String groupId) {
            this.inputTopic = inputTopicName;
            this.groupId = groupId;
            //this.outputTopic = outputTopicName;
        }

        public void run() {
            Properties producerConfigProperties = new Properties();
            producerConfigProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091");
            producerConfigProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
            producerConfigProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
            producerConfigProperties.put(ProducerConfig.CLIENT_ID_CONFIG, groupId);
            org.apache.kafka.clients.producer.Producer producer = new KafkaProducer(producerConfigProperties);

            Properties configProperties = new Properties();
            configProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            configProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArrayDeserializer");
            configProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            configProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
            configProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, "simple");


            //Figure out where to start processing messages from
            kafkaConsumer = new KafkaConsumer<String, String>(configProperties);
            kafkaConsumer.subscribe(Arrays.asList(inputTopic));
            //Start processing messages
            try {
                while (true) {
                    ConsumerRecords<String, String> records = kafkaConsumer.poll(100);
                    //Taking in Unprocessed Website HTML data from producer
                    for (ConsumerRecord<String, String> record : records) {
                        System.out.println(record.value());

                        //Processing the data using JSOUP
                        String processedString = htmlRemover(record.value());
                        ProducerRecord<String, String> rec = new ProducerRecord<String, String>(inputTopic, processedString);
                        producer.send(rec);
                    }





                }
            } catch (WakeupException ex) {
                System.out.println("Exception caught " + ex.getMessage());
            } finally {
                kafkaConsumer.close();
                System.out.println("After closing KafkaConsumer");
            }
        }

        public KafkaConsumer<String, String> getKafkaConsumer() {
            return this.kafkaConsumer;
        }


        public static String htmlRemover(String input) {
            return Jsoup.parse(input).text();
        }

    }
}
