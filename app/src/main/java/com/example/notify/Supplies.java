package com.example.notify;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.github.luben.zstd.Zstd;

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


    public static byte[] compress(Notification_item item) throws IOException {


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 标题
        baos.write(item.getTitle().getBytes(StandardCharsets.UTF_8).length);
        baos.write("\n".getBytes());
        baos.write(item.getTitle().getBytes(StandardCharsets.UTF_8));
        baos.write("\n\n".getBytes());
        // 内容
        baos.write(item.getContent().getBytes(StandardCharsets.UTF_8).length);
        baos.write("\n".getBytes());
        baos.write(item.getContent().getBytes(StandardCharsets.UTF_8));
        baos.write("\n\n".getBytes());
        // 图片
        Bitmap bitmap = item.getBitmap();
        try {
            ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, imageBytes);
            baos.write("true\n".getBytes(StandardCharsets.UTF_8));
            baos.write(imageBytes.toByteArray());
        } catch (Exception e){
            baos.write("false".getBytes(StandardCharsets.UTF_8));
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
