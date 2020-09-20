package i.apps.unveil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class EpisodesListAdapter extends RecyclerView.Adapter<EpisodesListVIewHolder> {

    ArrayList<EpisodesList> episodes;


    public EpisodesListAdapter() {
        episodes = new ArrayList<>();
    }

    public void setData(ArrayList<EpisodesList> episodesLists) {
        this.episodes = episodesLists;
    }

    @NonNull
    @Override
    public EpisodesListVIewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View episodeView = inflater.inflate(R.layout.episodes_row, parent, false);
        return new EpisodesListVIewHolder(episodeView);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodesListVIewHolder holder, int position) {

        EpisodesList episodesList = episodes.get(position);

        holder.season.setText(episodesList.season);
        holder.number.setText(episodesList.number);
        holder.date.setText(episodesList.date);
        holder.name.setText(episodesList.name);
    }


    @Override
    public int getItemCount() {
        return episodes.size();
    }
}
