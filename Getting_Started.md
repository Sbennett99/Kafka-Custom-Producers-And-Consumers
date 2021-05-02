# Getting Started

#### Prerequisites
-   Kafka Installation Completed
-   Local copy of Project files
-   Powershell *(prefered for the below steps, but a cmd prompt should work)*
-   Maven

### Step 1: Start Zookeeper and Kafka Servers
**1st - Zookeeper** 
Open a Powershell or Cmd prompt Window and traverse to your kafka folder then run the following command to start zookeeper.
```Powershell
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
```
**2nd - Kafka**
Open a Powershell or Cmd prompt Window and traverse to your kafka folder then run the following command to start Kafka.
```Powershell
 .\bin\windows\kafka-server-start.bat .\config\server.properties
```

-------------------
### Step 2: Create Input and Output topics
*below comands use default topics that are hardcoded into the classes for easy starting, other topics may be used if specified as arguments*

**Topic Creation Command**
Open a Powershell or Cmd prompt Window and traverse to your kafka folder then run the following command, inserting topic name where indicated.
```Powershell
 .\bin\windows\kafka-topics.bat --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --create --topic <INSERT_TOPIC_NAME_HERE>
```
**Default Input:** Stream1Input
**Default Output:** StreamOutput
**Default group:** group1

-------------------
### Step 3: Compile and Run the project
**Compiling the project using maven**

Open a Powershell or Cmd prompt Window and traverse to the **KafkaCustoms** folder within the project folder, then run the following command.
```Powershell
 mvn clean compile assembly:single
```

**Starting The Producer**
Open a Powershell or Cmd prompt Window and traverse to the **KafkaCustoms** folder within the project folder, then run the following command.
```Powershell
 java -cp target/KafkaFraudDetection-1.0-SNAPSHOT-jar-with-dependencies.jar edu.nwmsu.indStudy.kafka.fraudDetector.WebsiteProcesserProducer <Stream_Input_Topic_If_Not_Using_Default_Provided_Above>
```

**Starting the Consumer**
Open a Powershell or Cmd prompt Window and traverse to the **KafkaCustoms** folder within the project folder, then run the following command.
```Powershell
java -cp target/KafkaFraudDetection-1.0-SNAPSHOT-jar-with-dependencies.jar edu.nwmsu.indStudy.kafka.fraudDetector.WebsiteConsumer <topicName_If_Not_Using_Default> <groupId_If_Not_Using_Default>
```

**Starting the Stream**
Open a Powershell or Cmd prompt Window and traverse to the **KafkaCustoms** folder within the project folder, then run the following command.
```Powershell
 java -cp target/KafkaFraudDetection-1.0-SNAPSHOT-jar-with-dependencies.jar edu.nwmsu.indStudy.kafka.fraudDetector.WebsiteWordCountFilterStream <Input_Topic_If_Not_Using_Default_Provided_Above> <Output_Topic_If_Not_Using_Default_Provided_Above>
```

**Processing data**
Into the producer enter a website url that you would like to curl and perform a wordcount on and watch as Producer prints the whole website and the Consumer prints a count of all words on the page.

*Note: Each time the stream is ran it keeps a running count of all total words. To clear this, after each website is processed perform the following steps:
1. close just the Stream application using CTRL + C
2. go to the kafka-streams folder within your tmp folder(usually located at C:\\tmp\\ ) and delete the stream-HTML-wordcount folder to clear the data.
3. restart the stream application as started previously and process another website freely