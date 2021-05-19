package com.example.firebasechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ListView messageListView;
    private MessageAdapter adapter;
    private ProgressBar progressBar;
    private ImageButton sendImageButton;
    private Button sendMessageButton;
    private EditText messageEditText;
    private String userName;

    private String recipientUserId;
    private String recipientUserName;

    private static final int RC_IMAGE_PICKER = 900;

    // Добавляем БД Firebase
    private FirebaseDatabase database;
    private DatabaseReference messagesDataBaseReference;
    private FirebaseAuth authUsID;

    //Слушатель сообщений с помощью котогоро они отображаются в View
    private ChildEventListener messagesChildEventListener;

    // хранилище БД
    private FirebaseStorage storage;
    private StorageReference chatImgStorageReference;

    private DatabaseReference usersDatabaseReference;
    private ChildEventListener usersChildEventListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        authUsID = FirebaseAuth.getInstance();

        // получение данных из UserListActivity
        Intent getUsId = getIntent();
        // Присваивание имени пользователя

        if (getUsId != null){
            userName = getUsId.getStringExtra("userName");
            recipientUserId = getUsId.getStringExtra("recipientUserId");
            recipientUserName = getUsId.getStringExtra("recipientUserName");
        }

        setTitle("Chating wich " + recipientUserName);


        // Имплементируем БД Firebase
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        chatImgStorageReference = storage.getReference().child("chat_image");
        messagesDataBaseReference = database.getReference().child("messages");
        usersDatabaseReference = database.getReference().child("users");

        progressBar = findViewById(R.id.progress_Bar);
        sendImageButton = findViewById(R.id.sendPhotoButton);
        sendMessageButton = findViewById(R.id.sendMassageButton);
        messageEditText = findViewById(R.id.messageEditText);





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
                message.setSender(authUsID.getCurrentUser().getUid());
                message.setRecipient(recipientUserId);

                messagesDataBaseReference.push().setValue(message);

                messageEditText.setText("");
            }
        });

        // Реализация отправки изображений в чат
        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent,"Choose in image"),
                        RC_IMAGE_PICKER);
            }
        });


        // Инициализация ChildEventListener
        usersChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                User user = snapshot.getValue(User.class);
                if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                    userName = user.getUserName();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        };

        usersDatabaseReference.addChildEventListener(usersChildEventListener);

        // Инициализация ChildEventListener
        // Отображение собщений в чате
        messagesChildEventListener = new ChildEventListener() {
            @Override
            // Добавление потомка
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                Message message = snapshot.getValue(Message.class);

                // Проверка отпрителя и получателя сообщений.
                if (message.getSender().equals(authUsID.getCurrentUser().getUid())
                && message.getRecipient().equals(recipientUserId)){
                    // Проверка какой бабал чат показывать
                    message.setWhoseIsMessage(true);
                    adapter.add(message);
                } else if (message.getRecipient().equals(authUsID.getCurrentUser().getUid())
                        && message.getSender().equals(recipientUserId)){
                    message.setWhoseIsMessage(false);
                    adapter.add(message);
                }
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
                startActivity(new Intent(ChatActivity.this,
                        SingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Реализация отображения изображения в чате
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_IMAGE_PICKER && resultCode == RESULT_OK){
            Uri selectedImageUri = data.getData();
            final StorageReference imgReference = chatImgStorageReference
                    .child(selectedImageUri.getLastPathSegment());

            UploadTask uploadTask = imgReference.putFile(selectedImageUri);

            uploadTask = imgReference.putFile(selectedImageUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return imgReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Message message = new Message();
                        message.setImageUrl(downloadUri.toString());
                        message.setUserName(userName);
                        message.setSender(authUsID.getCurrentUser().getUid());
                        message.setRecipient(recipientUserId);
                        messagesDataBaseReference.push().setValue(message);
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }
}
