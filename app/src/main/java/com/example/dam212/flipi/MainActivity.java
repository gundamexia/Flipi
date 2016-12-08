package com.example.dam212.flipi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private final int GAMEFINISHED = 0;

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
        Intent intentFlipi = new Intent(this, Flipi.class);

        intentFlipi.putExtra("Rows", seekBarRows.getProgress());

        intentFlipi.putExtra("Columns", seekBarColumns.getProgress());
        intentFlipi.putExtra("MaxLoop", seekBarMaxLoop.getProgress());
        intentFlipi.putExtra("selectedGame", spinnerTypeOfGame.getSelectedItemPosition());
        if(checkBoxSound.isSelected()) intentFlipi.putExtra("Sound", true);
        if(checkBoxHaptic.isSelected()) intentFlipi.putExtra("Haptic", true);

        startActivityForResult(intentFlipi, GAMEFINISHED);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case GAMEFINISHED:
                if (resultCode == RESULT_OK) {
                    Bundle result = data.getExtras();
                    Log.d("RESULT",data.getExtras().getString("clicks"));
                    Toast.makeText(this, result.getString("clicks"), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
