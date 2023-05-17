package com.example.app1.BusinessActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.app1.R;

public class MainBusiness extends AppCompatActivity {
    CardView tableView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainbusiness);

        tableView = (CardView) findViewById(R.id.TableView);
    }

    protected void onStart () {
        super.onStart();

        //Pairnoume onoma xrhth kai topic
        tableView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent s = new Intent(view.getContext(), TableViewActivity.class);


                startActivity(s);


            }
        });
    }
}