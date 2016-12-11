package com.example.dam212.flipi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;

/**
 * Muestra un ListView exclusivamente, conteniendo ListItems como elemento base.
 */
public class Preferences extends Activity  {

    /**
     * Callback generado cuando se inicia la actividad.
     * Aplica los valores recividos de la actividad principal a los elementos gráficos.
     * @param savedInstanceState Bundle - Recivido desde activity main.
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preferences);
        Bundle extras = getIntent().getExtras();

        ((EditText)findViewById(R.id.editTextUsername)).setText(extras.getString("Username"));
        ((Switch)findViewById(R.id.switchSD)).setChecked(extras.getBoolean("SaveToSD"));
    }

    /**
     * Guadra las preferencias ajustadas por el usuario en la interfaz gráfica en un bundle.
     * Este se devuelve a mainActivity y finaliza esta actividad.
     * @param view
     */
    public void savePreferences(View view) {
        Intent resultIntent = new Intent();
        Log.i("PREFERENCES", "User saved the preferences.");

        String username = ((EditText)findViewById(R.id.editTextUsername)).getText().toString();
        boolean saveToSD = ((Switch)findViewById(R.id.switchSD)).isChecked();

        resultIntent.putExtra("Username", username);
        resultIntent.putExtra("SaveToSD", saveToSD);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
