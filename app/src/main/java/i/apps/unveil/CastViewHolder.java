package i.apps.unveil;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CastViewHolder extends RecyclerView.ViewHolder {


    TextView name,character;
    ImageView image;

    public CastViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        character = itemView.findViewById(R.id.character);
        image = itemView.findViewById(R.id.image);
    }
}
