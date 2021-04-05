package edu.nwmsu.indStudy.kafka.fraudDetector;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

public class WebsiteProcesserProducer {
    private static Scanner in;

    public static void main(String[] argv)throws Exception {

        if (argv.length != 1) {
            System.err.println("Please specify 1 parameter ");
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
        //configProperties.put(ProducerConfig.CLIENT_ID_CONFIG, "group1");

        org.apache.kafka.clients.producer.Producer producer = new KafkaProducer(configProperties);

        System.out.println("Welcome To The web page Processor");
        System.out.println("Enter 'exit' to quit or a website URL to begin\n");
        String line = in.nextLine();
        while (!line.equals("exit")) {
            try {
                String url = line;
                ProcessBuilder pb = new ProcessBuilder("curl","--silent","--location","--request","POST",url,"--header","Content-Type:application/x-www-form-urlencoded","--data-urlencode","inputParams=<Your Body>");

                pb.redirectErrorStream(true);

                Process proc = pb.start();


                InputStream ins = proc.getInputStream();

                BufferedReader read = new BufferedReader(new InputStreamReader(ins));
                StringBuilder sb = new StringBuilder();

                read
                        .lines()
                        .forEach(lines -> {
                            System.out.println(lines);
                            ProducerRecord<String, String> rec = new ProducerRecord<String, String>(topicName, lines);
                            producer.send(rec);

                            sb.append(lines);
                        });

                read.close();

                proc.waitFor();

                int exitCode = proc.exitValue();
                if (exitCode!=0){System.out.println("ExitCode> " + exitCode);}

                proc.destroy();
                System.out.println("Welcome To The web page Processor");
                System.out.println("Enter 'exit' to quit or a website URL to begin\n");
                line = in.nextLine();
            } catch (UnsupportedOperationException | IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
        in.close();
        producer.close();
    }
}
