package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import model.UserModel;

public class SignupActivity extends AppCompatActivity {

    private EditText email;
    private EditText name;
    private EditText password;
    private Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = findViewById(R.id.signupActivity_edittext_email);
        name = findViewById(R.id.signupActivity_edittext_name);
        password = findViewById(R.id.signupActivity_edittext_password);
        signup = findViewById(R.id.signupActivity_button_signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString() == null ||
                        name.getText().toString()==null || password.getText().toString() == null ) {
                    return;
                }

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            UserModel userModel = new UserModel();
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                final String uid = task.getResult().getUser().getUid();

                                userModel.userName = name.getText().toString();
                                userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        SignupActivity.this.finish();
                                    }

                                        });
                                UserModel userModel = new UserModel();
                                userModel.userName = name.getText().toString();
                                userModel.uid = uid;
                                userModel.email = email.getText().toString();
                                userModel.flag = false;

                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);

                            }
                        });
            }
        });
    }
}