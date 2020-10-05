package com.pepivsky.chat.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.pepivsky.chat.R;
import com.pepivsky.chat.adapter.UserAdapter;
import com.pepivsky.chat.model.User;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    RecyclerView contactsList;
    UserAdapter userAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        contactsList = findViewById(R.id.contactRecyclerList);
        contactsList.setHasFixedSize(true);

        contactsList.setAdapter(userAdapter);

        userAdapter = new UserAdapter(dummyUsers(), this);
    }

    private ArrayList<User> dummyUsers() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Alex"));
        return users;
    }
}