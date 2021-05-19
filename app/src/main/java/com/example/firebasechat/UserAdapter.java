package com.example.firebasechat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.
        UserViewHolder> {

    private ArrayList<User> users;
    private OnUserClickListener listener;

    public interface OnUserClickListener{
        void onUserClick(int position);
    }

    public void setOnUserClickListener(OnUserClickListener listener){
        this.listener = listener;
    }

    public UserAdapter(ArrayList<User> users) {
        this.users = users;
    }
    @NonNull
    @NotNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item,parent,false);
        UserViewHolder userViewHolder = new UserViewHolder(view, listener);
        return userViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdapter.UserViewHolder holder, int position) {
        User currentUser = users.get(position);
        holder.avatarImageView.setImageResource(currentUser.getAvatarMockUpResource());
        holder.userNameTextView.setText(currentUser.getUserName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    // Реализация выбора собеседника по клику
    public static class UserViewHolder extends RecyclerView.ViewHolder{

        public ImageView avatarImageView;
        public TextView userNameTextView;

        public UserViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView,
                              OnUserClickListener listener) {
            super(itemView);

            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            userNameTextView = itemView.findViewById(R.id.userNameTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onUserClick(position);
                        }
                    }
                }
            });
        }
    }
}
