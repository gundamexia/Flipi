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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(getIntent().getStringExtra("exitApp") != null) finish();

        initVariables();
        saveLogIn();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }


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

        intentFlipi.putExtra("Rows", seekBarRows.getProgress());

        intentFlipi.putExtra("Columns", seekBarColumns.getProgress());
        intentFlipi.putExtra("MaxLoop", seekBarMaxLoop.getProgress());
        intentFlipi.putExtra("selectedGame", spinnerTypeOfGame.getSelectedItemPosition());
        intentFlipi.putExtra("username", username);
        intentFlipi.putExtra("saveToSD", saveToSD);
        if(checkBoxSound.isSelected()) intentFlipi.putExtra("Sound", true);
        if(checkBoxHaptic.isSelected()) intentFlipi.putExtra("Haptic", true);

        startActivityForResult(intentFlipi, FLIPI);
    }

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

    private void onPreferencesResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle result = data.getExtras();
            username = result.getString("Username");
            saveToSD = result.getBoolean("SaveToSD");
        }
    }

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

    private void launchScoresActivity(){
        Intent intentScore = new Intent(this, score.class);
        startActivity(intentScore);
    }

    private void launchPreferencesActivity(){
        Intent intentPreferences = new Intent(this, Preferences.class);
        intentPreferences.putExtra("Username", username);
        intentPreferences.putExtra("SaveToSD", saveToSD);
        startActivityForResult(intentPreferences, PREFERENCES);
    }

    // FUCK ME, uso una variable deprecated, lo sé.
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
