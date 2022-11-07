package com.example.notify;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.nViewHolder> {
    private MainActivity mainActivity;
    private static Notification_Adapter adapter;

    public static boolean is_Open = false;

    private static List<Notification_item> notificationItems = new ArrayList<>();

    Notification_Adapter(MainActivity mainActivity){
        adapter = this;
        this.mainActivity = mainActivity;
    }

    public static Notification_Adapter getInstance(){
        return adapter;
    }

    public void setMainActivity(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    public void onPost(Notification_item item){
        item.setBitmap(mainActivity.getBitmap(item.getApp(), item.getSrc() ));
        notificationItems.add(item);
        notifyItemInserted(notificationItems.size());
        if (!Supplies.IP.equals("")){
            new Thread(() -> NotificationSend.send(item)).start();
        } else {
            Toast.makeText(mainActivity, "No Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void onConnected(){
        is_Open = true;
        mainActivity.listenerConnected();
    }

    public List<Notification_item> getNotifications(){
        return notificationItems;
    }


    @NonNull
    @Override
    public nViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notify_item, parent, false);
        return new nViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull nViewHolder holder, int position) {
//        holder.imageView.setImageResource(notificationItems.get(position).getSrc());
        holder.imageView.setImageBitmap(notificationItems.get(position).getBitmap());
        holder.title.setText(notificationItems.get(position).getTitle());
        holder.content.setText(notificationItems.get(position).getContent());

        holder.itemView.setOnLongClickListener(view -> {
            new Thread(() -> NotificationSend.send(notificationItems.get(holder.getAdapterPosition()))).start();
            return false;
        });


    }

    @Override
    public int getItemCount() {
        return notificationItems.size();
    }


    public class nViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView title;
        TextView content;

        public nViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.item_pic);
            this.title = itemView.findViewById(R.id.item_title);
            this.content = itemView.findViewById(R.id.item_content);
        }
    }
}
