package com.pepivsky.chat.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.pepivsky.chat.R;
import com.pepivsky.chat.adapter.UserAdapter;
import com.pepivsky.chat.model.User;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    RecyclerView contactsList;
    UserAdapter userAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar myToolbar = findViewById(R.id.my_toolbarMain);
        setSupportActionBar(myToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(true); //muestra el titulo de la app en la toolbar

        contactsList = findViewById(R.id.contactRecyclerList);
        contactsList.setHasFixedSize(true);
        userAdapter = new UserAdapter(dummyUsers(),this);

        contactsList.setAdapter(userAdapter);


    }

    //lista dummy de los usuarios
    private ArrayList<User> dummyUsers() {
        ArrayList<User> users = new ArrayList<>();
        users.add(new User("Alex"));
        users.add(new User("Pepe"));
        users.add(new User("Eric"));
        users.add(new User("Andres"));
        return users;
    }
}