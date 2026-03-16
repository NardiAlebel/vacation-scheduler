package com.example.vacation_sceduler.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacation_sceduler.ExcursionDetailActivity;
import com.example.vacation_sceduler.R;
import com.example.vacation_sceduler.entities.Excursion;

import java.util.List;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {

    private List<Excursion> excursions;
    private final Context context;
    private final LayoutInflater inflater;
    private String vacationStartDate;
    private String vacationEndDate;

    public ExcursionAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setVacationDates(String startDate, String endDate) {
        this.vacationStartDate = startDate;
        this.vacationEndDate = endDate;
    }

    public class ExcursionViewHolder extends RecyclerView.ViewHolder {
        private final TextView excursionTitle;
        private final TextView excursionDate;

        public ExcursionViewHolder(@NonNull View itemView) {
            super(itemView);
            excursionTitle = itemView.findViewById(R.id.excursion_title);
            excursionDate = itemView.findViewById(R.id.excursion_date);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Excursion current = excursions.get(position);
                    Intent intent = new Intent(context, ExcursionDetailActivity.class);
                    intent.putExtra("excursionId", current.getId());
                    intent.putExtra("excursionTitle", current.getTitle());
                    intent.putExtra("excursionDate", current.getDate());
                    intent.putExtra("vacationId", current.getVacationId());
                    intent.putExtra("vacationStartDate", vacationStartDate);
                    intent.putExtra("vacationEndDate", vacationEndDate);
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_excursion, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionViewHolder holder, int position) {
        if (excursions != null) {
            Excursion current = excursions.get(position);
            holder.excursionTitle.setText(current.getTitle());
            holder.excursionDate.setText(current.getDate());
        }
    }

    @Override
    public int getItemCount() {
        if (excursions != null) {
            return excursions.size();
        }
        return 0;
    }

    public void setExcursions(List<Excursion> excursions) {
        this.excursions = excursions;
        notifyDataSetChanged();
    }
}
