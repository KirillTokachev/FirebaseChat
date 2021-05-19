package com.example.firebasechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class UserListActivity extends AppCompatActivity {

    private DatabaseReference usersDatabaseReference;
    private ChildEventListener usersChildEventListener;
    private FirebaseAuth auth;

    private ArrayList<User> userArrayList;
    private RecyclerView userRecyclerView;
    private UserAdapter userAdapter;
    private RecyclerView.LayoutManager userLayoutManager;

    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        Intent getUsArName = getIntent();

        if (getUsArName != null){
            userName = getUsArName.getStringExtra(userName);
        }


        auth = FirebaseAuth.getInstance();

        userArrayList = new ArrayList<>();

        attachUserDatabaseReferenceListener();
        buildRecyclerView();


    }

    private void attachUserDatabaseReferenceListener() {
        usersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        if (usersChildEventListener == null){
            usersChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                    User user = snapshot.getValue(User.class);
                    // Проверка есть ли пользователь есть то его не отображаем
                    if (!user.getId().equals(auth.getCurrentUser().getUid())){
                        user.setAvatarMockUpResource(R.drawable.ic_baseline_person_pin_24);
                        userArrayList.add(user);
                        userAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            };

            usersDatabaseReference.addChildEventListener(usersChildEventListener);
        }
    }

    private void buildRecyclerView() {
        userRecyclerView = findViewById(R.id.userListRecyclerView);
        userRecyclerView.setHasFixedSize(true);
        // Разделитель пользователлей в RecyclerView
        userRecyclerView.addItemDecoration(new DividerItemDecoration(userRecyclerView
                .getContext(), DividerItemDecoration.VERTICAL));
        userLayoutManager = new LinearLayoutManager(this);
        userAdapter = new UserAdapter(userArrayList);

        userRecyclerView.setLayoutManager(userLayoutManager);
        userRecyclerView.setAdapter(userAdapter);

        userAdapter.setOnUserClickListener(new UserAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(int position) {
                goToChat(position);
            }
        });
    }

    private void goToChat(int position) {
        Intent intent = new Intent(UserListActivity.this, ChatActivity.class);
        // Получение позиции пользователя при кликание.
        intent.putExtra("recipientUserId", userArrayList.get(position).getId());
        intent.putExtra("recipientUserName", userArrayList.get(position).getUserName());
        intent.putExtra("userName", userName);

        startActivity(intent);
    }

    // Реализация выхода к меню входа
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.sing_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(UserListActivity.this,
                        SingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}