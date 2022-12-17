package com.example.shashank.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class activity_chat extends AppCompatActivity {
    ImageButton send;
    ImageButton backButton ;
    TextView name ;
    EditText msg ;
    Socket socket;
    String s ;
    String[]  userList ,messages ;
    RecyclerView mmessagerecyclerview;
    MessagesAdapter messagesAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mmessagerecyclerview=findViewById(R.id.recyclerviewofspecific);
        msg = (EditText) findViewById(R.id.getmessage);
        name = (TextView) findViewById(R.id.Nameofspecificuser);
        Intent intent = getIntent();
        send = (ImageButton) findViewById(R.id.imageviewsendmessage);
        backButton = (ImageButton) findViewById(R.id.backbutton);
        String [] userList = intent.getStringArrayExtra("userList");
        String [] listMsg = intent.getStringArrayExtra("messages");
        System.out.println("chat:"+Arrays.toString(listMsg));
        if ("messages".equals(listMsg[0])) {
            List<String> list = new ArrayList<>(Arrays.asList(listMsg));
            list.remove(0);
            listMsg = list.toArray(new String[0]);
        }
        System.out.println("chat:"+Arrays.toString(listMsg));
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListUserActivity.class);
                intent.putExtra("userList",userList);
                startActivity(intent);
            }
        });

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmessagerecyclerview.setLayoutManager(linearLayoutManager);
        messagesAdapter=new MessagesAdapter(activity_chat.this,listMsg);
        mmessagerecyclerview.setAdapter(messagesAdapter);
        String username = intent.getStringExtra("name");
        name.setText(username);
        Client c = new Client();
        c.start();
    }


   class  Client extends Thread {

        @Override
        public void run() {

            try {
                socket = new Socket("Adresse IP", 1235);
                System.out.println("chat: je suis connecté");

            } catch (IOException e) {
                e.printStackTrace();
            }

            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        if(!("".equals(msg.getText().toString()))) {
                            OutputStream os = socket.getOutputStream();

                            PrintWriter pw = new PrintWriter(os, true);
                            Thread t = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    pw.println("message " + msg.getText().toString());
                                }
                            });
                            t.start();
                        }


                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    };
                    msg.setText("");
                }
            });
        }
    }

}