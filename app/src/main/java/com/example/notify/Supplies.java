package com.example.notify;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

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
        PORT = sharedPreferences.getInt("port",5568);
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
