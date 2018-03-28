// Setup a PubSub connection 
TopicName topicName = TopicName.of(projectID, topicID);
Publisher publisher = Publisher.newBuilder(topicName).build();

// Specify an event to send
String event = {\"eventType\":\"session\",\"eventVersion\":\"1\"}";

// Convert the event to bytes    
ByteString data = ByteString.copyFromUtf8(event.toString());
//schedule a message to be published    
PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                                   .setData(data).build();     
// publish the message, and add this class as a callback listener    
ApiFuture<String> future = publisher.publish(pubsubMessage);    ApiFutures.addCallback(future, this);
