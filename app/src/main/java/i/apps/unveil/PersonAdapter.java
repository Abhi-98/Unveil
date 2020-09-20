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

public class PersonAdapter extends RecyclerView.Adapter<PersonViewHolder> {



    ArrayList<PersonGetSet> person;
    Context context;



    public PersonAdapter() {
        person = new ArrayList<>();
    }

    public void setData(ArrayList<PersonGetSet> p){
        this.person = p;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View showView = inflater.inflate(R.layout.activity_person, parent, false);
        return new PersonViewHolder(showView);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, final int position) {
        final PersonGetSet personGetSet = person.get(position);


        Picasso.get()
                .load(personGetSet.image)
                .into(holder.image);
        holder.name.setText(personGetSet.name);

    }

    @Override
    public int getItemCount() {
        return person.size();
    }
}

