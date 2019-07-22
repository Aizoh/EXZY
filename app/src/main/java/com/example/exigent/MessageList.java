package com.example.exigent;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.exigent.Model.Messages;

import java.util.List;

public class MessageList extends ArrayAdapter<Messages> {
    private Activity context;
    private  List<Messages> messagesList;
    public  MessageList(Activity context, List<Messages>messagesList){
        super(context,R.layout.message_list_layout,messagesList);
        this.context = context;
        this.messagesList = messagesList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = context.getLayoutInflater();
        View listViewItem = layoutInflater.inflate(R.layout.message_list_layout,null,true);
        TextView tvMessage = listViewItem.findViewById(R.id.tvMessage);
        TextView tvDate = listViewItem.findViewById(R.id.tvDate);
        Messages messages = messagesList.get(position) ;

        tvDate.setText(messages.getDate());
        tvMessage.setText(messages.getMessage());
        return listViewItem;
    }
}
