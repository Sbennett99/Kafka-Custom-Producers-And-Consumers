package edu.nwmsu.indStudy.kafka.fraudDetector;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class FraudProducer {
    private static Scanner in;

    public static void main(String[] argv)throws Exception {

        if (argv.length != 1) {
            System.err.println("Please specify 1 parameters ");
            System.exit(-1);
        }

        String topicName = argv[0];
        in = new Scanner(System.in);
        System.out.println("Enter message(type exit to quit)");

        //Configure the Producer
        Properties configProperties = new Properties();
        configProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
        configProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        org.apache.kafka.clients.producer.Producer producer = new KafkaProducer(configProperties);
        String line = in.nextLine();

        while (!line.equals("exit")) {
            System.out.println("Enter 'test' to Start the test data generator");
            List<List<String>> records = new ArrayList<>();

            if(line.equals("test")) {
                System.out.println("Beginning Test data generation");

                while (!line.equals("exit")){


                    line = in.nextLine();
                }

            }

            /*
            try (Scanner scanner = new Scanner(new File("book.csv"));) {
                while (scanner.hasNextLine()) {
                    ProducerRecord<String,String> rec = new ProducerRecord<String,
                            String>(topicName, scanner.nextLine());
                    producer.send(rec);
                    line = in.nextLine();
                }




            ProducerRecord<String,String> rec = new ProducerRecord<String, List<String>>(topicName, ListNameHere );
            producer.send(rec);
            line = in.nextLine();
               */

        }
        in.close();
        producer.close();
    }



    }