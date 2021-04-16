package edu.nwmsu.indStudy.kafka.fraudDetector;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;

import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Produced;

import org.jsoup.Jsoup;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;


public class WebsiteWordCountFilterStream {

    public static final String StopWordsFilePath = "stopWords.txt";


    public static List<String> englishStopWords = new ArrayList<>();

    public static String INPUT_TOPIC = "Stream1Input";
    public static String OUTPUT_TOPIC = "StreamOutput";

    public WebsiteWordCountFilterStream() throws IOException {
    }

    static Properties getStreamsConfig(final String[] args) throws IOException {
        final Properties props = new Properties();
        if(args.length == 0){
            System.out.println("Using Default Topics Stream1Input, StreamOutput");
        }
        if (args.length == 1) {
            INPUT_TOPIC = args[0];

        }
        if (args.length == 2) {
            INPUT_TOPIC = args[0];
            OUTPUT_TOPIC = args[1];
        }
        if (args.length > 2) {
            System.err.println("Error: To many arguments given: 0-2 arguments required\n" +
                    "0 -> default Topics: Stream1Input, StreamOutput\n" +
                    "1 -> <inputTopic> , uses default StreamOutput for outputTopic\n" +
                    "2 -> <inputTopic> <outputTopic>\n");
            System.exit(-1);
        }

        props.putIfAbsent(StreamsConfig.APPLICATION_ID_CONFIG, "stream-HTML-wordcount");
        props.putIfAbsent(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.putIfAbsent(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 0);
        props.putIfAbsent(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        props.putIfAbsent(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());


        props.putIfAbsent(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }

    static void createWordCountStream(final StreamsBuilder builder) {
        final KStream<String, String> source = builder.stream(INPUT_TOPIC);

        final KTable<String, Long> counts = source
                .flatMapValues(value -> Arrays.asList(value.replaceAll("<.*?>", "").replaceAll("\\p{Punct}", "").toLowerCase(Locale.getDefault()).split("\\s+")))
                .filter((key, value) -> !englishStopWords.contains(value) || value.length() > 1)
                .groupBy((key, value) -> value)
                .count();



        // need to override value serde to Long type


        counts.toStream().to(OUTPUT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));
    }

    public static void main(final String[] args) throws IOException {
        // retrieving stop words to a list
        englishStopWords = getFileContentAsList(StopWordsFilePath);

        final Properties props = getStreamsConfig(args);

        final StreamsBuilder builder = new StreamsBuilder();

        createWordCountStream(builder);
        final KafkaStreams streams = new KafkaStreams(builder.build(), props);
        final CountDownLatch latch = new CountDownLatch(1);

        // attach shutdown handler to catch control-c
        Runtime.getRuntime().addShutdownHook(new Thread("streams-WebsiteCleanerCounter") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (final Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }

    public static String htmlRemover(String input) {
        return Jsoup.parse(input).text();
    }

    private static ArrayList<String> getFileContentAsList(String resourceFilePath) throws IOException {
        Path pathToFile = Paths.get(StopWordsFilePath);

        File file = new File(pathToFile.toAbsolutePath().toString());
        Scanner s = new Scanner(file);
        ArrayList<String> result = new ArrayList<String>();

        while (s.hasNext() ){
            result.add(s.next());

        }
        s.close();
        result = (ArrayList<String>) result.stream().map(line -> line.toLowerCase()).collect(Collectors.toList());

        return result;

    }

}

