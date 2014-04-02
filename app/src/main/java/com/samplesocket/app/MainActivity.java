package com.samplesocket.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.samplesocket.adapters.UserListAdapter;
import com.samplesocket.classes.App;
import com.samplesocket.classes.AsyncActivity;

public class MainActivity extends AsyncActivity {
    private UserListAdapter onlineUsersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        onlineUsersAdapter = new UserListAdapter(this.getLayoutInflater(), App.Instance().getOnlineUsers());

        final Context context = this;
        ListView onlineUsersList = (ListView)findViewById(R.id.onlineUsers);
        onlineUsersList.setAdapter(onlineUsersAdapter);
        onlineUsersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("user_id", App.Instance().getOnlineUsers().get(position).getName());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        App.Instance().socketListener().setContext(this);//to receive async progress
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
        }else if (id == R.id.action_logout) {
            App.Instance().clear();
            Intent intent = new Intent(this, ConnectActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void progressUpdate(String... progress) {
        App.Instance().update(progress[0]);
        onlineUsersAdapter.notifyDataSetChanged();
    }
}
