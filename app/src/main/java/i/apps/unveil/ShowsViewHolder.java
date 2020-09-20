package i.apps.unveil;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ShowsViewHolder extends RecyclerView.ViewHolder {


    ImageView image;
    TextView body;
    TextView title;
    TextView ratingNum;

    public ShowsViewHolder(@NonNull View itemView) {
        super(itemView);

        image = itemView.findViewById(R.id.image);
        title = itemView.findViewById(R.id.title);
        body = itemView.findViewById(R.id.body);
        ratingNum = itemView.findViewById(R.id.ratingNum);
    }
}
