package com.example.vacation_sceduler;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vacation_sceduler.adapters.ExcursionAdapter;
import com.example.vacation_sceduler.database.VacationRepository;
import com.example.vacation_sceduler.entities.Excursion;
import com.example.vacation_sceduler.entities.Vacation;
import com.example.vacation_sceduler.ui.BaseDetailActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class VacationDetailActivity extends BaseDetailActivity {

    private TextInputEditText editName;
    private TextInputEditText editHotel;
    private TextInputEditText editStartDate;
    private TextInputEditText editEndDate;

    private VacationRepository repository;
    private ExcursionAdapter excursionAdapter;

    private int vacationId = -1;
    private boolean isNewVacation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setTitle(R.string.vacation_detail);

        repository = new VacationRepository(getApplication());

        editName = findViewById(R.id.edit_vacation_name);
        editHotel = findViewById(R.id.edit_hotel_name);
        editStartDate = findViewById(R.id.edit_start_date);
        editEndDate = findViewById(R.id.edit_end_date);

        Button saveButton = findViewById(R.id.button_save);
        Button deleteButton = findViewById(R.id.button_delete);
        Button addExcursionButton = findViewById(R.id.button_add_excursion);
        Button setAlertButton = findViewById(R.id.button_set_alert);
        Button shareButton = findViewById(R.id.button_share);

        RecyclerView excursionRecyclerView = findViewById(R.id.excursion_recycler_view);
        excursionAdapter = new ExcursionAdapter(this);
        excursionRecyclerView.setAdapter(excursionAdapter);
        excursionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        if (intent.hasExtra("vacationId")) {
            isNewVacation = false;
            vacationId = intent.getIntExtra("vacationId", -1);
            editName.setText(intent.getStringExtra("vacationName"));
            editHotel.setText(intent.getStringExtra("vacationHotel"));
            editStartDate.setText(intent.getStringExtra("vacationStartDate"));
            editEndDate.setText(intent.getStringExtra("vacationEndDate"));
        }

        editStartDate.setOnClickListener(v -> showDatePicker(editStartDate));
        editEndDate.setOnClickListener(v -> showDatePicker(editEndDate));

        saveButton.setOnClickListener(v -> saveVacation());
        deleteButton.setOnClickListener(v -> confirmDeleteVacation());
        setAlertButton.setOnClickListener(v -> setVacationAlerts());
        shareButton.setOnClickListener(v -> shareVacation());

        addExcursionButton.setOnClickListener(v -> {
            if (isNewVacation) {
                Toast.makeText(this, "Please save the vacation first", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent excursionIntent = new Intent(VacationDetailActivity.this, ExcursionDetailActivity.class);
            excursionIntent.putExtra("vacationId", vacationId);
            excursionIntent.putExtra("vacationStartDate", editStartDate.getText() != null ? editStartDate.getText().toString() : "");
            excursionIntent.putExtra("vacationEndDate", editEndDate.getText() != null ? editEndDate.getText().toString() : "");
            startActivity(excursionIntent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isNewVacation) {
            String startDate = editStartDate.getText() != null ? editStartDate.getText().toString() : "";
            String endDate = editEndDate.getText() != null ? editEndDate.getText().toString() : "";
            excursionAdapter.setVacationDates(startDate, endDate);
            List<Excursion> excursions = repository.getExcursionsForVacation(vacationId);
            excursionAdapter.setExcursions(excursions);
        }
    }

    private boolean isEndDateAfterStartDate(String startDateStr, String endDateStr) {
        try {
            Date startDate = dateFormat.parse(startDateStr);
            Date endDate = dateFormat.parse(endDateStr);
            return endDate != null && startDate != null && (!endDate.before(startDate));
        } catch (ParseException e) {
            return false;
        }
    }

    private void saveVacation() {
        String name = editName.getText() != null ? editName.getText().toString().trim() : "";
        String hotel = editHotel.getText() != null ? editHotel.getText().toString().trim() : "";
        String startDate = editStartDate.getText() != null ? editStartDate.getText().toString().trim() : "";
        String endDate = editEndDate.getText() != null ? editEndDate.getText().toString().trim() : "";

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter a vacation name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (name.length() > 100) {
            Toast.makeText(this, R.string.name_too_long, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!startDate.isEmpty() && !isValidDateFormat(startDate)) {
            Toast.makeText(this, R.string.invalid_date_format, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!endDate.isEmpty() && !isValidDateFormat(endDate)) {
            Toast.makeText(this, R.string.invalid_date_format, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!startDate.isEmpty() && !endDate.isEmpty() && !isEndDateAfterStartDate(startDate, endDate)) {
            Toast.makeText(this, R.string.end_date_before_start, Toast.LENGTH_SHORT).show();
            return;
        }

        Vacation vacation = new Vacation(name, hotel, startDate, endDate);

        if (isNewVacation) {
            repository.insert(vacation);
        } else {
            vacation.setId(vacationId);
            repository.update(vacation);
        }

        Toast.makeText(this, R.string.vacation_saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void confirmDeleteVacation() {
        if (isNewVacation) {
            finish();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete_title)
                .setMessage(R.string.confirm_delete_vacation)
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteVacation())
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void deleteVacation() {
        int excursionCount = repository.getExcursionCountForVacation(vacationId);
        if (excursionCount > 0) {
            Toast.makeText(this, R.string.cannot_delete_vacation, Toast.LENGTH_LONG).show();
            return;
        }

        Vacation vacation = new Vacation(
                editName.getText() != null ? editName.getText().toString() : "",
                editHotel.getText() != null ? editHotel.getText().toString() : "",
                editStartDate.getText() != null ? editStartDate.getText().toString() : "",
                editEndDate.getText() != null ? editEndDate.getText().toString() : ""
        );
        vacation.setId(vacationId);
        repository.delete(vacation);

        Toast.makeText(this, R.string.vacation_deleted, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setVacationAlerts() {
        String name = editName.getText() != null ? editName.getText().toString().trim() : "";
        String startDate = editStartDate.getText() != null ? editStartDate.getText().toString().trim() : "";
        String endDate = editEndDate.getText() != null ? editEndDate.getText().toString().trim() : "";

        if (startDate.isEmpty() || endDate.isEmpty()) {
            Toast.makeText(this, R.string.please_enter_dates, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidDateFormat(startDate) || !isValidDateFormat(endDate)) {
            Toast.makeText(this, R.string.invalid_date_format, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);

            if (start != null) setAlert(start.getTime(), name, getString(R.string.vacation_starting) + ": " + name, vacationId * 2);
            if (end != null) setAlert(end.getTime(), name, getString(R.string.vacation_ending) + ": " + name, vacationId * 2 + 1);

            Toast.makeText(this, R.string.alert_set, Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            Toast.makeText(this, R.string.invalid_date_format, Toast.LENGTH_SHORT).show();
        }
    }

    private void shareVacation() {
        String name = editName.getText() != null ? editName.getText().toString().trim() : "";
        String hotel = editHotel.getText() != null ? editHotel.getText().toString().trim() : "";
        String startDate = editStartDate.getText() != null ? editStartDate.getText().toString().trim() : "";
        String endDate = editEndDate.getText() != null ? editEndDate.getText().toString().trim() : "";

        StringBuilder shareText = new StringBuilder();
        shareText.append("Vacation Details\n\n");
        shareText.append("Title: ").append(name).append("\n");
        shareText.append("Hotel: ").append(hotel).append("\n");
        shareText.append("Start Date: ").append(startDate).append("\n");
        shareText.append("End Date: ").append(endDate).append("\n");

        if (!isNewVacation) {
            List<Excursion> excursions = repository.getExcursionsForVacation(vacationId);
            if (excursions != null && !excursions.isEmpty()) {
                shareText.append("\nExcursions:\n");
                for (Excursion excursion : excursions) {
                    shareText.append("- ").append(excursion.getTitle())
                            .append(" (").append(excursion.getDate()).append(")\n");
                }
            }
        }

        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Vacation: " + name);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText.toString());
        startActivity(Intent.createChooser(shareIntent, "Share Vacation"));
    }
}
