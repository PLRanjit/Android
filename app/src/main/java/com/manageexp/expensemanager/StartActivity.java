package com.manageexp.expensemanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class StartActivity extends AppCompatActivity {

    ImageView imageView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        imageView = (ImageView) findViewById(R.id.imageView2);

        try {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(StartActivity.this, ExpenseListActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }catch(Exception e){
            Toast.makeText(this,"Error: "+e,Toast.LENGTH_LONG).show();
        }
    }
}
