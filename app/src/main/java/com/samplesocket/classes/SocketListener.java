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

    //private static final String server_ip = "192.168.0.6";
    private static final String server_ip = "86.11.223.177";
    private static final int port = 9090;

    public SocketListener(){
       initAsyncSocket();
    }

    public void setContext(Context _context){
        context = _context;
    }

    private void resetSocket(){
        initAsyncSocket();
    }

    private void initAsyncSocket(){
        asyncSocket = new AsyncReadSocket();
        asyncSocket.execute();
    }

    public void connect (String name){
        try {
            send("connect " + name);
        }catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    public void send(String message)throws IOException {
        if (socket == null) {
            System.out.println("No socket connection to server");
            resetSocket();
        } else if (socket.isClosed()) {
            resetSocket();
        } else{
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.println(message + "\r\n");
            writer.flush();
        }
    }


    public class AsyncReadSocket extends AsyncTask<String, String, Void> {
        int connect_retries;

        @Override
        protected Void doInBackground(String... params) {
            try {
                startListener();
            } catch (IOException ex) {
                Log.d("ServerListenerException: ", ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            ((AsyncActivity)context).progressUpdate(progress);
            super.onProgressUpdate(progress);
        }

        private void startListener() throws IOException {
            connect_retries = 0;
            while(true) {
                try {
                    if (socket == null || socket.isClosed())
                        socket = new Socket(server_ip, port);

                    String line;
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String response = "";
                    while ((line = reader.readLine()) != null) {
                        response += line;
                        if (!reader.ready()) {
                            publishProgress(response);
                            response = "";
                        }
                    }
                    socket.close();
                    App.GetGlobalApp().isConnected = false;

                } catch (IOException ex) {
                    Log.d("SocketException: ", ex.getMessage());
                    connect_retries++;
                    if(connect_retries > 0)
                        break;
                }
            }
        }

    }

}
