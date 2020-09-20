package i.apps.unveil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CrewAdapter extends RecyclerView.Adapter<CrewViewHolder> {

    ArrayList<CrewList> crews;


    public CrewAdapter() {
        crews = new ArrayList<>();
    }

    public void setData(ArrayList<CrewList> crew){
        this.crews = crew;
    }

    @NonNull
    @Override
    public CrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View crewView = inflater.inflate(R.layout.crew_row, parent, false);
        return new CrewViewHolder(crewView);
    }

    @Override
    public void onBindViewHolder(@NonNull CrewViewHolder holder, int position) {

        CrewList crewList = crews.get(position);


        holder.name.setText(crewList.name);
        holder.designation.setText(crewList.designation);
    }




    @Override
    public int getItemCount() {
        return crews.size();
    }
}