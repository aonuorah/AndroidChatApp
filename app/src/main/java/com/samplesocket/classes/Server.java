package com.samplesocket.classes;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nedu on 4/5/14.
 */
public class Server {
    public class RequestCodes{

        public static final String SEND_MESSAGE = "12";
        public static final String CONTROL = "10";
        public static final String CONNECT = "11";
    }

    public class ResponseCodes{
        public static final String NEW_MESSAGE = "902";
        public static final String SENT_MESSAGE_UPDATE = "903";
        public static final String USER_UPDATE_ONLINE = "904";
        public static final String USER_UPDATE_OFFLINE = "905";
        public static final String IS_ALIVE = "909";
    }

    public class StatusCodes{
        public static final String SUCCESS = "200";
        public static final String ACCEPTED = "202";
        public static final String NOT_IMPLEMENTED = "501";
        public static final String NOT_FOUND = "404";
        public static final String UNAUTHORIZED = "401";
        public static final String BAD_REQUEST = "400";
        public static final String TIMEOUT = "408";
        public static final String GONE = "410";
    }

    public class Keys{
        public static final String CODE = "code";
        public static final String STATUS = "status";
        public static final String NAME = "name";
        public static final String REQUEST_ID = "request_id";
        public static final String ONLINE = "online";
        public static final String TIMESTAMP = "stamp";
        public static final String TO = "to";
        public static final String FROM = "from";
        public static final String MESSAGE = "message";
        public static final String ID = "id";
    }

    public static JSONObject ConnectionRequest(String name){
        try {
            return new JSONObject().put(Keys.CODE, RequestCodes.CONNECT).put(Keys.NAME, name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static JSONObject IsAliveRequest(){
        try {
            return new JSONObject().put(Keys.CODE, ResponseCodes.IS_ALIVE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public static JSONObject IsAliveResponse(String status){
        try {
            return new JSONObject().put(Keys.CODE, ResponseCodes.IS_ALIVE).put(Keys.STATUS, status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }
}
