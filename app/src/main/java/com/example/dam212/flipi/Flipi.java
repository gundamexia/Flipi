package com.example.dam212.flipi;

import android.app.Activity;
import android.os.Bundle;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
public class Flipi extends AppCompatActivity {

    //tipos de juego
    private final int COLOR = 0;
    private final int NUMBER = 1;

    //colores
    private static final int []colors = new int[]{
            R.drawable.ic_1c,
            R.drawable.ic_2c,
            R.drawable.ic_3c,
            R.drawable.ic_4c,
            R.drawable.ic_5c,
            R.drawable.ic_6c
    };


    //numeros
    private static final int []numbers = new int[]{
            R.drawable.ic_1n,
            R.drawable.ic_2n,
            R.drawable.ic_3n,
            R.drawable.ic_4n,
            R.drawable.ic_5n,
            R.drawable.ic_6n
    };


    //mantener el array que el usuario hay decidido utilizar
    private int []pictures = null;

    // Número máximo de celdas horizontales y verticales
    private int topTileX = 3;
    private int topTileY = 3;

    // Número máximo de elementos a utilizar
    private int topElements = 2;

    //	Si ha seleccionado o no usar sonido y vibración
    private boolean hasSound = false;
    private boolean hasVibration = false;

    // Array con los identificadores de las celdas cuando
    // se añadan al layout,
    // para poder recuperarlos durante la partida
    private int ids[][] = null;

    // Array para guardar los valores de los índices de cada una de las celdas.
    // se utilizará para agilizar la comprobación de si la partida ha acabado o no.
    private int values[][] = null;

    // Contador con el número de pulsaciones que ha realizado el jugador
    private int numberOfClicks = 0;

    // Para reproducir un sonido cuando el usuario pulse una celda
    private MediaPlayer mp = null;

    // Para hacer vibrar el dispositivo cuando el usuario pulse una celda
    private Vibrator vibratorService = null;

    // Mostrará en pantalla el las veces que el usuario ha pulsado una celda
    private TextView tvNumberOfClicks = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flipi);
        vibratorService = (Vibrator)(getSystemService(Service.VIBRATOR_SERVICE));
        mp = MediaPlayer.create(this, R.raw.touch);
        tvNumberOfClicks = (TextView) findViewById(R.id.clicksTxt);


        //obtención de parámetros de configuración
        Bundle extras = getIntent().getExtras();
        topTileX = extras.getInt("Rows") + 2;
        topTileY = extras.getInt("Columns") + 2;

        topElements = extras.getInt("MaxLoop") + 1;

        if ( extras.getInt("selectedGame") == COLOR) pictures = colors;
        else if ( extras.getInt("selectedGame") == NUMBER) pictures = numbers;

        hasSound= extras.getBoolean("Sound");
        hasVibration= extras.getBoolean("Haptic");


        //limpiar el tablero
        LinearLayout ll = (LinearLayout) findViewById(R.id.fieldLandscape);
        ll.removeAllViews();


        //obtención de tamaño de pantalla
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        final int WIDTH = dm.widthPixels / topTileX;
        final int HEIGHT = (dm.heightPixels - 90) / topTileY;


        //inicialización de arrays
        ids =  new int [topTileX][topTileY];
        values =  new int [topTileX][topTileY];


        //inicialización de números aleatorio
        Random r = new Random(System.currentTimeMillis());

        // crear celdas
        int ident = 0;
        for (int i = 0; i < topTileY; i++) {
            LinearLayout l2 = new LinearLayout(this);
            l2.setOrientation(LinearLayout.HORIZONTAL);
            for (int j = 0; j < topTileX; j++) {
                int tilePictureToShow = r.nextInt(topElements);

                // guardamos la trama a mostrar
                values[j][i] = tilePictureToShow;
                TileView tv = new TileView(this, j, i, topElements, tilePictureToShow, pictures[tilePictureToShow]);
                ident++;

                // se asigna un identificador al objeto creado
                tv.setId(ident);

                // se guarda el identificador en una matriz
                ids[j][i] = ident;
                tv.setHeight(HEIGHT);
                tv.setWidth(WIDTH);
                tv.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        hasClick(((TileView) view).x, ((TileView) view).y);
                    }
                });
                l2.addView(tv);
            }
            ll.addView(l2);
        }
        // cronómetro
        Chronometer t = (Chronometer)findViewById(R.id.Chronometer);
        t.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.flipi_menu, menu);
        return true;
    }

    //Método llamado al clickar en un botón (Definido programáticamente).
    protected void hasClick(int x, int y) {
        Log.i("Clicked", "Start of click action");
        // vibrar y/o sonar si está configurado
        if (hasVibration) vibratorService.vibrate(100);
        if (hasSound) mp.start();
        //cambiar la celda pulsada
        changeView(x, y);
        //esquinas del tablero
        //Superior izquierda
        if (x==0 && y == 0){
            changeView(0, 1);
            changeView(1, 0);
            changeView(1, 1);
        }
        //Inferior izquierda
        else if (x == 0 && y  == topTileY -1 ){
            changeView(0, topTileY-2);
            changeView(1, topTileY-2);
            changeView(1, topTileY -1);
        }
        //Superior derecha
        else if (x == topTileX -1 && y  == 0 ){ //
            changeView( topTileX-2,0);
            changeView( topTileX-2,1);
            changeView( topTileX -1,1);
        }
        //Inferior derecha
        else if (x == topTileX -1 && y  == topTileY-1 ){
            changeView( topTileX-2,topTileY-1);
            changeView( topTileX-2,topTileY-2);
            changeView( topTileX -1,topTileY-2);
        }
        // lados del tablero
        //Izquierda
        else if (x == 0){
            changeView( x,y-1);
            changeView( x,y+1);
            changeView( x+1 ,y);
        }
        //Arriba
        else if (y == 0){
            changeView( x-1,y);
            changeView( x+1,y);
            changeView( x ,y+1);
        }
        //Derecha
        else if (x == topTileX-1){
            changeView( x,y-1);
            changeView( x,y+1);
            changeView( x-1 ,y);
        }
        //Abajo
        else if (y == topTileY-1){
            changeView( x-1,y);
            changeView( x+1,y);
            changeView( x ,y-1);
        }
        //resto
        else{
            changeView( x-1,y);
            changeView( x+1,y);
            changeView( x,y-1);
            changeView( x,y+1);
        }
        // actualiza marcador
        numberOfClicks++;
        tvNumberOfClicks.setText( getString(R.string.textViewPulsations) + numberOfClicks );
        // se ha acabado la partida?
        if (hasFinished()) gameWon();
    }

    //No estoy muy seguro de como funciona,
    // pero lo que hace es actualizar las cuadriculas (numero o color) CREO.
    private void changeView(int x, int y){
        TileView tt = (TileView) findViewById(ids[x][y]);
        int newIndex = tt.getNewIndex();
        values[x][y] = newIndex;
        tt.setBackgroundResource(pictures[newIndex]);
        tt.invalidate();
    }

    //Comprueba que la partida haya acabado.
    //Es decir que todas las casillas compartan número o color.
    private Boolean hasFinished() {
        Log.i("checkIfFinished", "Start of check action");
        int targetValue = values[0][0];
        for (int i = 0; i < topTileY; i++) {
            for (int j = 0; j < topTileX; j++) {
                if (values[j][i] != targetValue) return false;
            }
        }
        return true;
    }


    //Acaba la partida devolviendo el total de clicks.
    private void gameWon(){
        Intent resultIntent = new Intent();
        Log.i("GAME_FINISHED", "User won the game");
        //Guarda al ganar
        //saveWinner()
        resultIntent.putExtra("clicks", numberOfClicks);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    //
    @Override
    public void onBackPressed() {
        Log.i("GAME_FINISHED", "Back button pressed");
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * Guarda la información del jugador, el número de clicks que hizo, el nombre, el tiempo invertido
     * y cómo lo quiere guardar el jugador.
     * @param numberOfClicks int que contiene la cantidad de clicks que hizo el jugador
     * @param playerName String nombre del jugador
     * @param investedTime Date tiempo invertido por el jugador
     * @param storageType int tipo de guardado que quiere el jugador. 0 = Interno, 1 = SD
     */
    private void saveWinner(int numberOfClicks, String playerName, Date investedTime, int storageType) {
        String fileName = "results.txt";
        FileOutputStream fOS = null;
        String data = numberOfClicks + "," + playerName + "," + investedTime;
        try {
            if(storageType == 0) {
                File file = new File(this.getFilesDir(), fileName);
                if(!file.exists()) file.createNewFile();
                fOS = new FileOutputStream(file, true);
                fOS.write(data.getBytes());
                Toast.makeText(this, "Data of the player saved", Toast.LENGTH_SHORT).show();
            } else {
                if(isExternalStorageWritable()) {
                    File file = new File(Environment.getExternalStorageDirectory().toString() + "/flipi/" + fileName);
                    if(!file.exists()) {
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    }
                    fOS = new FileOutputStream(file, true);
                    fOS.write(data.getBytes());
                    Toast.makeText(this, "Data of the player saved", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(this, "Insert a SD card", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            //e.printStackTrace();
            Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show();
        } finally {
            if(fOS != null) try {
                fOS.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
    }

    /**
     * Revisa si el almacenamiento externo está disponible para la lectura y escritura.
     * @return boolean True si está disponible, False si no lo está
     */
    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemExitGame:
                onBackPressed();
                return true;
            case R.id.itemExitApp:
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("exitApp", "exitapp");
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
