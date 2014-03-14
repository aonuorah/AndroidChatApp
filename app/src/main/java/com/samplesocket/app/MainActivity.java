package com.samplesocket.app;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;


public class MainActivity extends Activity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.receivedText);
        SocketListener s = new SocketListener();
        s.execute();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public class SocketListener extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                startListener();
            }catch(IOException ex){
                Log.d("ServerListnerException: ", ex.getMessage());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress){
            textView.setText(((String[])progress)[0]);
            super.onProgressUpdate(progress);
        }

        private void startListener() throws IOException {
            ServerSocket listener = new ServerSocket(9000);
            try{
                while(true) {
                    char[] c = new char[512];
                    Socket socket = listener.accept();
                    try {
                        InputStreamReader reader = new InputStreamReader(socket.getInputStream());
                        int readChar = reader.read();
                        int count = 0;
                        while (readChar != -1) {
                            c[count++] = (char) readChar;
                            readChar = reader.read();
                        }
                        String s = new String(c);
                        publishProgress(s);
                    }finally{
                        socket.close();
                    }
                }
            }catch(Exception ex){
                Log.d("SocketExeption: ", ex.getMessage());
            }finally{
                listener.close();
            }
        }
    }


}
