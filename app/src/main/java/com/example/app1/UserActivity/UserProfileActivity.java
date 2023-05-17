package com.example.app1.UserActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.app1.R;

public class UserProfileActivity extends AppCompatActivity {

    String prdetails[] = {"User info","My past reservations", "Favorites" ,"Customer Service","About our App"};
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile2);

        listview = (ListView) findViewById(R.id.profiledetails);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_view,R.id.textdetails ,prdetails);
        listview.setAdapter(arrayAdapter);
    }
}