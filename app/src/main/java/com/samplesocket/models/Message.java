package com.samplesocket.models;

/**
 * Created by nedu on 3/31/14.
 */
public class Message {
    private static int count = 0;
    private String id;
    private String from;
    private String to;
    private String message;
    private String timestamp;
    private boolean delivered;

    public Message(String _from, String _to, String _message, String _timestamp){
        id = String.valueOf(count++);
        from = _from;
        to = _to;
        message = _message;
        timestamp = _timestamp;
    }

    public String getId(){
        return id;
    }

    public String getFrom(){
        return from;
    }

    public String getTo(){
        return to;
    }

    public String getMessage(){
        return message;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public void setDelivered(){
        delivered = true;
    }

    public boolean isDelivered(){
        return delivered;
    }
}
