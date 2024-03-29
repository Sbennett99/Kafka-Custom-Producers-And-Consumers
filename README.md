# Kafka-Custom-Producers-And-Consumers

## Project Goal
- Gain a better understanding of Kafka and its capabilities as well as other services that may integrate with it

### Project Files
-------------
- [Custom Website Producer](KafkaCustoms/src/main/java/edu/nwmsu/indStudy/kafka/fraudDetector/WebsiteProcesserProducer.java)
  - custom producer that takes input of a Website Url
  - fetches the website HTML using curl and passes it to the given outputTopic
<img src="Kafka_Producer.PNG" alt="" width="400"/>



-------------
- [Custom HTML Removal and KeyValue Sorting Stream](KafkaCustoms/src/main/java/edu/nwmsu/indStudy/kafka/fraudDetector/WebsiteWordCountFilterStream.java)
  - Pulls from given Topic through a Kstream
  - Filters out HTML artifacts and condenses information into key value 2


-------------
- [Custom HTML Removal Consumer/Producer Hybrid ](KafkaCustoms/src/main/java/edu/nwmsu/indStudy/kafka/fraudDetector/WebsiteHTMLRemovalCP.java)
  - Pulls from given Topic through a Kstream
  - Filters out HTML artifacts and condenses information into key value 

<em>\*\*Not Fully Tested, Cannot Run locally due to Kafka Constraints</em>

-------------
- [Custom Website Consumer](KafkaCustoms/src/main/java/edu/nwmsu/indStudy/kafka/fraudDetector/WebsiteConsumer.java)
  - not much different than a standard consumer in that it pulls data and print it
  - it takes in key value pairs from a Ktable
  

<img src="Kafka_Output.PNG" alt="" width="400"/>

### [Running the Project for yourself](https://github.com/Sbennett99/Kafka-Custom-Producers-And-Consumers/blob/a826f8189c995916bd86db4d1d17318eaa0369fe/Getting_Started.md)

### [A Quick and Concise Summary of what I Learned](https://github.com/Sbennett99/Kafka-Custom-Producers-And-Consumers/blob/30ea929cc4274654427feb21ad5247679d92c696/Experience%20Overview.docx)

### References

[Appache Kafka Streams Documentation](https://kafka.apache.org/documentation/streams/)
[English Stop words](https://gist.github.com/sebleier/554280)
[HTML removal through JSOUP](https://stackoverflow.com/questions/240546/remove-html-tags-from-a-string/4095615)
[Basic Producer and Consumer Examples](https://github.com/denisecase/kafka-api/tree/master/src/main/java/com/spnotes/kafka/simple)
[Curling a website in java](https://www.baeldung.com/java-curl)
