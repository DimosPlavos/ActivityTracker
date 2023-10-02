package com.example.app1.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app1.Adaptor.CategoryAdaptor;
import com.example.app1.Domain.CategoryDomain;
import com.example.app1.R;
import com.example.app1.Statistics.TotalClimb;
import com.example.app1.Statistics.TotalDistance;
import com.example.app1.Statistics.TotalTime;

import java.io.IOException;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.net.Uri;
import android.provider.OpenableColumns;

public class MainActivity extends AppCompatActivity implements SelectListener, ResultCallback {
    public static final String IP_ADDRESS = "192.146.1.8";
    private static final int PICK_FILE_REQUEST_CODE = 1;
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewCategoryList;
    private RecyclerView recyclerViewPopularList;
    String fileName;
    boolean ResultsReceived=false, availableForUpload = false;
    TextView GpxFileName;
    Button GpxUpload, check_gpx_results_btn;
    Intent intent1,intent2,intent3,intent4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        intent1 = new Intent(MainActivity.this, GpxResults.class);
        intent2 = new Intent(MainActivity.this, TotalTime.class);
        intent3 = new Intent(MainActivity.this, TotalDistance.class);
        intent4 = new Intent(MainActivity.this, TotalClimb.class);

        check_gpx_results_btn = findViewById(R.id.check_gpx_results_btn);
        check_gpx_results_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ResultsReceived == true){
                    startActivity(intent1);
            }else{
                    Toast.makeText(MainActivity.this, "Your Gpx file isn't uploaded/processed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerviewCategory();
        GpxFileName = findViewById(R.id.enterpathname);

        //OTAN PATAW TO UPLOAD THA TREXEI O SERVER
        GpxUpload = findViewById(R.id.upload);
        GpxUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (availableForUpload) {
                    AsyncTask<String, Void, String> myAsync
                            = new AsyncTask<String, Void, String>() {
                        @Override
                        protected String doInBackground(String... strings) {
                            Socket s;
                            try {
                                s = new Socket(IP_ADDRESS, 1234);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            Client client = new Client(s, fileName);

                            client.listenForMessage(MainActivity.this);
                            client.sendMessage();
                            return " ";
                        }
                        @Override
                    protected void onPostExecute(String s) {
                            Toast.makeText(MainActivity.this, "Process of " +fileName +" done successfully",
                                    Toast.LENGTH_SHORT).show();
                    }
                    };
                    myAsync.execute();
                }else{
                    Toast.makeText(MainActivity.this, "You have to choose a Gpx file for processing.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        // Launch file picker when a button is clicked
        findViewById(R.id.btn_pick_file).setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");  // Set the file type(s) you want to allow the user to pick
            startActivityForResult(intent, PICK_FILE_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();

                // Handle the selected file URI
                processSelectedFile(uri);
            }
        }
    }

    private void processSelectedFile(Uri fileUri) {
        // Use the file URI to perform further processing or display the file details
        fileName = getFileName(fileUri);
        Toast.makeText(this, "Selected file: " + fileName, Toast.LENGTH_SHORT).show();
        GpxFileName.setText(fileName); // Update the text of GpxFileName
    }

    @SuppressLint("Range")
    private String getFileName(Uri fileUri) {
        String displayName = null;
        if (fileUri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(fileUri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
        } else if (fileUri.getScheme().equals("file")) {
            displayName = fileUri.getLastPathSegment();
        }
        availableForUpload = true;
        return displayName;
    }

    private void recyclerviewCategory(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false);
        recyclerViewCategoryList = findViewById(R.id.recyclerView);
        recyclerViewCategoryList.setLayoutManager(linearLayoutManager);

        ArrayList<CategoryDomain> category = new ArrayList<>();
        category.add(new CategoryDomain("Time Stats", "timestats"));
        category.add(new CategoryDomain("Distance Stats", "distancestats"));
        category.add(new CategoryDomain("Climb Stats", "climbstats"));

        adapter = new CategoryAdaptor(category,this);
        recyclerViewCategoryList.setAdapter(adapter);
    }

    @Override
    public void onItemClicked(CategoryDomain myModel) {
        if (ResultsReceived) {
            if (myModel.getTitle().equals("Time Stats")) {
                startActivity(intent2);
            } else if (myModel.getTitle().equals("Distance Stats")) {
                startActivity(intent3);
            }
            if (myModel.getTitle().equals("Climb Stats")) {
                startActivity(intent4);
            }
        }else{
            Toast.makeText(this, "You have to upload a gpx file first", Toast.LENGTH_SHORT).show();
        }
    }


    public void onResultsReceived(ArrayList<Double> results) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ResultsReceived = true;
                String newText = "CHECK RESULTS! ✔️";
                check_gpx_results_btn.setText(newText); // Update the text of check_gpx_results_btn
                DecimalFormat decimalFormat = new DecimalFormat("#.#####"); // Format with at most 5 decimals
                intent1.putExtra("distance_arg", String.valueOf(decimalFormat.format(results.get(1))));
                intent1.putExtra("speed_arg", String.valueOf(decimalFormat.format(results.get(3))));
                intent1.putExtra("time_arg", String.valueOf(decimalFormat.format(results.get(2))));
                intent1.putExtra("climb_arg", String.valueOf(decimalFormat.format(results.get(0))));

                intent4.putExtra("mesos_oros_climb_arg", String.valueOf(decimalFormat.format(results.get(4)/results.get(7))));
                intent4.putExtra("climb_arg", String.valueOf(decimalFormat.format(results.get(0))));

                intent3.putExtra("mesos_oros_distance_arg", String.valueOf(decimalFormat.format(results.get(5)/results.get(7))));
                intent3.putExtra("distance_arg", String.valueOf(decimalFormat.format(results.get(1))));

                intent2.putExtra("mesos_oros_time_arg", String.valueOf(decimalFormat.format(results.get(6)/results.get(7))));
                intent2.putExtra("time_arg", String.valueOf(decimalFormat.format(results.get(2))));
                availableForUpload = false;
                GpxFileName.setText("Select a file...");
            }
        });
    }
}