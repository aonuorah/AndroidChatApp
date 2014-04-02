package com.samplesocket.classes;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by nedu on 3/30/14.
 */
public class SocketListener {
    private Context context;
    private AsyncReadSocket asyncSocket;
    private Socket socket;
    private BufferedReader reader;
    private String server_ip;
    private int port;

    public SocketListener(String _ip, int _port){
        server_ip = _ip;
        port = _port;
    }


    public void setContext(Context _context){
        context = _context;
    }


    public void connect (){
        asyncSocket = new AsyncReadSocket();
        asyncSocket.execute();
    }

    public void send(String message){
        try {
            if (socket == null || socket.isClosed()) {
                Log.d("", "No socket connection to server " + asyncSocket.getStatus());
                //ToDo create a global to check when there is a connection to server
                //asyncSocket.execute();
            } else {
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                writer.println(message + "\r\n");
                writer.flush();
            }
        }catch(IOException ex){
            Log.d(App.DEBUG,ex.getMessage());
        }
    }

    public void closeSocket(){
        try {
            socket.close();
        }catch(IOException ex){
            Log.d(App.DEBUG, ex.getMessage());
        }
    }


    public class AsyncReadSocket extends AsyncTask<String, String, Void> {
        private boolean isRunning;


        @Override
        protected Void doInBackground(String... params) {
            try {
                isRunning = true;
                startListener();
            } catch (IOException ex) {
                Log.d("ServerListenerException: ", ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            if(context != null) {
                ((AsyncActivity) context).progressUpdate(progress);
            }
            super.onProgressUpdate(progress);
        }


        private void startListener() throws IOException {
            while(isRunning) {
                try {
                    socket = new Socket(server_ip, port);
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    publishProgress("{'code':'2', 'status':'200'}");
                    String line;
                    String response = "";
                    try {
                        while ((line = reader.readLine()) != null) {
                            response += line;
                            if (!reader.ready()) {
                                publishProgress(response);
                                response = "";
                            }
                        }
                    }catch(IOException ex){
                        publishProgress("{'code':'2', 'status':'410'}");
                    }finally{
                        reader.close();
                        socket.close();
                        isRunning = false;
                        App.Instance().isConnected = false;
                    }
                } catch (IOException ex) {//ToDo checkout timeout
                    Log.d(App.DEBUG, ex.getMessage());
                    publishProgress("{'code':'2', 'status':'404'}");
                }
            }
        }

    }

}
