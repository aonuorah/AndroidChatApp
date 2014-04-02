package com.samplesocket.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.samplesocket.app.R;
import com.samplesocket.classes.App;
import com.samplesocket.models.Message;

import java.util.ArrayList;

/**
 * Created by nedu on 3/31/14.
 */
public class MessageListAdapter extends BaseAdapter {
    private ArrayList<Message> messages;
    private LayoutInflater inflater;

    public MessageListAdapter(LayoutInflater _inflater, ArrayList<Message> _messages){
        messages = _messages;
        inflater = _inflater;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View _view = view;
        Message message = messages.get(i);

        if(_view == null) {
            if(message.getFrom().equals(App.Instance().getConnectionName())) {
                _view = inflater.inflate(R.layout.message_list_to_row, null);
            }else{
                _view = inflater.inflate(R.layout.message_list_from_row, null);
            }
        }

        TextView messageText = (TextView)_view.findViewById(R.id.message);
        messageText.setText(message.getMessage());

        TextView timestamp = (TextView)_view.findViewById(R.id.timestamp);
        timestamp.setText(message.getTimestamp());

        return _view;
    }

    public int addMessage(Message _message){
        messages.add(_message);
        notifyDataSetChanged();
        return messages.size() - 1;
    }

    public ArrayList<Message> getMessages(){
        return messages;
    }
}
