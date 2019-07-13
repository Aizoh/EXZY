package com.example.exigent;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exigent.Model.EmergencyGuide;

import java.util.ArrayList;
import java.util.List;

public class EmergencyGuideAdapter extends RecyclerView.Adapter<EmergencyGuideAdapter.MyViewHolder>implements Filterable {
    private Context context;
    private List<EmergencyGuide> emergencyGuideList;
    private List<EmergencyGuide> emergencyGuidesListFiltered;
    private EmergencyGuideAdapterListener listener;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView title, description;
        public ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image_terror);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onEmergencyGuideSelected(emergencyGuidesListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
    public EmergencyGuideAdapter(Context context,List<EmergencyGuide> emergencyGuideList,EmergencyGuideAdapterListener listener){
        this.context = context;
        this.listener = listener;
        this.emergencyGuideList = emergencyGuideList;
        this.emergencyGuidesListFiltered = emergencyGuideList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.emergency_guide_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final  EmergencyGuide emergencyGuide = emergencyGuidesListFiltered.get(position);
        holder.title.setText(emergencyGuide.getTitle());
        holder.description.setText(emergencyGuide.getDescription());
        holder.image.setImageResource(emergencyGuide.getImage());

    }

    @Override
    public int getItemCount() {
        return emergencyGuidesListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if(charString.isEmpty()){
                    emergencyGuidesListFiltered = emergencyGuideList;
                }else{
                    List<EmergencyGuide> filteredList = new ArrayList<>();
                    for(EmergencyGuide row : emergencyGuideList){
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getDescription().contains(constraint)) {
                            filteredList.add(row);
                        }
                    }
                    emergencyGuidesListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = emergencyGuidesListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                emergencyGuidesListFiltered = (ArrayList<EmergencyGuide>)filterResults.values;
                notifyDataSetChanged();

            }
        };
    }

    public interface EmergencyGuideAdapterListener{
        void onEmergencyGuideSelected(EmergencyGuide emergencyGuide);
    }

}
