package com.example.notify;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class NotificationSend implements Runnable{
    Socket client;
    Notification_item item;

    NotificationSend(Notification_item item){
        this.item = item;
    }


    @Override
    public void run() {

        try {
            byte[] data = Supplies.compress(item);
            // Connect to ServerSocket
            System.out.println("Connecting...");
            this.client = new Socket(Supplies.IP, Supplies.PORT);
            System.out.println(this.client.getInetAddress().getHostAddress());
            try {
//                client.connect(new InetSocketAddress(Supplies.IP, Supplies.PORT), 3000);

                DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(client.getOutputStream()));
                dataOutputStream.write(data);
                dataOutputStream.flush();

            } catch (ConnectException e) {
                System.out.println(e.getMessage());
            } catch (Exception e){
                e.printStackTrace();
            }
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
