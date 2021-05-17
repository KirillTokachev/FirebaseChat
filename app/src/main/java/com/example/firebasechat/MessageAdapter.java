package com.example.firebasechat;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class MessageAdapter extends ArrayAdapter<Message> {

    public MessageAdapter(Context context, int resource, List<Message> messages) {
        super(context, resource, messages);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = ((Activity) getContext())
                    .getLayoutInflater().inflate(R.layout.message_item, parent, false);
        }

        ImageView photoImageView = convertView.findViewById(R.id.photoImage);
        TextView messageTextView = convertView.findViewById(R.id.messageTextView);
        TextView userNameTextView = convertView.findViewById(R.id.userNameTextView);

        Message message = getItem(position);

        //Проверка отпраки фото или текста
        boolean isText = message.getImageUrl() == null;

        if (isText) {
            messageTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            messageTextView.setText(message.getTextMessage());
        } else {
            messageTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message.getImageUrl())
                    .into(photoImageView);
        }

        userNameTextView.setText(message.getUserName());

        return convertView;
    }
}