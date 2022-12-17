package com.example.shashank.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {
    Button saveProfile ;
    EditText userName ;
    Socket socket;
    String[] s,userList,messages;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userName = (EditText) findViewById(R.id.getusername);
        saveProfile = (Button) findViewById(R.id.saveProfile);
        Client c = new Client();
        c.start();
    };


    class  Client extends Thread {

        @Override
        public void run() {

            try {
                socket = new Socket("Adresse IP", 1235);
                System.out.println("main: je suis connecté");

            } catch (IOException e) {
                e.printStackTrace();
            }
            saveProfile.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    String user = userName.getText().toString();
                    try {

                            OutputStream os = socket.getOutputStream();

                            PrintWriter pw = new PrintWriter(os, true);
                            Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    pw.println("username "+user);
                                }
                            });
                            t.start();


                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        };

                    Thread t =new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                InputStream    is = socket.getInputStream();
                                InputStreamReader isr= new InputStreamReader(is);
                                BufferedReader br = new BufferedReader(isr);
                                 s= br.readLine().split("%");
                                 userList = s;
                                System.out.println("main:" + Arrays.toString(s));
                                 s = br.readLine().split("%");
                                 messages = s;
                                System.out.println("main:" + Arrays.toString(s));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                   t.start();

                    System.out.println("main:" + Arrays.toString(messages));
                    Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), ListUserActivity.class);
                    intent.putExtra("name",userName.getText().toString());
                    intent.putExtra("msg",messages);
                    intent.putExtra("userList",userList);
                    startActivity(intent);
                    }
            });
        }
    }
    }








