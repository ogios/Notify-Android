package com.example.notify;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class Supplies {


    static SharedPreferences sharedPreferences;

    public static String IP = "";
    public static int PORT = 5568;

    Supplies(MainActivity mainActivity){
//        'white': '\033[30m',
//        'red': '\033[31m',
//        'green': '\033[32m',
//        'yellow': '\033[33m',
//        'blue': '\033[34m',
//        'purple': '\033[35m',
//        'cyan': '\033[36m',
//        'black': '\033[37m'

        sharedPreferences = mainActivity.getSharedPreferences("config", Context.MODE_PRIVATE);
        init();
    }

    public static void init(){
        System.out.println(sharedPreferences.getString("ip",""));
        System.out.println(IP);
        IP = sharedPreferences.getString("ip","");
        PORT = Integer.parseInt(String.valueOf(sharedPreferences.getString("port","5568")));
    }


    public static long[] bytesFromLong(long length) {
        long[] length_bytes = {0,0,0,length};
        int step = length_bytes.length - 1;
        while (length_bytes[step] > 255 && step > 1) {
            length_bytes[step - 1] = length_bytes[step] / 255;
            length_bytes[step] = length_bytes[step] % 255;
            step--;
        }
        return length_bytes;
    }


    public static byte[] compress(Notification_item item) throws IOException {


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 标题
        long title_length = item.getTitle().getBytes(StandardCharsets.UTF_8).length;
        for (long c: bytesFromLong(title_length)) baos.write((int) c);
        baos.write("\n".getBytes());
        baos.write(item.getTitle().getBytes(StandardCharsets.UTF_8));
        baos.write("\n\n".getBytes());
        // 内容
        long content_length = item.getContent().getBytes(StandardCharsets.UTF_8).length;
        for (long c: bytesFromLong(content_length)) baos.write((int) c);
        baos.write("\n".getBytes());
        baos.write(item.getContent().getBytes(StandardCharsets.UTF_8));
        baos.write("\n\n".getBytes());
        // 图片
        Bitmap bitmap = item.getBitmap();
        try {
            ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, imageBytes);
            byte[] img = imageBytes.toByteArray();
            for (long c: bytesFromLong(img.length)) baos.write((int) c);
            baos.write(img);
        } catch (Exception e){
            for (int c: new int[]{0, 0, 0, 0}) baos.write(c);
        }

        // 使用zstanard压缩请求体
//        return Zstd.compress(baos.toByteArray());
        return baos.toByteArray();
    }

    public static void showDialog(Notification_item item, Context context) {
        AlertDialog alertDialog1 = new AlertDialog.Builder(context)
                .setTitle(item.getTitle())
                .setMessage(item.getApp())
                .setIcon(R.mipmap.ic_launcher)
                .create();
        alertDialog1.show();
    }






//    static final public String QQ = "com.tencent.mobileqq";
//    static final public String WECHAT = "com.tencent.mm";
//
//    public static int getSrc(String packagename){
//        switch (packagename){
//            case QQ:
//                return R.drawable.qq_logo;
//            case WECHAT:
//                return R.drawable.wechat_logo;
//            default:
//                return R.mipmap.ic_launcher;
//        }
//    }
}
