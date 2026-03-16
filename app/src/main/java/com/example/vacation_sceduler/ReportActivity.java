package com.example.vacation_sceduler;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.vacation_sceduler.database.VacationRepository;
import com.example.vacation_sceduler.entities.Vacation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_report);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setTitle(R.string.report_title);

        String timestamp = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a", Locale.US).format(new Date());
        TextView generatedView = findViewById(R.id.text_generated);
        generatedView.setText(getString(R.string.report_generated, timestamp));

        VacationRepository repository = new VacationRepository(getApplication());
        List<Vacation> vacations = repository.getAllVacations();

        TableLayout table = findViewById(R.id.table_report);
        addHeaderRow(table);

        if (vacations != null) {
            for (Vacation v : vacations) {
                int excursionCount = repository.getExcursionCountForVacation(v.getId());
                addDataRow(table, v, excursionCount);
            }
        }
    }

    private void addHeaderRow(TableLayout table) {
        TableRow row = new TableRow(this);
        row.setBackgroundResource(R.color.report_header_bg);

        String[] headers = {
                getString(R.string.col_vacation_name),
                getString(R.string.col_hotel),
                getString(R.string.col_start_date),
                getString(R.string.col_end_date),
                getString(R.string.col_excursions)
        };

        for (String header : headers) {
            row.addView(makeCell(header, true));
        }
        table.addView(row);
    }

    private void addDataRow(TableLayout table, Vacation vacation, int excursionCount) {
        TableRow row = new TableRow(this);

        String[] values = {
                vacation.getName(),
                vacation.getHotel() != null ? vacation.getHotel() : "",
                vacation.getStartDate() != null ? vacation.getStartDate() : "",
                vacation.getEndDate() != null ? vacation.getEndDate() : "",
                String.valueOf(excursionCount)
        };

        for (String value : values) {
            row.addView(makeCell(value, false));
        }
        table.addView(row);
    }

    private TextView makeCell(String text, boolean isHeader) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setPadding(16, 12, 16, 12);
        tv.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        tv.setMinWidth(180);

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 1, 1);
        tv.setLayoutParams(params);

        if (isHeader) {
            tv.setTypeface(null, Typeface.BOLD);
            tv.setTextSize(14f);
        } else {
            tv.setTextSize(13f);
        }
        return tv;
    }
}
