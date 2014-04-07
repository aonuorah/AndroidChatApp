package com.samplesocket.app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.samplesocket.classes.App;
import com.samplesocket.classes.AsyncActivity;
import com.samplesocket.classes.Server;
import com.samplesocket.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ConnectActivity extends AsyncActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        if(App.Instance().isConnected){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            App.Instance().socketListener().setContext(this);
            textView = (TextView) findViewById(R.id.responseText);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.connect, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml./**/
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Connect_click(View button){
        EditText text = (EditText)findViewById(R.id.username);
        String name = text.getText().toString();
        App.Instance().setConnectionName(name);
        App.Instance().socketListener().connect();
    }

    @Override
    public void progressUpdate(String... progress) {
        try {
            JSONObject response = new JSONObject(progress[0]);
            if(response.getString(Server.Keys.CODE).equals(Server.RequestCodes.CONNECT)){
                if(response.getString(Server.Keys.STATUS).equals(Server.StatusCodes.SUCCESS)) {
                    App.Instance().socketListener().send(Server.ConnectionRequest(App.Instance().getConnectionName()).toString());
                }else if(response.getString(Server.Keys.STATUS).equals(Server.StatusCodes.ACCEPTED)) {
                    App.Instance().isConnected = true;
                    if (response.has(Server.Keys.ONLINE)) {
                        JSONArray onlineUsers = new JSONArray(response.getString(Server.Keys.ONLINE));
                        for (int i = 0; i < onlineUsers.length(); i++) {
                            App.Instance().addOnlineUser(new User(onlineUsers.getString(i)));
                        }
                    }
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    throw new Exception(response.getString(Server.Keys.STATUS));
                }
            }
        }catch(JSONException ex){
            Log.d(App.DEBUG, ex.getMessage());
        }catch(Exception ex){
            textView.setText(ex.getMessage() );
        }

    }
}
