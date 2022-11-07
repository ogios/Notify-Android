package com.example.notify;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.net.InetSocketAddress;

public class SETIP extends AppCompatActivity {

    EditText IP;
    EditText PORT;
    Button commit;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setip);

        IP = findViewById(R.id.ip);
        PORT = findViewById(R.id.port);
        commit = findViewById(R.id.commit);

        sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        commit.setOnClickListener(view -> {
            try {
                String ip = String.valueOf(IP.getText());
                int port = Integer.parseInt(String.valueOf(PORT.getText()));
                checkIP(ip, port);
                editor.putString("ip", ip);
                editor.putInt("port", port);
                editor.commit();
                Supplies.init();
                finish();
            } catch (Exception e){
                e.printStackTrace();
                Toast.makeText(SETIP.this, "Wrong Input", Toast.LENGTH_SHORT).show();
            }

        });
    }

    public void checkIP(String ip, int port) throws Exception {
        InetSocketAddress inetSocketAddress = new InetSocketAddress(ip, port);
        if (inetSocketAddress.getAddress() == null || inetSocketAddress.getPort() <0){
            throw new Exception();
        }
//        if (ip.contains(":")){
//            return true;
//        }
//        String[] ips = ip.split("\\.");
//        if (ips.length == 4){
//            for (String i:ips){
//                try {
//                    int tmp = Integer.parseInt(i);
//                    if (tmp >255 || tmp <0){
//                        return false;
//                    }
//                } catch (Exception e){
//                    e.printStackTrace();
//                    return false;
//                }
//            }
//            return true;
//        }
    }
}