package com.example.app1.ConnectActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app1.Activity.MainActivity;
import com.example.app1.BusinessActivity.MainBusiness;
import com.example.app1.R;
import com.example.app1.UserActivity.UserProfileActivity;
import com.google.api.SystemParameterOrBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SigninupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signinup);
        EditText edtUsername, edtPassword;
        Button loginbtn = findViewById(R.id.Login);

        edtPassword= (MaterialEditText)findViewById(R.id.password);
        edtUsername= (MaterialEditText)findViewById(R.id.Name);

        //Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference ("user");

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(SigninupActivity.this, MainActivity.class);
//                startActivity(intent);

                ProgressDialog mDialog = new ProgressDialog(SigninupActivity.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //mDialog.dismiss();
                        if(dataSnapshot.child(edtUsername.getText().toString()).exists()) {
                            //Get user Information
                            User user = dataSnapshot.child(edtUsername.getText().toString()).getValue(User.class);
                            System.out.println(user.getName()+user.getPassword());
                            if (user.getPassword().equals(edtPassword.getText().toString())) {
                                Toast.makeText(SigninupActivity.this, "Signin successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SigninupActivity.this, "Signin failed!", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(SigninupActivity.this, "User doesn;t exist in Database.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
//                        mDialog.dismiss(); // Dismiss the progress dialog in case of an error
//                        Log.e("SigninupActivity", "Database Error: " + error.getMessage());
//                        Toast.makeText(SigninupActivity.this, "Error accessing database", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}