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

import com.example.vacation_sceduler.database.VacationRepository;
import com.example.vacation_sceduler.entities.Excursion;
import com.example.vacation_sceduler.ui.BaseDetailActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.util.Date;

public class ExcursionDetailActivity extends BaseDetailActivity {

    private TextInputEditText editTitle;
    private TextInputEditText editDate;

    private VacationRepository repository;

    private int excursionId = -1;
    private int vacationId = -1;
    private boolean isNewExcursion = true;
    private String vacationStartDate;
    private String vacationEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setTitle(R.string.excursion_detail);

        repository = new VacationRepository(getApplication());

        editTitle = findViewById(R.id.edit_excursion_title);
        editDate = findViewById(R.id.edit_excursion_date);

        Button saveButton = findViewById(R.id.button_save);
        Button deleteButton = findViewById(R.id.button_delete);
        Button setAlertButton = findViewById(R.id.button_set_alert);

        Intent intent = getIntent();
        vacationId = intent.getIntExtra("vacationId", -1);
        vacationStartDate = intent.getStringExtra("vacationStartDate");
        vacationEndDate = intent.getStringExtra("vacationEndDate");

        if (intent.hasExtra("excursionId")) {
            isNewExcursion = false;
            excursionId = intent.getIntExtra("excursionId", -1);
            editTitle.setText(intent.getStringExtra("excursionTitle"));
            editDate.setText(intent.getStringExtra("excursionDate"));
        }

        editDate.setOnClickListener(v -> showDatePicker(editDate));

        saveButton.setOnClickListener(v -> saveExcursion());
        deleteButton.setOnClickListener(v -> confirmDeleteExcursion());
        setAlertButton.setOnClickListener(v -> setExcursionAlert());
    }

    private boolean isDateWithinVacation(String excursionDateStr) {
        if (vacationStartDate == null || vacationStartDate.isEmpty() ||
                vacationEndDate == null || vacationEndDate.isEmpty()) {
            return true;
        }
        try {
            Date excursionDate = dateFormat.parse(excursionDateStr);
            Date startDate = dateFormat.parse(vacationStartDate);
            Date endDate = dateFormat.parse(vacationEndDate);
            return excursionDate != null && startDate != null && endDate != null
                    && !excursionDate.before(startDate) && !excursionDate.after(endDate);
        } catch (ParseException e) {
            return true;
        }
    }

    private void saveExcursion() {
        String title = editTitle.getText() != null ? editTitle.getText().toString().trim() : "";
        String date = editDate.getText() != null ? editDate.getText().toString().trim() : "";

        if (title.isEmpty()) {
            Toast.makeText(this, "Please enter an excursion title", Toast.LENGTH_SHORT).show();
            return;
        }

        if (title.length() > 100) {
            Toast.makeText(this, R.string.name_too_long, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!date.isEmpty() && !isValidDateFormat(date)) {
            Toast.makeText(this, R.string.invalid_date_format, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!date.isEmpty() && !isDateWithinVacation(date)) {
            Toast.makeText(this, R.string.excursion_date_not_in_vacation, Toast.LENGTH_LONG).show();
            return;
        }

        Excursion excursion = new Excursion(title, date, vacationId);

        if (isNewExcursion) {
            repository.insert(excursion);
        } else {
            excursion.setId(excursionId);
            repository.update(excursion);
        }

        Toast.makeText(this, R.string.excursion_saved, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void confirmDeleteExcursion() {
        if (isNewExcursion) {
            finish();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_delete_title)
                .setMessage(R.string.confirm_delete_excursion)
                .setPositiveButton(R.string.delete, (dialog, which) -> deleteExcursion())
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void deleteExcursion() {
        Excursion excursion = new Excursion(
                editTitle.getText() != null ? editTitle.getText().toString() : "",
                editDate.getText() != null ? editDate.getText().toString() : "",
                vacationId
        );
        excursion.setId(excursionId);
        repository.delete(excursion);

        Toast.makeText(this, R.string.excursion_deleted, Toast.LENGTH_SHORT).show();
        finish();
    }

    private void setExcursionAlert() {
        String title = editTitle.getText() != null ? editTitle.getText().toString().trim() : "";
        String date = editDate.getText() != null ? editDate.getText().toString().trim() : "";

        if (date.isEmpty()) {
            Toast.makeText(this, R.string.please_enter_excursion_date, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isValidDateFormat(date)) {
            Toast.makeText(this, R.string.invalid_date_format, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Date excursionDate = dateFormat.parse(date);
            if (excursionDate != null) {
                setAlert(excursionDate.getTime(), title, "Excursion: " + title, excursionId + 10000);
                Toast.makeText(this, R.string.excursion_alert_set, Toast.LENGTH_SHORT).show();
            }
        } catch (ParseException e) {
            Toast.makeText(this, R.string.invalid_date_format, Toast.LENGTH_SHORT).show();
        }
    }
}
