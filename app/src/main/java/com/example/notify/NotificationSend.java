package com.example.notify;

import java.net.ConnectException;
import java.net.Socket;

import sutils.out.SocketOut;

public class NotificationSend implements Runnable{
    Socket client;
    Notification_item item;

    NotificationSend(Notification_item item){
        this.item = item;
    }


    @Override
    public void run() {

        try {
            SocketOut so = Supplies.setOutput(item);
            // Connect to ServerSocket
            System.out.println("Connecting...");
            this.client = new Socket(Supplies.IP, Supplies.PORT);
            System.out.println(this.client.getInetAddress().getHostAddress());
            try {
//                client.connect(new InetSocketAddress(Supplies.IP, Supplies.PORT), 3000);
                so.writeTo(client.getOutputStream());
            } catch (ConnectException e) {
                System.out.println(e.getMessage());
            } catch (Exception e){
                e.printStackTrace();
            }
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
