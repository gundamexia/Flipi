package com.example.dam212.flipi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    SeekBar seekBarRows;
    SeekBar seekBarColumns;
    SeekBar seekBarMaxLoop;
    RadioGroup radioGroup;
    CheckBox checkBoxSound;
    CheckBox checkBoxHaptic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();
    }

    private void initVariables() {
        seekBarRows = (SeekBar) findViewById(R.id.seekBarRows);
        seekBarColumns = (SeekBar) findViewById(R.id.seekBarColumns);
        seekBarMaxLoop = (SeekBar) findViewById(R.id.seekBarMaxLoop);

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        checkBoxSound = (CheckBox) findViewById(R.id.checkBoxSound);
        checkBoxHaptic = (CheckBox) findViewById(R.id.checkBoxHaptic);
    }

    public void sendBundle(View v) {
        Intent intentFlipi = new Intent(this, Flipi.class);

        intentFlipi.putExtra("seekBarRows", seekBarRows.getProgress());

        startActivity(intentFlipi);
    }
}
