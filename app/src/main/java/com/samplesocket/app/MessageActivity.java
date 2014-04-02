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

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MessageActivity extends AsyncActivity {
    private String to;
    private ListView messageList;
    private EditText newMessage;
    private MessageListAdapter messageListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        newMessage = (EditText)findViewById(R.id.new_message);

        Intent intent = getIntent();
        to = intent.getStringExtra("user_id");

        messageList = (ListView)findViewById(R.id.messageList);
        messageListAdapter = new MessageListAdapter(this.getLayoutInflater(), App.Instance().getMessages(to));
        messageList.setAdapter(messageListAdapter);
        setTitle(to);
    }

    @Override
    public void onResume(){
        super.onResume();
        App.Instance().socketListener().setContext(this);//to receive async progress
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
        String message = newMessage.getText().toString();
        newMessage.setText("");
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        String stamp = sdf.format(calendar.getTime());
        Message _message = new Message(App.Instance().getConnectionName(), to, message, stamp);
        int _id = App.Instance().addMessage(to, _message);
        try {
            JSONObject jsonMessage = new JSONObject()
                    .put("id", _id)
                    .put("to", to)
                    .put("message", message)
                    .put("from",App.Instance().getConnectionName())
                    .put("stamp", stamp);
            App.Instance().socketListener().send(jsonMessage.toString());
            messageListAdapter.notifyDataSetChanged();
        } catch(JSONException ex){
            System.out.println(ex.getMessage());
        }
    }


    @Override
    public void progressUpdate(String... progress) {
        App.Instance().update(progress[0]);
        messageListAdapter.notifyDataSetChanged();
    }
}
