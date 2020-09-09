package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import model.UserModel;

public class SignupActivity extends AppCompatActivity {

    private EditText email;
    private EditText name;
    private EditText password;
    private Button signup;
    private Button verifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        email = findViewById(R.id.signupActivity_edittext_email);
        name = findViewById(R.id.signupActivity_edittext_name);
        password = findViewById(R.id.signupActivity_edittext_password);
        signup = findViewById(R.id.signupActivity_button_signup);
        verifier = findViewById(R.id.signupActivity_button_verifier);

        verifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()) {
                                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(SignupActivity.this,FirebaseAuth.getInstance().getCurrentUser().getEmail()+"님의 메일을 확인해 주세요.",Toast.LENGTH_LONG).show();
                                                signup.setEnabled(true);
                                            } else {
                                                Log.e("Email Verifier Error","sendEmailVerification",task.getException());
                                                Toast.makeText(SignupActivity.this, "이메일 전송에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString() == null ||
                        name.getText().toString()==null || password.getText().toString() == null ) {
                    return;
                }

                            UserModel userModel = new UserModel();

                                final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                userModel.userName = name.getText().toString();
                                userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        SignupActivity.this.finish();
                                    }

                                                                       });
                                userModel.userName = name.getText().toString();
                                userModel.uid = uid;
                                userModel.email = email.getText().toString();
                                userModel.flag = false;
                                userModel.usingSeatNum = 0;

                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel);

                            }
                        });
                }
        }