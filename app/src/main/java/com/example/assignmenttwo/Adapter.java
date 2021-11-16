package com.example.assignmenttwo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.ContentInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignmenttwo.activities.MainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Adapter extends RecyclerView.Adapter<Adapter.Holder> implements Filterable {

    private Context context;
    ArrayList<Model> arrayList, filterList;
    CustomFilter filter;
    DataBaseHelper dataBaseHelper;

    public Adapter(Context context, ArrayList<Model> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.filterList = arrayList;
        dataBaseHelper = new DataBaseHelper(context);
    }

    public void updateList(ArrayList<Model> newList){
        arrayList = new ArrayList<>();
        arrayList.addAll(newList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        Model model = arrayList.get(position);

        String address = model.getAddress();
        String longitude = model.getLongitude();
        String latitude = model.getLatitude();
        String id = model.getId();


        holder.address.setText(""+address);
        holder.lon.setText("Longitude: " + longitude);
        holder.lat.setText("Latitude: " + latitude);


        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDialog(
                        ""+id,
                        ""+latitude,
                        ""+longitude
                );
            }
        });

        //when long press on item, show an alert dialog for delete an item
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                deleteDialog(""+id);
                return false;
            }
        });
    }

    private void deleteDialog(final String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete");
        builder.setMessage("Delete?");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_baseline_delete_24);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dataBaseHelper.deleteLocation(id);
                ((MainActivity)context).onResume();
                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
    private void editDialog(String id, String latitude, String longitude) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Update");
        builder.setMessage("Do you want to update?");
        builder.setCancelable(false);
        builder.setIcon(R.drawable.ic_baseline_edit_location_24);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                double lat = Double.parseDouble(latitude);
                double lon = Double.parseDouble(longitude);
                Geocoder geocoder;
                List<Address> addresses = null;
                geocoder = new Geocoder(context, Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(lat, lon, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert addresses != null;
                String address = addresses.get(0).getAddressLine(0);

                Intent intent = new Intent(context, EditLocation.class);
                intent.putExtra("ID", id);
                intent.putExtra("address", address);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("editMode", true);
                context.startActivity(intent);
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public Filter getFilter() {

        if(filter == null){
            filter = new CustomFilter(filterList, this);
        }

        return filter;
    }

    class Holder extends RecyclerView.ViewHolder{

        ImageView locationImage;
        TextView address, lat, lon;
        ImageButton editButton;

        public Holder(@NonNull View itemView){
            super(itemView);

            locationImage = itemView.findViewById(R.id.locIm);
            address = itemView.findViewById(R.id.address);
            lat = itemView.findViewById(R.id.latitude);
            lon = itemView.findViewById(R.id.longitude);
            editButton = itemView.findViewById(R.id.editButton);
        }
    }

}
