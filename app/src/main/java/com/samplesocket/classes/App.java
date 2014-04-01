package com.samplesocket.classes;

import android.content.Context;

import com.samplesocket.models.Message;
import com.samplesocket.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by nedu on 3/31/14.
 */
public class App {
    private static App app;

    private ArrayList<User> onlineUsers;
    private HashMap<String, ArrayList<Message>> messages;
    private String connectionName;

    public boolean isConnected;
    public SocketListener socketListener;

    private App(){
        onlineUsers = new ArrayList<User>();
        messages = new HashMap<String, ArrayList<Message>>();
    }

    public static App GetGlobalApp(){
        if(app == null){
            app = new App();
        }
        return app;
    }

    public SocketListener GetSocketListener(Context _context){
        if(socketListener == null){
           socketListener = new SocketListener();
        }
        socketListener.setContext(_context);
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
            String response_code = response.getString("code");

            if(response_code.equals("902")){
                String _from = response.getString("from");
                String _stamp = response.getString("stamp");
                String _mes = response.getString("message");
                Message _message = new Message(_from, getConnectionName(), _mes, _stamp);
                this.addMessage(_from, _message);

            }else if(response_code.equals("903")){
                int _id = Integer.valueOf(response.getString("id"));
                String _to = response.getString("to");
                this.getMessages(_to).get(_id).setDelivered();

            }else if(response_code.equals("904")){
                this.addOnlineUser(new User(response.getString("id")));

            }else if(response_code.equals("905")){
                this.removeOnlineUser(response.getString("id"));

            }
        }catch(JSONException ex){
            System.out.println(ex.getMessage());
        }
    }

}
