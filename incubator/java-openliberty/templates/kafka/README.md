# Kafka template for Open Liberty

This is a template for the development of Liberty applications that can connect to Kafka using MicroProfile Reactive Messaging. There is a simple StarterApplication that enables basic production and consumption of events. The instructions for getting this up and running are below. As part of this, we have included a `docker-compose.yaml` file which will start a local Kafka and Zookeeper. 


## Getting Started with the StarterApplication.

### 1. Create a new folder and initialize it using appsody init:


```
mkdir test-appsody-reactive
cd test-appsody-reactive
appsody init java-openliberty reactive
```

### 2. Start Kafka and Zookeeper

In order to run the StarterApplication we will need to start a Kafka and Zookeeper. For this, you can use the provided `docker-compose.yaml`.


Start docker compose with 

```docker-compose -f docker-compose.yaml up```

If you run `docker network list`, you should see a new network, which will have the name of your directory with `_default` appended at the end. In this case it would be `test-appsody-reactive_default`.

If instead you want to connect to a Kafka broker elsewhere, edit `src/main/resources/META-INF/microprofile-config.properties` and set the value of `mp.messaging.connector.liberty-kafka.bootstrap.servers` property to the host and port of the your borker.

### 3. Run the appsody application in the new network

We need to run our appsody application in the same network as Kafka.

Run the application using the following command:

```appsody run --network test-appsody-reactive_default```

### 4. Produce a message to a topic

Run another container in the same network:

```docker run -it --network test-appsody-reactive_default strimzi/kafka:0.16.0-kafka-2.4.0 /bin/bash```

From within this container, we will produce a message. The following command will start a Kafka Producer using the console, writing out to incomingTopic1 :

```bin/kafka-console-producer.sh --broker-list kafka:9092 --topic incomingTopic1```

Enter text at the prompt to produce a message.

### 5. Consume a message from a topic

To view the messages, you can either see them in the appsody app terminal window or you can create a Kafka console consumer using the following command:

```bin/kafka-console-consumer.sh --bootstrap-server kafka:9092 --topic incomingTopic1 --from-beginning```

## Deploying to Kubernetes

When deploying to a Kubernetes environment, you will need to configure your application to connect the Kafka broker, for example deployed using the  [Strimzi Kafka operator](https://strimzi.io/docs/quickstart/latest/). To do this, first run

```appsody build```

This will generate an `app-deploy.yaml` file.
Then you can edit it and override the bootstrap server configuration by setting an enviornment variable as follows:

```
spec:
  env:
    - name: MP_MESSAGING_CONNECTOR_LIBERTY_KAFKA_BOOTSTRAP_SERVERS
      value: <your-kafka-host>:9092
```  

Then run the following command to deploy your application:

```
appsody deploy --no-build
```
