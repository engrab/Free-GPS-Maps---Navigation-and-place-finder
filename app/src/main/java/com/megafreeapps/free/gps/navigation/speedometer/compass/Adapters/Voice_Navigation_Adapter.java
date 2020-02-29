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

import com.megafreeapps.free.gps.navigation.speedometer.compass.R;

public class Voice_Navigation_Adapter extends RecyclerView.Adapter<Voice_Navigation_Adapter.ViewHolder>
{
    private Activity context;
    private List<String> list;

    public Voice_Navigation_Adapter(Activity context, List<String> list)
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.voice_navigation_row, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        holder.text.setText(list.get(position));
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView text;

        ViewHolder(View itemView)
        {
            super(itemView);
            text = itemView.findViewById(R.id.texticon_new);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + list.get(getAdapterPosition())));
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
