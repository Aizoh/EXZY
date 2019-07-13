package com.example.exigent;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exigent.Model.PanicEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ImageViewHolder> {
    //
    Button BtnLoc ;
    // a constructor which will accept an array of images
    List<PanicEvent> panicEventList;

    public RecyclerAdapter(List<PanicEvent> panicEventList) {
        this.panicEventList = panicEventList;
    }

    @Override
    public ImageViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        //responsible for creating every object view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_terror,parent,false);
        // from the constructor of the Image view holder static class

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder( ImageViewHolder viewHolder, int position) {
        // ensures the appropriate image and text are positioned in place
        final PanicEvent event = panicEventList.get(position);
        viewHolder.Terror.setImageResource(event.getImage());
        viewHolder.Terror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Post event to all activity using Eventbus.
                EventBus.getDefault().post(event);
            }
        });
        //The whole card is clickable
        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Post event to all activity using Eventbus.
                EventBus.getDefault().post(event);
            }
        });
        viewHolder.Terror_nm.setText(event.getCaption());
    }

    @Override
    public int getItemCount() {
        //return length of the array as its the number of our objects
        return panicEventList.size();
    }
    //all items in recycler view are objects of view holder a class to create this objects
    public static class ImageViewHolder extends RecyclerView.ViewHolder{


        // each object in the view has an image and a text ,create the two holders
        ImageView Terror;
        TextView Terror_nm;
        CardView cardView;
        public ImageViewHolder( View itemView) {
            super(itemView);
            Terror = itemView.findViewById(R.id.terror);
            Terror_nm = itemView.findViewById(R.id.terror_nm);
            cardView = itemView.findViewById(R.id.cardevent);
        }
    }



}
