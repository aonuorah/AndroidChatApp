package com.samplesocket.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.samplesocket.app.R;
import com.samplesocket.models.User;

import java.util.ArrayList;

/**
 * Created by nedu on 3/31/14.
 */
public class UserListAdapter extends BaseAdapter {
    private ArrayList<User> data;
    private static LayoutInflater inflater=null;

    public UserListAdapter(LayoutInflater _inflater, ArrayList<User> d) {
        data=d;
        inflater = _inflater;
    }

    @Override
    public int getCount() {
        return data.size();
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
        if(_view == null) {
            _view = inflater.inflate(R.layout.user_list_row, null);
        }

        User user = data.get(i);

        TextView name = (TextView)_view.findViewById(R.id.name);
        name.setText(user.getName());

        return _view;
    }

}
