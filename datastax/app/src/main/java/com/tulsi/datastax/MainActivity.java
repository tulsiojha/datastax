package com.tulsi.datastax;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button connect = findViewById(R.id.connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });

    }


    //AsyncTask is deprecated so using Thread

    public void test(){
        new Thread(()->{
            AssetManager assetManager = getAssets();

            try (CqlSession session = CqlSession.builder()
                    .withCloudSecureConnectBundle(assetManager.open("secure-connect-zine.zip"))
                    .withAuthCredentials("bikash","ojhabikash")
                    .withKeyspace("data")
                    .build()) {
                // Select the release_version from the system.local table:
                ResultSet rs = session.execute("select release_version from system.local");
                Row row = rs.one();
                //Print the results of the CQL query to the console:
                if (row != null) {
                    System.out.println(row.getString("release_version"));
                } else {
                    System.out.println("An error occurred.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
    }
}