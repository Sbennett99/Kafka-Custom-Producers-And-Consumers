package edu.nwmsu.indStudy.kafka.fraudDetector;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
import java.util.Scanner;

/*
    My reference/ example package
 */
public class SethBennettProducer {
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
        System.out.println("Welcome To Rubiks Cube Algorithm Look up");
        System.out.println("Enter 'exit' to quit or parity to enter parity look up\n");

        while (!line.equals("exit")) {
            if(line.equals("parity")) {
                String enterP ="Entering Parity lookup - enter cube in\nin form 3x3x3 to look up or\n" +
                        "enter exit quit parity look up\n";
                ProducerRecord<String, String> rec = new ProducerRecord<String, String>(topicName, "Entering Parity System\n");
                System.out.println(enterP);

                producer.send(rec);
                line = in.nextLine();
                while (!line.equals("exit")) {
                    String retrieving = "Now retrieving Parity Algorithms for " + line + "\n";
                    if (line.equals("4x4x4")) {
                        //4x4x4 paritys
                        String fourByfour = "OLL Algorithm\n" +
                                "**Piece flip**\n" +
                                "-r2 B2 U2 l U2 r' U2 r U2 F2 r F2 l' B2 r2-\n" +
                                "\n" +
                                "PLL Algorithm 1\n" +
                                "**Swap Fron and Back**\n" +
                                "-(Uu)2 (Ll)2 U2 l2 U2 (Ll)2 (Uu)2-\n" +
                                "\n" +
                                "PLL Algorithm 2\n" +
                                "**Swap Front and Left**\n" +
                                "-L2 D (Ff)2 (Ll)2 F2 l2 F2 (Ll)2 (Ff)2 D' L2 -\n" +
                                "\n" +
                                "PLL Algorithm 3\n" +
                                "**Swap front corners**\n" +
                                "-(Uu)2 (Ll)2 U2 l2 U2 (Ll)2 (Uu)2 F' U' F U F R' F2 U F U F' U' F R -\n" +
                                "\n" +
                                "PLL Algorithm 4\n" +
                                "**swap front right and back left corners**\n" +
                                "-(Uu)2 (Ll)2 U2 l2 U2 (Ll)2 (Uu)2 R U' L U2 R' U R L' U' L U2 R' U L' U -\n";

                        rec = new ProducerRecord<String, String>(topicName, retrieving + fourByfour);
                        producer.send(rec);
                        System.out.println("Output Successfully Sent, You may continue.\n");

                    } else if (line.equals("3x3x3") || line.equals("2x2x2")) {
                        String noParityMessage = "There are no paritys present in this cube\n";

                        rec = new ProducerRecord<String, String>(topicName,retrieving + noParityMessage);
                        producer.send(rec);


                    }
                    else{
                        String invalidMessage = "Invalid input or currently unrecognized puzzle\n";
                        rec = new ProducerRecord<String, String>(topicName, invalidMessage);
                        producer.send(rec);

                    }
                    line = in.nextLine();

                }

            }else if(line.equals("quit")){
                String leaving = "Thank you for using this producer";

                ProducerRecord<String, String> rec = new ProducerRecord<String, String>(topicName, leaving);
                producer.send(rec);
                line = in.nextLine();
            }

            line = in.nextLine();

        }
        in.close();
        producer.close();
    }

}
