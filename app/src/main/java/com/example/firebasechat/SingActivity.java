package com.example.firebasechat;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SingActivity extends AppCompatActivity {

    private static final String TAG = "SingActivity";

    private FirebaseAuth auth;

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText nameEditText;
    private TextView toggleLoginSingUpTextView;
    private Button loginSingInButton;

    private boolean loginModActive;

    FirebaseDatabase database;
    DatabaseReference usersDataBaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing);

        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        usersDataBaseReference = database.getReference().child("users");


        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        toggleLoginSingUpTextView = findViewById(R.id.toggleLoginSingUpTextView);
        loginSingInButton = findViewById(R.id.loginSingInButton);

        // Реализация регистрации или входа в приложение
        loginSingInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSingInUser(emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim());
            }
        });

        // Проверка аутентификации
        if (auth.getCurrentUser() != null){
            startActivity(new Intent(SingActivity.this, MainActivity.class));
        }

    }
    // Реализация регистрации или входа в приложение
    // + Валидация пароля
    private void loginSingInUser(String email, String password) {

        if (loginModActive){
            if (passwordEditText.getText().toString().trim().length() <= 6) {
                Toast.makeText(this, "Password must contain at least 6 characters",
                        Toast.LENGTH_LONG).show();
            } else if (emailEditText.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please enter your mail", Toast.LENGTH_LONG).show();
            } else {
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = auth.getCurrentUser();
                                    Intent intent = new Intent(SingActivity.this, MainActivity.class);
                                    intent.putExtra("userName", nameEditText.getText().toString().trim());
                                    startActivity(intent);
                                    Toast.makeText(SingActivity.this, "Authentication complete",
                                            Toast.LENGTH_LONG).show();
                                    //updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SingActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });
            }

        } else {
            // Валидация повтора ввода пороля
            if (!passwordEditText.getText().toString().trim()
                    .equals(confirmPasswordEditText.getText().toString().trim())){
                Toast.makeText(this, "Passwords don't match", Toast.LENGTH_LONG).show();
            } else if (passwordEditText.getText().toString().trim().length() <= 6) {
                Toast.makeText(this, "Password must contain at least 6 characters",
                        Toast.LENGTH_LONG).show();
            } else if (emailEditText.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please enter your mail", Toast.LENGTH_LONG).show();
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    Toast.makeText(SingActivity.this, "Authentication complete",
                                            Toast.LENGTH_LONG).show();
                                    FirebaseUser user = auth.getCurrentUser();

                                    // Сохраннение в бд данных юзера при регистрации
                                    createUser(user);

                                    Intent intent = new Intent(SingActivity.this, MainActivity.class);
                                    intent.putExtra("userName", nameEditText.getText().toString().trim());
                                    startActivity(intent);
                                    // updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SingActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });
            }
        }
    }

    // Реализация метода сохранения данных пользоваетлей в БД
    private void createUser(FirebaseUser firebaseUser) {
        User user = new User();
        user.setId(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        user.setUserName(nameEditText.getText().toString().trim());

        usersDataBaseReference.push().setValue(user);
    }

    // Выбор регистрация или вход
    public void toggleLoginMod(View view) {

        if (loginModActive){
            loginModActive = false;
            loginSingInButton.setText("Sing Up");
            toggleLoginSingUpTextView.setText("Log in");
            confirmPasswordEditText.setVisibility(View.VISIBLE);
        } else {
            loginModActive = true;
            loginSingInButton.setText("Log in");
            toggleLoginSingUpTextView.setText("Sing up");
            confirmPasswordEditText.setVisibility(View.GONE);
        }

    }
}