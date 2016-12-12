package com.example.dam212.flipi;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 *  Punto de entrada de la actividad, aquí se presentan al usuario la configuración básica de la aplicación.
 *  FLIPI & PREFERENCES: int - Alias para lanzar las clases sugeridas.
 *  username: String - Nombre del usuario.
 *  saveToSD: boolean - Si se ha de guardar en la memoria local o externa.
 *  seekBarRows, seekBarColumns, seekBarMaxLoop: SeekBar - Guardan el elemento gráfico.
 *  checkBoxSound, heckBoxHaptic: CheckBox - Guardan el elemento gráfico.
 *  spinnerTypeOfGame: Spinner - Guardan el elemento gráfico.
 */
public class MainActivity extends AppCompatActivity {

    private final int FLIPI = 0;
    private final int PREFERENCES = 1;

    private String username;
    private boolean saveToSD;

    private SeekBar seekBarRows;
    private SeekBar seekBarColumns;
    private SeekBar seekBarMaxLoop;
    private CheckBox checkBoxSound;
    private CheckBox checkBoxHaptic;
    private Spinner spinnerTypeOfGame;
    private boolean resumeGame;

    /**
     * Callback generado cuando se inicia la actividad.
     * Se encatga de llamar a initVariables().
     * Si recive la señal de salida del bundle, cierra la app.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resumeGame = false;

        if(getIntent().getStringExtra("exitApp") != null) finish();
        if(getIntent().getStringExtra("resumegame") != null) resumeGame = true;

        initVariables();
        if(getIntent().getStringExtra("resumegame") == null) saveLogIn();
    }

    /**
     * Callback generado cuando se inicia el menu de la actividad.
     * @param menu Menu - menu que se va a inflar.
     * @return Boolean - siempre true para que muestre el menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    /**
     * Callback generado cuando se clicka en un item del menu.
     * @param item MenuItem - item que se ha clickado.
     * @return retorna true para acabarlo aquí, si no ejecuta el procesamiento habitual.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemPreferences:
                launchPreferencesActivity();
                return true;
            case R.id.itemScores:
                launchScoresActivity();
                return true;
            case R.id.itemEnglish:
                changeLanguage(Locale.ENGLISH);
                return true;
            case R.id.itemSpanish:
                changeLanguage(new Locale("es"));
                return true;
            case R.id.itemLogIn:
                openLogInList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

        username = "Anonymous";
        saveToSD = false;
    }

    /**
     * Crea un nuevo intent para iniciar la clase flipi con las variables necesarias.
     * @param v es el View.
     */
    public void sendBundle(View v) {
        Intent intentFlipi = new Intent(this, Flipi.class);

        if(!resumeGame) {
            intentFlipi.putExtra("Rows", seekBarRows.getProgress());

            intentFlipi.putExtra("Columns", seekBarColumns.getProgress());
            intentFlipi.putExtra("MaxLoop", seekBarMaxLoop.getProgress());
            intentFlipi.putExtra("selectedGame", spinnerTypeOfGame.getSelectedItemPosition());
            intentFlipi.putExtra("username", username);
            intentFlipi.putExtra("saveToSD", saveToSD);
            if(checkBoxSound.isSelected()) intentFlipi.putExtra("Sound", true);
            if(checkBoxHaptic.isSelected()) intentFlipi.putExtra("Haptic", true);
        } else intentFlipi.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        //FLAG_ACTIVITY_REORDER_TO_FRONT trae de vuelta la activdad si ya se inició

        startActivityForResult(intentFlipi, FLIPI);
    }

    /**
     * Callback  llamado cuando se termina la ejecucion de la actividad llamada con "startActivityForResult"
     * Distingue entre las llamadas a Flipi y Preferences.
     * @param requestCode int - Que actividad acabó.
     * @param resultCode int - Como acabó la actividad.
     * @param data Intent - Datos retonados por la actividad.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case FLIPI:
                onFlipiResult(resultCode, data);
                break;
            case PREFERENCES:
                onPreferencesResult(resultCode, data);
                break;
        }
    }

    /**
     * Llamado cuando se termina la actividad Preferences.
     * Solo se ejecuta si el resultado es RESULT_OK
     * @param resultCode int - Como acabó la actividad.
     * @param data Intent - Datos retonados por la actividad.
     */
    private void onPreferencesResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle result = data.getExtras();
            username = result.getString("Username");
            saveToSD = result.getBoolean("SaveToSD");
        }
    }

    /**
     * Llamado cuando se termina la actividad Flipi.
     * Diferencia entre que acabara bien, no acabase o sea cancelado.
     * @param resultCode int - Como acabó la actividad.
     * @param data Intent - Datos retonados por la actividad.
     */
    private void onFlipiResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle result = data.getExtras();
            Log.d("RESULT",Integer.toString(data.getExtras().getInt("clicks")));
            Toast.makeText(this, "You won with: "+result.getInt("clicks")+" clicks.", Toast.LENGTH_LONG).show();
        }

        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "You exited the game.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *  Wrap para lanzar la actividad Scores.
     */
    private void launchScoresActivity(){
        Intent intentScore = new Intent(this, score.class);
        startActivity(intentScore);
    }

    /**
     *  Wrap para lanzar la actividad Preferences.
     *  Le manda username y saveToSD.
     */
    private void launchPreferencesActivity(){
        Intent intentPreferences = new Intent(this, Preferences.class);
        intentPreferences.putExtra("Username", username);
        intentPreferences.putExtra("SaveToSD", saveToSD);
        startActivityForResult(intentPreferences, PREFERENCES);
    }

    /**
     * Cambia el idioma de la aplicación a la indicada en language.
     * Después recrea toda interfaz para forzar el actualizado de las vistas.
     * @param language Locale - región a la cual se debe cambiar el idioma.
     * @deprecated Se usa: configuration.locale = language; que es una variable en desuso.
     */
    private void  changeLanguage(Locale language){
        Log.i("Lang","changed to \""+language.getDisplayName()+"\".");
        Configuration configuration = getResources().getConfiguration();
        configuration.locale = language;
        this.recreate();
    }

    /**
     * Sirve para guardar los inicios de sesión del usuario.
     */
    private void saveLogIn() {
        FileOutputStream fOS = null;
        File file = new File(this.getFilesDir(), "login.txt");
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy  hh:mm a");
        String date = format.format(new Date());
        String data = username + " - " + date + System.getProperty("line.separator");
        try {
            if(!file.exists()) file.createNewFile();
            fOS = new FileOutputStream(file, true);
            fOS.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fOS != null) try {
                fOS.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }

    /**
     * Abre la actividad score para mostrar los inicios de sesión
     */
    private void openLogInList() {
        Intent intent = new Intent(getApplicationContext(), score.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("openAsLogIn", "openAsLogIn");
        startActivity(intent);
    }

}
