package com.pepivsky.chat.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.pepivsky.chat.R;

public class ContactDetailActivity extends AppCompatActivity {
    TextView tvUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        //obtieniendo los extras
        Bundle extras = getIntent().getExtras();
        tvUserName.setText(extras.getString("user"));
    }

    private void initComponents() {
        tvUserName = findViewById(R.id.tvUsername);
    }
}