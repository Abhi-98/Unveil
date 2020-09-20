package i.apps.unveil;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleViewHolder> {

    ArrayList<PeopleGetSet> peoples;
    Context context;


    public PeopleAdapter() {
        peoples = new ArrayList<>();
    }

    public void setData(ArrayList<PeopleGetSet> people) {
        this.peoples = people;
    }

    @NonNull
    @Override
    public PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View showView = inflater.inflate(R.layout.people_row, parent, false);
        return new PeopleViewHolder(showView);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleViewHolder holder, int position) {

        final PeopleGetSet peopleGetSet = peoples.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Person.class);
                intent.putExtra("n", peopleGetSet.name);
                context.startActivity(intent);
            }
        });
        Picasso.get()
                .load(peopleGetSet.image)
                .into(holder.image);
        holder.name.setText(peopleGetSet.name);

    }

    @Override
    public int getItemCount() {
        return peoples.size();
    }
}
