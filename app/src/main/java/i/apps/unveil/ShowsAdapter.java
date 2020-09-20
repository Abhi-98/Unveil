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


public class ShowsAdapter extends RecyclerView.Adapter<ShowsViewHolder> {



        ArrayList<Shows> shows;
        Context context;



        public ShowsAdapter() {
        shows = new ArrayList<>();
        }

public void setData(ArrayList<Shows> shows){
        this.shows = shows;
        }

@NonNull
@Override
public ShowsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View showView = inflater.inflate(R.layout.recycler_row, parent, false);
        return new ShowsViewHolder(showView);
        }

@Override
public void onBindViewHolder(@NonNull ShowsViewHolder holder, final int position) {
        final Shows show = shows.get(position);


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

