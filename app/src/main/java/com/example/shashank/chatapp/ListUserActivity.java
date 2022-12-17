package com.example.shashank.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ListUserActivity extends AppCompatActivity {
    ListView listView ;
    Socket socket;
    String[] s,ss,message;
    String[] finalUserList;
    CustomBaseAdapter customBaseAdapter;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);
        intent = getIntent();

        String [] userList = intent.getStringArrayExtra("userList");
        String[] messagess = intent.getStringArrayExtra("msg");
        message = messagess;
        System.out.println("listuser:msg"+Arrays.toString(message));
        System.out.println("listuser:userlist"+Arrays.toString(userList));
        if ("userList".equals(userList[0])) {
            List<String> list = new ArrayList<>(Arrays.asList(userList));
            list.remove(0);
            userList = list.toArray(new String[0]);
        }
        listView = (ListView) findViewById(R.id.userListView);
        customBaseAdapter = new CustomBaseAdapter(getApplicationContext(),userList);
        listView.setAdapter(customBaseAdapter);
        finalUserList = userList;
        Client c = new Client();
        c.start();
    }

    class  Client extends Thread {

        @Override
        public void run() {

            try {
                socket = new Socket("Adresse IP", 1235);
                System.out.println("listUser: je suis connecté");

            } catch (IOException e) {
                e.printStackTrace();
            }

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Object o =  customBaseAdapter.getItem(i);
                    String str=(String)o;//As you are using Default String Adapter
                    System.out.println(str.toString());
                    Intent intent = new Intent(getApplicationContext(), activity_chat.class);
                    intent.putExtra("name",str.toString());
                    intent.putExtra("userList", finalUserList);
                    intent.putExtra("messages", message);
                    startActivity(intent);
                }

            });
        }

    }
}





