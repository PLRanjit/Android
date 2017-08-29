package com.manageexp.expensemanager;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.graphics.Typeface.BOLD;

public class ViewPieActivity extends Activity {

    private DBManager dbManager;

    GraphicalView mChartView = null;
    private DefaultRenderer mRenderer = new DefaultRenderer();
    private CategorySeries mSeries = new CategorySeries("Expenses");
    private static int[] COLORS = new int[] {
            Color.rgb(110, 246, 230), Color.rgb(110, 194, 246), Color.rgb(110, 126, 246),
            Color.rgb(162, 110, 246), Color.rgb(230, 110, 246), Color.rgb(246, 110, 194),
            Color.rgb(17, 175, 196)};


    /*
    Color.rgb(210, 112, 91), Color.rgb(210, 171, 91), Color.rgb(189, 210, 91),
            Color.rgb(130, 210, 91), Color.rgb(91, 210, 112), Color.rgb(91, 210, 172),
            Color.rgb(62, 67, 153)
    * */
    Spinner spinner;
    String mnthYr;
    TextView textView, totalExpense;
    int cYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Report");

        setContentView(R.layout.charts);

        dbManager = new DBManager(this);
        dbManager.open();

        textView = (TextView)findViewById(R.id.textView2);
        totalExpense = (TextView) findViewById(R.id.expenseTotal);
        spinner = (Spinner) findViewById(R.id.spinner);

        List<String> month = new ArrayList<String>();
        month.add("January");
        month.add("February");
        month.add("March");
        month.add("April");
        month.add("May");
        month.add("June");
        month.add("July");
        month.add("August");
        month.add("September");
        month.add("October");
        month.add("November");
        month.add("December");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, month);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);

        Calendar c = Calendar.getInstance();
        cYear = c.get(Calendar.YEAR) % 100;
        int cMonth = c.get(Calendar.MONTH)+ 1;
        spinner.setSelection(cMonth-1);
        mnthYr = cMonth+"/"+cYear;

       // Toast.makeText(this, "Month & Year 0"+mnthYr, Toast.LENGTH_LONG).show();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int month = (int) spinner.getSelectedItemId() + 1;
                mnthYr = month+"/"+cYear;
                //Toast.makeText(ViewPieActivity.this, "Month & Year Spinner "+mnthYr, Toast.LENGTH_LONG).show();
                try{
                    SetPie(dbManager.fetchPie(mnthYr));
                }catch(Exception e){
                    Toast.makeText(ViewPieActivity.this, "Error :"+ e, Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void SetPie(Cursor c){
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.chartsRelativeLayout);
        if (mChartView != null) {
            mRenderer = new DefaultRenderer();
            mSeries = new CategorySeries("Expenses");
            layout.removeView(mChartView);
        }
        int count = c.getCount();
        if(count > 0) {
            double[] values = new double[count];
            String[] categoryNames = new String[count];
            if (c != null) {
                int i = 0;
                if (c.moveToFirst()) {
                    do {
                        values[i] = c.getDouble(1);
                        categoryNames[i] = c.getString(0);
                        i++;
                    } while (c.moveToNext());
                }
            }
            for (int i = 0; i < count; i++) {
                mSeries.add(categoryNames[i] + "  ", values[i]);
                SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
                renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);
                renderer.setDisplayBoundingPoints(true);
                mRenderer.addSeriesRenderer(renderer);
                mRenderer.setDisplayValues(true);
                mRenderer.setTextTypeface("sans_serif", BOLD);
                mRenderer.setLabelsColor(Color.rgb(0, 0, 0));
                mRenderer.setFitLegend(true);
                mRenderer.setLabelsTextSize(24f);
                mRenderer.setLegendTextSize(24f);
            }

            mChartView = ChartFactory.getPieChartView(this, mSeries, mRenderer);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, 1000);
            mChartView.setLayoutParams(params);

            textView.setVisibility(View.INVISIBLE);
            layout.addView(mChartView);

            double sum = 0;
            for(int i = 0 ; i < values.length; i++){
                sum = sum + values[i];
            }

            totalExpense.setText("Total Expense : ₹"+ sum);
        }else{

            textView.setVisibility(View.VISIBLE);
            totalExpense.setText("Total Expense : ");
        }

    }

}