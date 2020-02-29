package com.megafreeapps.free.gps.navigation.speedometer.compass.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.megafreeapps.free.gps.navigation.speedometer.compass.Models.Favourite_Places_Model;
import com.megafreeapps.free.gps.navigation.speedometer.compass.R;

public class Favourite_Places_Adapter extends RecyclerView.Adapter<Favourite_Places_Adapter.ViewHolder>
{
    private Activity context;
    private List<Favourite_Places_Model> list;

    public Favourite_Places_Adapter(Activity context, List<Favourite_Places_Model> list)
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.favourite_places_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Favourite_Places_Model favouritePlacesModel = list.get(position);
        if (favouritePlacesModel.Name.equals(""))
        {
            holder.tvAddress.setText(String.valueOf(favouritePlacesModel.Latitude + "," + favouritePlacesModel.Longitude));
        }
        else
        {
            holder.tvAddress.setText(String.valueOf(favouritePlacesModel.Name + "," + list.get(position).Address));
        }
        holder.tvDateTime.setText(String.valueOf("Date:" + favouritePlacesModel.Date + " Time:" + list.get(position).Time));

    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tvAddress;
        private TextView tvDateTime;

        ViewHolder(View itemView)
        {
            super(itemView);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + list.get(getAdapterPosition()).Name);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    if (mapIntent.resolveActivity(context.getPackageManager()) != null)
                    {
                        context.startActivity(mapIntent);
                    }
                }
            });
        }
    }
}
