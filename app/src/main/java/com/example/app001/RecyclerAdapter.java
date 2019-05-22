package com.example.app001;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ImageViewHolder> {

    // a constructor which will accept an array of images
    private int[] images;

    public RecyclerAdapter(int[] images){
        this.images= images;

    }

    @Override
    public ImageViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        //responsible for creating every object view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_terror,parent,false);

        // from the constructor of the Image view holder static class
        ImageViewHolder imageViewHolder = new ImageViewHolder(view);

        return imageViewHolder;
    }

    @Override
    public void onBindViewHolder( ImageViewHolder viewHolder, int position) {
        // ensures the appropriate image and text are positioned in place
        int image_id = images[position];
        viewHolder.Terror.setImageResource(image_id);
        viewHolder.Terror_nm.setText("image"+ position);
    }

    @Override
    public int getItemCount() {
        //return length of the array as its the number of our objects
        return images.length;
    }
    //all items in recycler view are objects of view holder a class to create this objects
    public static class ImageViewHolder extends RecyclerView.ViewHolder{

        // each object in the view has an image and a text ,create the two holders
        ImageView Terror;
        TextView Terror_nm;

        public ImageViewHolder( View itemView) {
            super(itemView);
            Terror = itemView.findViewById(R.id.terror);
            Terror_nm = itemView.findViewById(R.id.terror_nm);
        }
    }

}
