package com.manageexp.expensemanager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class ExpenseListActivity extends AppCompatActivity implements View.OnClickListener {

    private DBManager dbManager;
    private FloatingActionButton addRecord, addBudget, viewReport;
    private ListView listView;
    private FloatingActionMenu menuF;
    private SimpleCursorAdapter adapter;


    final String[] from = new String[] { DatabaseHelper._ID,
            DatabaseHelper.SUBJECT, DatabaseHelper.DESC, DatabaseHelper.AMOUNT, DatabaseHelper.DATE};

    final int[] to = new int[] { R.id.id, R.id.title, R.id.desc ,R.id.amt, R.id.date};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_emp_list);

        menuF = (FloatingActionMenu) findViewById(R.id.menu);
        menuF.setClosedOnTouchOutside(true);

        addRecord = (FloatingActionButton) findViewById(R.id.add_Me);
        addRecord.setOnClickListener(this);

        addBudget = (FloatingActionButton) findViewById(R.id.add_Budget);
        addBudget.setVisibility(View.GONE);
        addBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuF.close(true);
                Intent add_mem = new Intent(ExpenseListActivity.this, AddBudgetActivity.class);
                startActivity(add_mem);
            }
        });

        viewReport = (FloatingActionButton) findViewById(R.id.view_report);
        viewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuF.close(true);
                try {
                    Intent add_mem = new Intent(ExpenseListActivity.this, ViewPieActivity.class);
                    startActivity(add_mem);
                } catch (Exception e) {
                    Toast.makeText(ExpenseListActivity.this, "Error Country : " + e, Toast.LENGTH_LONG).show();
                }
            }
        });

        dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        listView = (ListView) findViewById(R.id.list_view);
        listView.setEmptyView(findViewById(R.id.empty));
        adapter = new SimpleCursorAdapter(this, R.layout.activity_view_record, cursor, from, to, 0);
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);
        onScroll();

        // OnCLickListiner For List Items
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long viewId) {
                TextView idTextView = (TextView) view.findViewById(R.id.id);
                TextView titleTextView = (TextView) view.findViewById(R.id.title);
                TextView descTextView = (TextView) view.findViewById(R.id.desc);
                TextView amtTextView = (TextView) view.findViewById(R.id.amt);
                TextView dateTextView = (TextView) view.findViewById(R.id.date);

                String id = idTextView.getText().toString();
                String title = titleTextView.getText().toString();
                String desc = descTextView.getText().toString();
                String amount = amtTextView.getText().toString();
                String date = dateTextView.getText().toString();

                String[] titleBreak = title.toLowerCase().split(" ");

                Intent modify_intent = new Intent(getApplicationContext(), ModifyExpenseActivity.class);
                modify_intent.putExtra("title", title);
                modify_intent.putExtra("desc", desc);
                modify_intent.putExtra("id", id);
                modify_intent.putExtra("amount", amount);
                modify_intent.putExtra("date", date);

                startActivity(modify_intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.add_Budget) {
            Intent add_mem = new Intent(this, AddBudgetActivity.class);
            startActivity(add_mem);
        }

        if (id == R.id.add_record) {
            Intent add_mem = new Intent(this, AddExpenseActivity.class);
            startActivity(add_mem);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        menuF.close(true);
        Intent add_mem = new Intent(this, AddExpenseActivity.class);
        startActivity(add_mem);
    }

    @Override
    protected void onStop() {
        super.onStop();
        dbManager.close();
    }

    public void onScroll()
    {
        ListView lv = listView;
        int childCount = lv.getChildCount();

        for (int i = 0; i < childCount; i++)
        {
            View v1 = lv.getChildAt(i);
            ImageView tx = (ImageView) v1.findViewById(R.id.imageView);
            tx.setImageResource(R.drawable.ic_grocery);
        }
    }

}