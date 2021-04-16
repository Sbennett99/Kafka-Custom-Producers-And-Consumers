# Kafka-Custom-Producers-And-Consumers

## Project Goal
- Gain a better understanding of Kafka and its capabilities as well as other services that may integrate with it

### Project Files
-[CustomWebsiteProducer](KafkaCustoms/src/main/java/edu/nwmsu/indStudy/kafka/fraudDetector/WebsiteProcesserProducer.java)
  - custom producer that takes input of a Website Url, fetches the website HTML using curl and passes it to the given outputTopic

-[CustomWebsiteProducer](KafkaCustoms/src/main/java/edu/nwmsu/indStudy/kafka/fraudDetector/WebsiteProcesserProducer.java)
  - Pulls from given Topic through a Kstream
  - Filters out HTML artifacts and condenses information into key value 

-[CustomWebsiteConsumer](KafkaCustoms/src/main/java/edu/nwmsu/indStudy/kafka/fraudDetector/WebsiteConsumer.java)
  - not much different than a standard consumer in that it pulls data and print it
  - it takes in key value pairs from a Ktable
  

