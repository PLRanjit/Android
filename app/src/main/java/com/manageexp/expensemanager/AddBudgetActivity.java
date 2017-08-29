package com.manageexp.expensemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddBudgetActivity extends Activity implements OnClickListener {

    private Button addTodoBtn;
    private EditText subjectEditText;
    private EditText descEditText;
    private TextView dateTextView;
    private Calendar myCalendar;
    private EditText amount;

    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Add Budget");

        setContentView(R.layout.activity_temp);

        myCalendar = Calendar.getInstance();
        subjectEditText = (EditText) findViewById(R.id.subject_edittext);
        descEditText = (EditText) findViewById(R.id.description_edittext);
        dateTextView = (TextView) findViewById(R.id.date);
        amount = (EditText) findViewById(R.id.amt);


        addTodoBtn = (Button) findViewById(R.id.add_record);

        dbManager = new DBManager(this);
        dbManager.open();
        addTodoBtn.setOnClickListener(this);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy E");
        dateTextView.setText(df.format(c.getTime()));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_record:

                final String name = subjectEditText.getText().toString();
                final String desc = descEditText.getText().toString();
                final String amt = amount.getText().toString();
                String dateD = dateTextView.getText().toString();

                dbManager.insert(name, desc, amt, dateD);

                Intent main = new Intent(AddBudgetActivity.this, ExpenseListActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(main);

                break;
        }
    }
}