package com.example.letran.equipmentmanagement.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.letran.equipmentmanagement.R;
import com.example.letran.equipmentmanagement.models.Device;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_viewcard_fragment_row,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Device device = lstDevices.get(position);
        holder.txtname.setText(device.getName());
        holder.txtdate.setText(device.getCreate_time());
        if(!lstDevices.get(position).getApprover().isEmpty())
            holder.imageDot.setImageResource(R.drawable.approved);
        else
            holder.imageDot.setImageResource(R.drawable.dot);


        if(!device.getUrl_image().isEmpty()){
            //Picasso.with(context).load(device.getUrl_image()).into(holder.imageView);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] imageBytes = baos.toByteArray();
            //decode base64 string to image
            imageBytes = Base64.decode(lstDevices.get(position).getUrl_image(), Base64.DEFAULT);
            //BitmapFactory.Options options = new BitmapFactory.Options();
            //options.inSampleSize = calculateInSampleSize(options, 400,300);
            //options.inJustDecodeBounds = false;
            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            Bitmap bMap = Bitmap.createScaledBitmap(decodedImage, 400, 300, true);
            holder.imageView.setImageBitmap(bMap);
        }else{
            holder.imageView.setImageResource(R.drawable.notfoundimage);
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    @Override
    public int getItemCount() {
        return lstDevices.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtname,txtdate;
        private ImageView imageView,imageDot;

        public MyViewHolder(View view) {
            super(view);
            txtname = (TextView)view.findViewById(R.id.name);
            txtdate = (TextView)view.findViewById(R.id.date);
            imageDot = (ImageView)view.findViewById(R.id.dot);
            imageView = (ImageView)view.findViewById(R.id.image);
        }

    }

    public void filterList(ArrayList<Device> filterdNames) {
        this.lstDevices = filterdNames;
        notifyDataSetChanged();
    }
}
