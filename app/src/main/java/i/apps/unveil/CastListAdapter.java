package i.apps.unveil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CastListAdapter extends RecyclerView.Adapter<CastViewHolder> {

    ArrayList<CastList> casts;


    public CastListAdapter() {
        casts = new ArrayList<>();
    }

    public void setData(ArrayList<CastList> cast){
        this.casts = cast;
    }

    @NonNull
    @Override
    public CastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View castView = inflater.inflate(R.layout.cast_row, parent, false);
        return new CastViewHolder(castView);
    }

    @Override
    public void onBindViewHolder(@NonNull CastViewHolder holder, int position) {

        CastList castList = casts.get(position);

        Picasso.get()
                .load(castList.image)
                .into(holder.image);
        holder.name.setText(castList.name);
        holder.character.setText(castList.character);
    }



    @Override
    public int getItemCount() {
        return casts.size();
    }
}