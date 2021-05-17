package com.example.firebasechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView messageListView;
    private MessageAdapter adapter;
    private ProgressBar progressBar;
    private ImageButton sendImageButton;
    private Button sendMessageButton;
    private EditText messageEditText;
    private String userName;

    // Добавляем БД Firebase
    FirebaseDatabase database;
    DatabaseReference messagesDataBaseReference;

    //Слушатель сообщений с помощью котогоро они отображаются в View
    ChildEventListener messagesChildEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Имплементируем БД Firebase
        database = FirebaseDatabase.getInstance();
        messagesDataBaseReference = database.getReference().child("messages");


        progressBar = findViewById(R.id.progress_Bar);
        sendImageButton = findViewById(R.id.sendPhotoButton);
        sendMessageButton = findViewById(R.id.sendMassageButton);
        messageEditText = findViewById(R.id.messageEditText);


        // Присваивание имени пользователя
        Intent intent = getIntent();
        if (intent != null){
            userName = intent.getStringExtra("userName");
        } else {
            userName = "default UserName";
        }


        messageListView = findViewById(R.id.sendMessageListView);
        List<Message> messages = new ArrayList<>();
        adapter = new MessageAdapter(this, R.layout.message_item, messages);
        messageListView.setAdapter(adapter);

        progressBar.setVisibility(ProgressBar.INVISIBLE);

        // Настраиваем слушатель ввода
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0){
                    sendMessageButton.setEnabled(true);
                } else {
                    sendMessageButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Ограничиваем число символов в сообщении
        messageEditText.setFilters(new InputFilter[]
                {new InputFilter.LengthFilter(500)});

        // Отправка сообщений по клику на кнопку
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Message message = new Message();
                message.setTextMessage(messageEditText.getText().toString());
                message.setUserName(userName);
                message.setImageUrl(null);

                messagesDataBaseReference.push().setValue(message);

                messageEditText.setText("");
            }
        });

        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        // Инициализация ChildEventListener
        // Отображение собщений в чате
        messagesChildEventListener = new ChildEventListener() {
            @Override
            // Добавление потомка
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                Message message = snapshot.getValue(Message.class);

                adapter.add(message);
            }

            @Override
            // Изменение потомка
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            // Удаление потомка
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            // Перемещение потомка
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            // Ошибка в БД
            public void onCancelled(DatabaseError error) {

            }
        };

        // Прикрепление ChildEventListener к messagesDataBaseReference
        messagesDataBaseReference.addChildEventListener(messagesChildEventListener);

    }

    // Меню
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // Реализация выбора в меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.sing_out:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,
                        SingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
