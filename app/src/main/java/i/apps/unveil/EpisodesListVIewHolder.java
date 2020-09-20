package i.apps.unveil;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EpisodesListVIewHolder extends RecyclerView.ViewHolder {

    TextView season,number,date,name;

    public EpisodesListVIewHolder(@NonNull View itemView) {
        super(itemView);

        season = itemView.findViewById(R.id.season);
        number = itemView.findViewById(R.id.number);
        date = itemView.findViewById(R.id.date);
        name = itemView.findViewById(R.id.name);
    }
}
