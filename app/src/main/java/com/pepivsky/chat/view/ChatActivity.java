package com.pepivsky.chat.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pepivsky.chat.R;
import com.pepivsky.chat.adapter.MessageAdapter;
import com.pepivsky.chat.adapter.UserAdapter;
import com.pepivsky.chat.model.Message;
import com.pepivsky.chat.model.User;
import com.pepivsky.chat.pubnub.PUBNUB;
import com.pepivsky.chat.pubnub.utils.UserPresenceCallback;
import com.pepivsky.chat.view.ContactDetailActivity;

import java.util.ArrayList;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    FloatingActionButton sendButton;
    EditText edtMessage;
    RecyclerView messagesList;
    MessageAdapter adapter;
    TextView tvUserNameToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        initComponents();

        messagesList.setHasFixedSize(true);
        adapter = new MessageAdapter(new ArrayList<Message>());
        messagesList.setAdapter(adapter);

        //obtener los extras
        Bundle extras = getIntent().getExtras();
        //setear el username
        tvUserNameToolbar.setText(extras.getString("user"));


        PUBNUB.getInstance().connect(success -> Log.d("PUBNUB -> UserConnected", success.toString()), new UserPresenceCallback() {
            @Override
            public void userJoin(User user) {
                Log.d("PUBNUB -> UserJoin", user.getUsername());
            }

            @Override
            public void userLeave(User user) {
                Log.d("PUBNUB -> User Leave", user.getUsername());
            }
        });

        PUBNUB.getInstance().setMessagesCallback(message -> {
            Log.d("PUBNUB", message.getContent());
            runOnUiThread(() -> adapter.add(message));
        });


        sendButton.setOnClickListener(v -> {
            PUBNUB.getInstance().publish(new Message(edtMessage.getText().toString(), PUBNUB.MY_UUID));
            edtMessage.setText("");
        });

    }

    private void initComponents() {
        //toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        //components
        sendButton = findViewById(R.id.fabSend);
        edtMessage = findViewById(R.id.edtMessage);
        tvUserNameToolbar = findViewById(R.id.tvUsernameToolbar);

        messagesList = findViewById(R.id.recyclerMessages);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PUBNUB.getInstance().unsuscribe();
    }

    /*private ArrayList<Message> dummyMessages() {
        ArrayList<Message> messages = new ArrayList<>();

        messages.add(new Message("Hola", true));
        messages.add(new Message("Hola", false));
        messages.add(new Message("como te va?", true));
        messages.add(new Message("bien :), y a ti?", false));


        return messages;
    }*/


    //Creando menu de la toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ver_contacto:
                Intent intent = new Intent(this, ContactDetailActivity.class);
                intent.putExtra("user", tvUserNameToolbar.getText().toString());
                startActivity(intent);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }


}