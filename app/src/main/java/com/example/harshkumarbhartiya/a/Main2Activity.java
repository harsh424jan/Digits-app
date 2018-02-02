package com.example.harshkumarbhartiya.a;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Main2Activity extends AppCompatActivity {


    public int radioid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main2);

    }
    public void onclick(View view){
        Intent i = new Intent(Main2Activity.this, Main3Activity.class);
        RadioGroup rg= (RadioGroup) findViewById(R.id.rg);
        radioid=rg.getCheckedRadioButtonId();
        i.putExtra("radioID", radioid);
        startActivity(i);
    }
}
