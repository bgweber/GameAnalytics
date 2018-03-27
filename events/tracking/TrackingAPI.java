package tracking;

import java.util.HashMap;
import java.util.Map.Entry;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
/**
 * An API for sending events to the game analytics pipeline.
 *
 * This class includes a main method that generates random events to send to
 * the pipeline to test connectivity.
 */
public class TrackingAPI implements ApiFutureCallback<String> {

  /** the project to send messages to */
  private static final String PROJECT_ID = "your_project_id";

  /** the topic to send messages to */
  private static final String TOPIC_ID = "raw-events";

  /** the publisher object used to send events */
  private Publisher publisher = null;

  /** the topic to publish to */
  private TopicName topicName = null;

  /**
   * Generates a series of events to send to the PubSub topic.
   */
  public static void main(String[] args) throws Exception {

    // set up the tracking API
    TrackingAPI tracking = new TrackingAPI(PROJECT_ID, TOPIC_ID);
    tracking.start();

    // send a batch of events
    for (int i=0; i<10000; i++) {

      // generate an event name
      String eventType = Math.random() < 0.5 ? "Session" : (Math.random() < 0.5 ? "Login" : "MatchStart");

      // create some attributes to send
      HashMap<String, String> attributes = new HashMap<String, String>();
      attributes.put("userID", "" + (int)(Math.random()*10000));
      attributes.put("deviceType", Math.random() < 0.5 ? "Android" : (Math.random() < 0.5 ? "iOS" : "Web"));

      // send the event
      tracking.sendEvent(eventType, "V1", attributes);

      // back off every once in awhile
      if (Math.random() < 0.01) {
        try {
          Thread.sleep(10000);
        }catch (Exception e) {}
      }
    }

    // stop publishing events
    tracking.stop();
  }

  /**
   * Constructs a TrackingAPI for the given project and topic IDs.
   */
  public TrackingAPI(String projectID, String topicID) {
    topicName = TopicName.of(projectID, topicID);
  }

  /**
   * Creates a publisher object for sending events.
   */
  public void start() throws Exception {
    publisher = Publisher.newBuilder(topicName).build();
  }

  /** Shuts down the publisher object */
  public void stop() throws Exception {
    publisher.shutdown();
  }

  /**
   * Publishes an event to the analytics endpoint. The input attributes are converted into a
   * JSON string.
   *
   * @param eventType - the type of event being sent (e.g. Session, MatchStart)
   * @param eventVersion - the version of the vent (e.g. V1.1)
   * @param attributes - optional attributes to append to the event. A mapping of the attribute
   *                     names to attribute values.
   */
  public void sendEvent(String eventType, String eventVersion, HashMap<String, String> attributes) {

    // build a json representation of the event
    StringBuilder event = new StringBuilder("{\"eventType\":\"" + eventType
            + "\",\"eventVersion\":\"" + eventVersion + "\"");
    for (Entry<String, String> entry : attributes.entrySet()) {
      event.append(",\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"");
    }
    event.append("}");

    // convert the event to bytes
    ByteString data = ByteString.copyFromUtf8(event.toString());

    //schedule a message to be published
    PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

    // publish the message, and add this class as a callback listener
    ApiFuture<String> future = publisher.publish(pubsubMessage);
    ApiFutures.addCallback(future, this);
  }

  /**
   * Callback for failures when publishing a message.
   */
  public void onFailure(Throwable throwable) {
    System.err.println(throwable.getMessage());
  }

  /** Callback for successfully delivered messages */
  public void onSuccess(String messageId) { }
}
