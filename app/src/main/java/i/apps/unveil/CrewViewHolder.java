package i.apps.unveil;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CrewViewHolder extends RecyclerView.ViewHolder {

    TextView name,designation;

    public CrewViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.name);
        designation = itemView.findViewById(R.id.designation);
    }
}
