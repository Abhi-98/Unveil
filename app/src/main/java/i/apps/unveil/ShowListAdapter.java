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

public class ShowListAdapter extends RecyclerView.Adapter<ShowListViewHolder> {

        ArrayList<ShowListGetSet> shows;
        Context context;


public ShowListAdapter() {
        shows = new ArrayList<>();
        }

public void setData(ArrayList<ShowListGetSet> shows){
        this.shows = shows;
        }

@NonNull
@Override
public ShowListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View showView = inflater.inflate(R.layout.showlist_row, parent, false);
        return new ShowListViewHolder(showView);
        }

@Override
public void onBindViewHolder(@NonNull ShowListViewHolder holder, int position) {
        final ShowListGetSet show = shows.get(position);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Intent intent = new Intent(context, Episode.class);
                        intent.putExtra("name", show.title);
                        context.startActivity(intent);
                }
        });
        Picasso.get()
        .load(show.image)
        .into(holder.image);
        holder.title.setText(show.title);
        holder.body.setText(show.body);
        holder.ratingNum.setText(show.rating);
        }

@Override
public int getItemCount() {
        return shows.size();
        }
        }