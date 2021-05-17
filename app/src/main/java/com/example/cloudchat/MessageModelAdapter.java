package com.example.cloudchat;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.List;

public class MessageModelAdapter extends BaseAdapter {

    private FirebaseAuth mAuth;
    private List<Messages> list;
    private LayoutInflater layoutInflater;
    private static final SimpleDateFormat showFormat = new SimpleDateFormat("dd.MM.yy hh:mm");
    private String nameClass;

    public MessageModelAdapter(Context context, List<Messages> list) {
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        nameClass = context.getClass().getName();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            if(nameClass.equals("com.example.cloudchat.ShowAllListChats")) {
                view = layoutInflater.inflate(R.layout.item_list_chats_layout, parent, false);
            }
            else {
                view = layoutInflater.inflate(R.layout.item_layout, parent, false);
            }
        }
        mAuth = FirebaseAuth.getInstance();

        Messages messages = getMessageModel(position);

        TextView textAuthor;
        TextView textMessage;
        TextView dateCreate;

        if(nameClass.equals("com.example.cloudchat.ShowAllListChats")) {
            textAuthor = view.findViewById(R.id.name_of_chat);
            textMessage = view.findViewById(R.id.text_message_prev);
            dateCreate = view.findViewById(R.id.date_create_prev);
        }
        else {
            LinearLayout linearLayout = view.findViewById(R.id.linear_layout);
            LinearLayout linearLayout2 = view.findViewById(R.id.linear_layout_2);

            textAuthor = view.findViewById(R.id.text_author);
            textMessage = view.findViewById(R.id.text_message);
            dateCreate = view.findViewById(R.id.date_create);

            if (messages.getIdAuthor().equals("ID_" + UserSaved.parcerId(mAuth.getCurrentUser().getEmail()))) {
                linearLayout.setGravity(Gravity.RIGHT);
                linearLayout2.setGravity(Gravity.RIGHT);
                textMessage.setGravity(Gravity.RIGHT);
            } else {
                linearLayout.setGravity(Gravity.LEFT);
                linearLayout2.setGravity(Gravity.LEFT);
                textMessage.setGravity(Gravity.LEFT);
            }
        }

        textAuthor.setText(messages.getAuthor());
        textMessage.setText(messages.getMessage());
        dateCreate.setText(showFormat.format(messages.getDate()));

        return view;
    }

    private Messages getMessageModel(int position){
        return (Messages) getItem(position);
    }
}
