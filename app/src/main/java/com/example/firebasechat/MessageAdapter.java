package com.example.firebasechat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class MessageAdapter extends ArrayAdapter<Message> {

    private List <Message> messagesList;
    private Activity activity;

    public MessageAdapter(Activity context, int resource, List<Message> messages) {
        super(context, resource, messages);
        this.messagesList = messages;
        this.activity = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        LayoutInflater layoutInflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        Message messages = getItem(position);
        int layoutResource = 0;
        int viewType = getItemViewType(position);

        if (viewType == 0) {
            layoutResource = R.layout.my_message_item;
        } else {
            layoutResource = R.layout.your_message_item;
        }


        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = layoutInflater.inflate(
                    layoutResource, parent, false
            );
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        //Проверка отпраки фото или текста
        boolean isText = messages.getImageUrl() == null;
        if (isText){
            viewHolder.bubbleText.setVisibility(View.VISIBLE);
            viewHolder.photoImageView.setVisibility(View.GONE);
            viewHolder.bubbleText.setText(messages.getTextMessage());
        } else {
            viewHolder.bubbleText.setVisibility(View.GONE);
            viewHolder.photoImageView.setVisibility(View.VISIBLE);
            Glide.with(viewHolder.photoImageView.getContext())
                    .load(messages.getImageUrl())
                    .into(viewHolder.photoImageView);
        }
        return convertView;
    }


    @Override
    public int getItemViewType(int position) {

        int flag;
        Message message = messagesList.get(position);

        if (message.isWhoseIsMessage()){
            flag = 0;
        } else {
            flag = 1;
        }
        return flag;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    // Реализация отображения сообщений в бабле
    private class ViewHolder{

        private TextView bubbleText;
        private ImageView photoImageView;

        public ViewHolder(View view){
            photoImageView = view.findViewById(R.id.photoImageView);
            bubbleText = view.findViewById(R.id.bubbleText);
        }

    }

}