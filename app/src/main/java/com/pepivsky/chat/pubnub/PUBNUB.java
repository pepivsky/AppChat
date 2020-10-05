package com.pepivsky.chat.pubnub;

import android.util.Log;

import com.google.gson.JsonObject;
import com.pepivsky.chat.model.Message;
import com.pepivsky.chat.model.User;
import com.pepivsky.chat.pubnub.utils.ConnectionCallback;
import com.pepivsky.chat.pubnub.utils.MessageCallback;
import com.pepivsky.chat.pubnub.utils.UserPresenceCallback;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.objects_api.channel.PNChannelMetadataResult;
import com.pubnub.api.models.consumer.objects_api.membership.PNMembershipResult;
import com.pubnub.api.models.consumer.objects_api.uuid.PNUUIDMetadataResult;
import com.pubnub.api.models.consumer.presence.PNSetStateResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.pubsub.PNSignalResult;
import com.pubnub.api.models.consumer.pubsub.files.PNFileEventResult;
import com.pubnub.api.models.consumer.pubsub.message_actions.PNMessageActionResult;

import java.util.Arrays;
import java.util.UUID;

public class PUBNUB {
    private static final PUBNUB instance = new PUBNUB();
    private PubNub pubNub;
    public static final String GLOBAL_CHANNEL = "GLOBAL_CHANNEL";
    public static final String MY_UUID = UUID.randomUUID().toString();
    //Callbacks
    private ConnectionCallback connectionCallback;
    private MessageCallback messagesCallback;
    private UserPresenceCallback userPresenceCallback;

    public static PUBNUB getInstance() {
        return instance;
    }
    public PubNub shared() {
        return pubNub;
    }

    public void connect(ConnectionCallback connectionCallback, UserPresenceCallback userPresenceCallback) {
        this.connectionCallback = connectionCallback;
        this.userPresenceCallback = userPresenceCallback;
        this.setupPubNub();
        this.subscribeTo(GLOBAL_CHANNEL);
    }
    public void setMessagesCallback(MessageCallback messagesCallback) {
        this.messagesCallback = messagesCallback;
    }
    public void publish(Message message) {
        this.pubNub.publish()
                .message(Arrays.asList(message.getContent()))
                .channel(GLOBAL_CHANNEL)
                .async(new PNCallback<PNPublishResult>() {
                    @Override
                    public void onResponse(PNPublishResult result, PNStatus status) {
                        log("Publish result", result.toString());
                        log("Publish status", status.toString());
                    }
                });
    }
    public void subscribeTo(String channel) {
        this.pubNub.subscribe().channels(Arrays.asList(channel)).withPresence().execute();
    }
    public void unsuscribe() {
        this.pubNub.unsubscribeAll();
    }
    private void setupPubNub() {
        PNConfiguration pnConfiguration = new PNConfiguration();

        //pnConfiguration.setPublishKey("pub-c-4b3cdc5a-8b9b-40cc-8b89-124f957dd16a");
        pnConfiguration.setPublishKey("pub-c-7aa3b342-2095-417f-892d-bb970675a5c4");
        //pnConfiguration.setSubscribeKey("sub-c-e07ff5d4-027c-11eb-88da-261b8c980873");
        pnConfiguration.setSubscribeKey("sub-c-3f01a39c-0439-11eb-ac24-4e38869d876d");

        pnConfiguration.setUuid(MY_UUID);
        pubNub = new PubNub(pnConfiguration);
        pubNub.addListener(makeListener());
    }
    private SubscribeCallback makeListener() {
        return new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus pnStatus) {
                handleStatus(pubnub, pnStatus);
            }
            @Override
            public void message(PubNub pubnub, PNMessageResult pnMessageResult) {
                handleMessage(pubnub, pnMessageResult);
            }
            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult pnPresenceEventResult) {
                handlePresence(pubnub, pnPresenceEventResult);
            }
            @Override
            public void signal(PubNub pubnub, PNSignalResult pnSignalResult) {
            }
            @Override
            public void uuid(PubNub pubnub, PNUUIDMetadataResult pnUUIDMetadataResult) {
            }
            @Override
            public void channel(PubNub pubnub, PNChannelMetadataResult pnChannelMetadataResult) {
            }
            @Override
            public void membership(PubNub pubnub, PNMembershipResult pnMembershipResult) {
            }
            @Override
            public void messageAction(PubNub pubnub, PNMessageActionResult pnMessageActionResult) {
            }
            @Override
            public void file(PubNub pubnub, PNFileEventResult pnFileEventResult) {
            }
        };
    }
    private void handleStatus(PubNub pubNub, PNStatus status) {
        if (status.getCategory() == PNStatusCategory.PNUnexpectedDisconnectCategory) {
            log("STATUS", "Conectivity lost");
            this.connectionCallback.Connected(false);
        } else if (status.getCategory() == PNStatusCategory.PNConnectedCategory) {
            log("STATUS", "Connected");
            this.connectionCallback.Connected(true);
            JsonObject state = new JsonObject();
            state.addProperty("name", "My Name");
            pubNub.setPresenceState()
                    .channels(Arrays.asList(GLOBAL_CHANNEL))
                    .state(state)
                    .async(new PNCallback<PNSetStateResult>() {
                        @Override
                        public void onResponse(final PNSetStateResult result, PNStatus status) {
                            log("Presence state", result.toString());
                            log("Presence status", status.toString());
                        }
                    });
        }
    }
    private void handlePresence(PubNub pubNub, PNPresenceEventResult presence) {
        switch (presence.getEvent()) {
            case "join":
                this.userPresenceCallback.userJoin(new User(presence.getUuid()));
                //log("PRESENCE", "Join State" + presence.getState().toString());
            case "leave":
                log("PRESENCE", "Leave");
                this.userPresenceCallback.userLeave(new User(presence.getUuid()));
            case "timeout":
                log("PRESENCE", "Timeout");
            case "state-change":
                log("PRESENCE", "State change");
            case "interval":
                log("PRESENCE", "Interval");
            default:
                break;
        }
    }
    private void handleMessage(PubNub pubNub, PNMessageResult message) {
        String messagePublisher = message.getPublisher();
        System.out.println("PUBNUB Message publisher: " + messagePublisher);
        System.out.println("PUBNUB Message Payload: " + message.getMessage());
        System.out.println("PUBNUB Message Subscription: " + message.getSubscription());
        System.out.println("PUBNUB Message Channel: " + message.getChannel());
        System.out.println("PUBNUB Message timetoken: " + message.getTimetoken());
        this.messagesCallback.received(new Message(message.getMessage().getAsString(), messagePublisher));
    }
    private void log(String handledMethod, String message) {
        Log.d("PUBNUB", "[" + handledMethod + "] -> " + message);
    }
}

