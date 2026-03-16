package com.example.vacation_sceduler;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vacation_sceduler.database.VacationRepository;
import com.example.vacation_sceduler.entities.Excursion;
import com.example.vacation_sceduler.receivers.VacationAlertReceiver;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExcursionDetailActivity extends AppCompatActivity {

    private TextInputEditText editTitle;
    private TextInputEditText editDate;

    private VacationRepository repository;

    private int excursionId = -1;
    private int vacationId = -1;
    private boolean isNewExcursion = true;
    private String vacationStartDate;
    private String vacationEndDate;

    private static final String DATE_FORMAT = "MM/dd/yyyy";
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.US);

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

        editDate.setOnClickListener(v -> showDatePicker());

        saveButton.setOnClickListener(v -> saveExcursion());
        deleteButton.setOnClickListener(v -> deleteExcursion());
        setAlertButton.setOnClickListener(v -> setExcursionAlert());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String date = String.format(Locale.US, "%02d/%02d/%04d",
                            selectedMonth + 1, selectedDay, selectedYear);
                    editDate.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private boolean isValidDateFormat(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return false;
        }
        try {
            dateFormat.setLenient(false);
            dateFormat.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
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
            return !excursionDate.before(startDate) && !excursionDate.after(endDate);
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

    private void deleteExcursion() {
        if (isNewExcursion) {
            finish();
            return;
        }

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
            setAlert(excursionDate.getTime(), title, "Excursion: " + title, excursionId + 10000);
            Toast.makeText(this, R.string.excursion_alert_set, Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            Toast.makeText(this, R.string.invalid_date_format, Toast.LENGTH_SHORT).show();
        }
    }

    private void setAlert(long triggerTime, String title, String message, int requestCode) {
        Intent intent = new Intent(this, VacationAlertReceiver.class);
        intent.putExtra("title", title);
        intent.putExtra("message", message);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
                }
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            }
        }
    }
}
