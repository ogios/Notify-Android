package com.example.notify;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";
    int NOTIFY_PERMISSION = 70;
    int SET_IP = 60;

//    String ip="172.19.71.230";
//    int port =5568;

    Button NotificationPermissionBtn;
    Button SetIP;
    RecyclerView recyclerView;

    boolean is_IP_SET = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Supplies(this);
        System.setProperty("java.net.preferIPv4Stack", "true");

        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        Notification_Adapter notification_adapter = new Notification_Adapter(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(notification_adapter);


        SetIP = findViewById(R.id.setip);
        SetIP.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SETIP.class);
            startActivityForResult(intent, SET_IP);
        });
        NotificationPermissionBtn = findViewById(R.id.notificationPermission);
        NotificationPermissionBtn.setOnClickListener(view -> {
            if (isNotiEnabled()){
                Log.d(TAG, "getNotiPermission: NotiPressiom Granted!!!!!!!!!!!");
                showMSG("Notification Permission Granted");
                setupService();
                getNotiPermission();
            } else {
                startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), 1);
            }
        });
        setupService();
        Log.d(TAG, "onCreate: "+ isNotiEnabled());

        if (Supplies.IP.equals("")){
            SetIP.setText("No IP");
        } else {
            is_IP_SET = true;
            SetIP.setText("Change IP");

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NOTIFY_PERMISSION){
            if (isNotiEnabled()){
                Log.d(TAG, "onActivityResult:  NotiConnected!!!!!!!!!!!!!!");
//                Toast.makeText(this, "通知服务已开启", Toast.LENGTH_SHORT).show();
                setupService();
            } else {
                Log.d(TAG, "onActivityResult:  开启失败!!!!!!!!!!!!!!");
                Toast.makeText(this, "通知服务未开启", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == SET_IP) {
            if (Supplies.IP.equals("")){
                SetIP.setText("Set IP");
            } else {
                is_IP_SET = true;
                SetIP.setText("Change IP");
                socketConnect();
            }
        }
    }

    public void listenerConnected(){
        recyclerView.setBackgroundColor(Color.parseColor("#000000"));
        socketConnect();
    }

    public void socketConnect(){
        if (is_IP_SET && Notification_Adapter.is_Open){
            int count = 0;
            StringBuilder stringBuilder = new StringBuilder();
            while (count < 800) {
                stringBuilder.append("t");
                count++;
            }
            new Thread(new NotificationSend(new Notification_item(0,"测试", stringBuilder.toString(), "", 0, null))).start();
        } else {
            if (!is_IP_SET){
                Toast.makeText(this, "No IP", Toast.LENGTH_SHORT).show();
            }
            if (!Notification_Adapter.is_Open){
                Toast.makeText(this, "No Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showMSG(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    public Bitmap getBitmap(String pkgName, int src){
        Bitmap smallIcon;
//        Context otherPkgContext;
        Drawable drawable;
        try {
//            otherPkgContext = this.createPackageContext("com.tencent.mm", 0);
//            drawable = otherPkgContext.getDrawable(src);
            drawable = getAppIcon(pkgName);
            if (drawable != null){
//                smallIcon = ((BitmapDrawable) drawable).getBitmap();
                smallIcon = getIconBitmap(drawable);
            } else {
                drawable = getResources().getDrawable(R.mipmap.ic_launcher);
                smallIcon = ((BitmapDrawable) drawable).getBitmap();
            }
        } catch (Exception e){
            e.printStackTrace();
            drawable = getResources().getDrawable(R.mipmap.ic_launcher);
            smallIcon = ((BitmapDrawable) drawable).getBitmap();
        }
        return smallIcon;
    }

    @SuppressLint("RestrictedApi")
    public Drawable getAppIcon(String pkgName) {
        try {
            PackageManager pm = getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(pkgName, pm.GET_UNINSTALLED_PACKAGES);
            return info.loadIcon(pm);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

//        .getDrawable(R.mipmap.ic_default, context.getTheme());
    }


    public static Bitmap getIconBitmap(Drawable drawable) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && drawable instanceof AdaptiveIconDrawable) {
                Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                drawable.draw(canvas);
                return bitmap;
            } else {
                return ((BitmapDrawable) drawable).getBitmap();
            }
        } catch (Exception e) {
            return null;
        }
    }


    public void getNotiPermission(){
        startActivityForResult(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"), NOTIFY_PERMISSION);
    }

//    private void toggleNotificationListenerService() {
//        PackageManager pm = getPackageManager();
//        pm.setComponentEnabledSetting(new ComponentName(this, com.xinghui.notificationlistenerservicedemo.NotificationListenerServiceImpl.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
//        pm.setComponentEnabledSetting(new ComponentName(this, com.xinghui.notificationlistenerservicedemo.NotificationListenerServiceImpl.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
//    }

    public void setupService(){
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(getApplicationContext(), NotificationListener.class), PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(getApplicationContext(), NotificationListener.class), PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }

    public boolean isNotiEnabled(){
        Set<String> PackagesNames = NotificationManagerCompat.getEnabledListenerPackages(this);
        return PackagesNames.contains(getPackageName());
    }
}