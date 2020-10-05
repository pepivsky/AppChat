package com.pepivsky.chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pepivsky.chat.R;
import com.pepivsky.chat.model.User;
import com.pepivsky.chat.view.ChatActivity;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> { //viewHolder

    ArrayList<User> users;
    Context context; //contexto para que funcione el toast

    public UserAdapter(ArrayList<User> users, Context context) { // recibir Activity
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User currentUser = users.get(position);
        holder.userName.setText(currentUser.getUsername());
        holder.goToChatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Chateando con el usuario " + currentUser.getUsername(), Toast.LENGTH_SHORT).show();
                //Intent con extras
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("user", currentUser.getUsername());
                context.startActivity(intent); //lanza la actividad del chat
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    //Clase viewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView userName;
        Button goToChatButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.username);
            goToChatButton = itemView.findViewById(R.id.goChatButton);
        }
    }

}
