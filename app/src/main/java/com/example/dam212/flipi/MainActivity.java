package com.example.dam212.flipi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SeekBar seekBarRows;
    private SeekBar seekBarColumns;
    private SeekBar seekBarMaxLoop;
    private CheckBox checkBoxSound;
    private CheckBox checkBoxHaptic;
    private Spinner spinnerTypeOfGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVariables();
    }

    /**
     * Inicia los view con sus variables necesarias.
     */
    private void initVariables() {
        seekBarRows = (SeekBar) findViewById(R.id.seekBarRows);
        seekBarColumns = (SeekBar) findViewById(R.id.seekBarColumns);
        seekBarMaxLoop = (SeekBar) findViewById(R.id.seekBarMaxLoop);

        checkBoxSound = (CheckBox) findViewById(R.id.checkBoxSound);
        checkBoxHaptic = (CheckBox) findViewById(R.id.checkBoxHaptic);

        spinnerTypeOfGame = (Spinner) findViewById(R.id.spinnerTypeOfGame);
    }

    /**
     * Crea un nuevo intent para iniciar la clase flipi con las variables necesarias.
     * @param v es el View.
     */
    public void sendBundle(View v) {
        if(seekBarRows.getProgress() != 0 && seekBarColumns.getProgress() != 0 && seekBarMaxLoop.getProgress() != 0) {
            Intent intentFlipi = new Intent(this, Flipi.class);

            intentFlipi.putExtra("seekBarRows", seekBarRows.getProgress());
            intentFlipi.putExtra("seekBarColumns", seekBarColumns.getProgress());
            intentFlipi.putExtra("seekBarMaxLoop", seekBarMaxLoop.getProgress());
            intentFlipi.putExtra("selectedGame", spinnerTypeOfGame.getSelectedItemPosition());
            if(checkBoxSound.isSelected()) intentFlipi.putExtra("checkBoxSound", true);
            if(checkBoxHaptic.isSelected()) intentFlipi.putExtra("checkBoxHaptic", true);

            startActivity(intentFlipi);
        } else Toast.makeText(this, R.string.norowsnocolumnsnomaxloops, Toast.LENGTH_SHORT).show();
    }

}
