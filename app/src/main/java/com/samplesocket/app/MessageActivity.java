package com.samplesocket.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.samplesocket.adapters.MessageListAdapter;
import com.samplesocket.classes.App;
import com.samplesocket.classes.AsyncActivity;
import com.samplesocket.models.Message;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MessageActivity extends AsyncActivity {
    private String to;
    private MessageListAdapter messageListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        Intent intent = getIntent();
        to = intent.getStringExtra("user_id");

        ListView messageList = (ListView)findViewById(R.id.messageList);
        messageListAdapter = new MessageListAdapter(this.getLayoutInflater(), App.GetGlobalApp().getMessages(to));
        messageList.setAdapter(messageListAdapter);
        setTitle(to);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.message, menu);
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

    public void Send_click(View button){
        EditText text = (EditText)findViewById(R.id.new_message);
        String message = text.getText().toString();
        text.setText("");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        String stamp = sdf.format(calendar.getTime());
        Message _message = new Message(App.GetGlobalApp().getConnectionName(), to, message, stamp);
        int _id = App.GetGlobalApp().addMessage(to, _message);
        try {
            JSONObject jsonMessage = new JSONObject()
                    .put("id", _id)
                    .put("to", to)
                    .put("message", message)
                    .put("from",App.GetGlobalApp().getConnectionName())
                    .put("stamp", stamp);
            App.GetGlobalApp().GetSocketListener(this).send(jsonMessage.toString());
            messageListAdapter.notifyDataSetChanged();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        } catch(JSONException ex){
            System.out.println(ex.getMessage());
        }
    }


    @Override
    public void progressUpdate(String... progress) {
        App.GetGlobalApp().update(progress[0]);
        messageListAdapter.notifyDataSetChanged();
    }
}
