package com.example.vacation_sceduler.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacation_sceduler.R;
import com.example.vacation_sceduler.VacationDetailActivity;
import com.example.vacation_sceduler.entities.Vacation;

import java.util.List;

public class VacationAdapter extends RecyclerView.Adapter<VacationAdapter.VacationViewHolder> {

    private List<Vacation> vacations;
    private final Context context;
    private final LayoutInflater inflater;

    public VacationAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public class VacationViewHolder extends RecyclerView.ViewHolder {
        private final TextView vacationName;
        private final TextView vacationDates;

        public VacationViewHolder(@NonNull View itemView) {
            super(itemView);
            vacationName = itemView.findViewById(R.id.vacation_name);
            vacationDates = itemView.findViewById(R.id.vacation_dates);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Vacation current = vacations.get(position);
                    Intent intent = new Intent(context, VacationDetailActivity.class);
                    intent.putExtra("vacationId", current.getId());
                    intent.putExtra("vacationName", current.getName());
                    intent.putExtra("vacationHotel", current.getHotel());
                    intent.putExtra("vacationStartDate", current.getStartDate());
                    intent.putExtra("vacationEndDate", current.getEndDate());
                    context.startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public VacationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_vacation, parent, false);
        return new VacationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VacationViewHolder holder, int position) {
        if (vacations != null) {
            Vacation current = vacations.get(position);
            holder.vacationName.setText(current.getName());
            String dates = current.getStartDate() + " - " + current.getEndDate();
            holder.vacationDates.setText(dates);
        }
    }

    @Override
    public int getItemCount() {
        if (vacations != null) {
            return vacations.size();
        }
        return 0;
    }

    public void setVacations(List<Vacation> vacations) {
        this.vacations = vacations;
        notifyDataSetChanged();
    }
}
