package com.pepivsky.chat.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pepivsky.chat.R;
import com.pepivsky.chat.model.Message;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    ArrayList<Message> messages;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;


    public MessageAdapter(ArrayList<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = this.messages.get(position);
        if (message.getPublisher().equalsIgnoreCase("Yo")) {
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case VIEW_TYPE_MESSAGE_SENT:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message_item, parent, false);
                break;
            case  VIEW_TYPE_MESSAGE_RECEIVED:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + viewType);
        }
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message currentMessage = messages.get(position);
        holder.publisher.setText(currentMessage.getPublisher());
        holder.contentMessage.setText(currentMessage.getContent());
        /*if (currentMessage.isMine()) { //si el mensaje es mio ponerlo a la derecha
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.END;
            holder.contentMessage.setLayoutParams(params);
        }*/
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void add(Message message) {
        this.messages.add(message);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView publisher;
        TextView contentMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.publisher = itemView.findViewById(R.id.publisher);
            this.contentMessage = itemView.findViewById(R.id.tvContentMessage);
        }
    }
}
