package com.example.letran.equipmentmanagement.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.letran.equipmentmanagement.R;
import com.example.letran.equipmentmanagement.models.Device;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.MyViewHolder> {
    private List<Device> lstDevices;
    private Context context;

    public DevicesAdapter(List<Device> lstDevice, Context context) {
        this.lstDevices = lstDevice;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_alldevices_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Device device = lstDevices.get(position);
        holder.txtname.setText(device.getName());
        holder.txtdate.setText(device.getCreate_time());
        holder.txtdot.setText(Html.fromHtml("&#8226;"));

        if(!device.getUrl_image().isEmpty()){
            Picasso.with(context).load(device.getUrl_image()).into(holder.imageView);
        }else{
            holder.imageView.setImageResource(R.drawable.notfoundimage);
        }
    }

    @Override
    public int getItemCount() {
        return lstDevices.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtname,txtdate,txtdot;
        private ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            txtname = (TextView)view.findViewById(R.id.name);
            txtdate = (TextView)view.findViewById(R.id.date);
            txtdot = (TextView)view.findViewById(R.id.dot);
            imageView = (ImageView)view.findViewById(R.id.image);
        }
    }
}
