package com.samplesocket.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.samplesocket.classes.App;
import com.samplesocket.classes.AsyncActivity;
import com.samplesocket.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ConnectActivity extends AsyncActivity {

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
        App.Instance().socketListener().connect(name);
    }

    @Override
    public void progressUpdate(String... progress) {
        try {
            JSONObject response = new JSONObject(progress[0]);
            if(response.getString("code").equals("901")){
                if(response.getString("status").equals("200")) {
                    App.Instance().isConnected = true;
                    if (response.has("online")) {
                        JSONArray onlineUsers = new JSONArray(response.getString("online"));
                        for (int i = 0; i < onlineUsers.length(); i++) {
                            App.Instance().addOnlineUser(new User(onlineUsers.getString(i)));
                        }
                    }
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    TextView textView = (TextView) findViewById(R.id.responseText);
                    textView.setText(response.getString("status") );
                }
            }
        }catch(JSONException ex){
            System.out.println(ex.getMessage());
        }

    }
}
