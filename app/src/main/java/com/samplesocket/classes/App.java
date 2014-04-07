package com.samplesocket.classes;

import android.util.Log;

import com.samplesocket.models.Message;
import com.samplesocket.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nedu on 3/31/14.
 */
public class App{
    //private static final String server_ip = "192.168.0.6";
    private static final String server_ip = "86.11.223.177";
    private static final int port = 9090;
    private static App app;
    public static final String DEBUG = "App log";

    private ArrayList<User> onlineUsers;
    private HashMap<String, ArrayList<Message>> messages;
    private String connectionName;

    public boolean isConnected;
    public SocketListener socketListener;

    private App(){
        init();
    }


    public static App Instance(){
        if(app == null){
            app = new App();
        }
        return app;
    }

    public void logout(){
        socketListener.closeSocket();
    }

    public void init(){
        isConnected = false;
        onlineUsers = new ArrayList<User>();
        messages = new HashMap<String, ArrayList<Message>>();
    }

    public SocketListener socketListener(){
        if(socketListener == null){
           socketListener = new SocketListener(server_ip, port);
        }
        return socketListener;
    }

    public ArrayList<User> getOnlineUsers(){
        return onlineUsers;
    }

    public void addOnlineUser(User user){
        onlineUsers.add(user);
    }

    public void removeOnlineUser(String user_id){
        for(int i = 0; i < onlineUsers.size(); i++){
            if(onlineUsers.get(i).getName().equals(user_id)){
                onlineUsers.remove(i);
                return;
            }
        }
    }

    public String getConnectionName(){
        return connectionName;
    }

    public void setConnectionName(String name){
        connectionName = name;
    }

    public int addMessage(String user_id, Message _message){
        if(!messages.containsKey(user_id)){
            messages.put(user_id, new ArrayList<Message>());
        }

        messages.get(user_id).add(_message);
        return messages.get(user_id).size() - 1;
    }

    public ArrayList<Message> getMessages(String user_id){
        if(!messages.containsKey(user_id)){
            messages.put(user_id, new ArrayList<Message>());
        }
        return messages.get(user_id);
    }

    public void update(String socketResponse){
        try {
            JSONObject response = new JSONObject(socketResponse);
            String response_code = response.getString(Server.Keys.CODE);

            if(response_code.equals(Server.ResponseCodes.NEW_MESSAGE)){//new message received
                String _from = response.getString(Server.Keys.FROM);
                String _stamp = response.getString(Server.Keys.TIMESTAMP);
                String _mes = response.getString(Server.Keys.MESSAGE);
                String _reqID = response.getString(Server.Keys.REQUEST_ID);
                Message _message = new Message(_from, getConnectionName(), _mes, _stamp);
                this.addMessage(_from, _message);
                this.socketListener().send(new JSONObject().put(Server.Keys.REQUEST_ID, _reqID)
                                                            .put(Server.Keys.STATUS, Server.StatusCodes.SUCCESS)
                                                            .toString());


            }else if(response_code.equals(Server.ResponseCodes.SENT_MESSAGE_UPDATE)){//update on sent message received
                int _id = Integer.valueOf(response.getString(Server.Keys.ID));
                String _to = response.getString(Server.Keys.TO);
                this.getMessages(_to).get(_id).setDelivered();

            }else if(response_code.equals(Server.ResponseCodes.USER_UPDATE_ONLINE)){//new user just came online
                this.addOnlineUser(new User(response.getString(Server.Keys.NAME)));

            }else if(response_code.equals(Server.ResponseCodes.USER_UPDATE_OFFLINE)){//a user just went offline
                this.removeOnlineUser(response.getString(Server.Keys.NAME));

            }else if(response_code.equals(Server.ResponseCodes.IS_ALIVE)){//is alive request from server
                this.socketListener().send(response.put(Server.Keys.STATUS, Server.StatusCodes.SUCCESS).toString());
            }
        }catch(JSONException ex){
            Log.d(DEBUG, ex.getMessage());
        }
    }

}
