package com.example.dam212.flipi;

import android.os.Bundle;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.SystemClock;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 *  Juego sobre el cual versa la aplicación
 *  Variables internas:
 *  COLOR & NUMBER: int - Alias para saber si el usuario quiere colores o números en las casillas.
 *  colors & numbers: int[] - Array conteniendo en memoria los fondos: colores o números, respectivamente.
 *  pictures: int [] - Array definitivo de los fondos a usar.
 *  topTileX & topTileX: int - Tamáño máximo de la cuadrícula.ç
 *  topElements: int - Máximo número de elementos.
 *  hasSound & hasVibration: boolean - Definen si el usuario quiere sonido y/o vibracion, o no.
 *  ids: int[] - Matriz contenedora de todos los ids de las cuadrículas.
 *  values: int[] - Matriz contenedora de todos los valores de las cuadrículas.
 *  numberOfClicks: int - Numero de clicks realizados hasta el momento.
 *  mp: MediaPlayer - Reproductor de sonidos.
 *  vibratorService: Vibrator - Generador de respuesta háptica.
 *  tvNumberOfClicks: TextView - Elemento gráfico que mostrará los clics realizados (numberOfClicks).
 *  chronometer: Chronometer - Cronómetro del juego que contará el tiempo de ejecución y se lo mostrará al usuario.
 */
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

    // Cronómetro del juego;
    private Chronometer chronometer;

    /**
     * Callback definido por android, llamado cuando se arranca esta actividad.
     * Define las variables tanto internnas como externas, basándose en los parametros pasados a la actividad
     * y alguna función que varia en tiempo de ejecución.
     * Limpia el tablero de posibles retos.
     * Genera el contenido del tablero.
     * Finalmente se inicializa el cronómetro.
     * @param savedInstanceState Bundle pasado si es re-inicializado despues de haber sido parada la app.
     */
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
        chronometer = (Chronometer)findViewById(R.id.Chronometer);
        chronometer.start();
    }

    /**
     * Callback definido por android para "inflar" menús.
     * @param menu El menu de opciones en el que se colocarán los items.
     * @return Boolean - True siempre, en caso contrario, no mostraría el menú.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.flipi_menu, menu);
        return true;
    }

    /**
     * Lanza el actualizador en las casillas aledañas y la misma que se indica con los parámetros
     *  X e Y.
     *  Contempla todos los escenarios que pudieran lanzar excepciones (Evitando salirse
     *  del array).
     *  Comprueba si ha de sonar o si ha de vibrar en vase a las variables globales
     *  hasVibration y hasSound .
     *  Actualiza el contador de clicks y el elemento gráfico de representación de los mismos.
     *  Finalmente comprueba si se ha de acabar la partida {@link #hasFinished}.
     * @param x posición de la casilla en el eje horizontal.
     * @param y posición de la casilla en el eje vertical.
     */
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
        tvNumberOfClicks.setText(getString(R.string.textViewPulsations) + numberOfClicks);
        // se ha acabado la partida?
        if (hasFinished()) gameWon();
    }

    /**
     * Actualiza el valor y la imagen asociadas a la casilla indicada en la cuadrícula por los
     * parametros X e Y.
     * @param x posición de la casilla en el eje horizontal.
     * @param y posición de la casilla en el eje vertical.
     */
    private void changeView(int x, int y){
        TileView tt = (TileView) findViewById(ids[x][y]);
        int newIndex = tt.getNewIndex();
        values[x][y] = newIndex;
        tt.setBackgroundResource(pictures[newIndex]);
        tt.invalidate();
    }

    /**
     * Comprueba que la partida haya acabado,
     * es decir que todas las casillas compartan número o color,
     * devolviendo verdadero o falso si se ha acabado la partida.
     * @return Boolean - Si ha acabado la partida o no.
     */
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


    /**
     * Da por ganado el juego:
     * Guarda en la memoria del móvil el nombre de usuario, fecha y pulsaciones realizadas.
     * Después devolvuelve los mismos a la actividad principal.
     * Por ultimo, finaliza esta actividad.
     */
    private void gameWon(){
        Intent resultIntent = new Intent();
        Log.i("GAME_FINISHED", "User won the game");
        //Guarda al ganar
        long timeInSeconds= (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;
        saveWinner(numberOfClicks, getIntent().getStringExtra("username"),
                String.valueOf(timeInSeconds) + "s", getIntent().getBooleanExtra("saveToSD", false));
        resultIntent.putExtra("clicks", numberOfClicks);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    /**
     * Sobrescerito el callback lanzado cuando se pulsa el botón físico "atrás".
     */
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
     * @param storageType int tipo de guardado que quiere el jugador. False = Interno, True = SD
     */
    private void saveWinner(int numberOfClicks, String playerName, String investedTime, boolean storageType) {
        String fileName = "results.txt";
        FileOutputStream fOS = null;
        String data = numberOfClicks + "," + playerName + "," + investedTime + System.getProperty("line.separator");
        try {
            if(!storageType) {
                File file = new File(this.getFilesDir(), fileName);
                if(!file.exists()) file.createNewFile();
                fOS = new FileOutputStream(file, true);
                fOS.write(data.getBytes());
                Toast.makeText(this, "Data of the player saved", Toast.LENGTH_SHORT).show();
            } else {
                if(isExternalStorageWritable()) {
                    File file = new File(getExternalFilesDir("flipi") + File.separator + fileName);
                    if(!file.exists()) file.createNewFile();
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

    /**
     * Callback llamado cuando se selecciona un item del menú.
     * Puede o bien salir del juego o completamente de la aplicación.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(this, MainActivity.class);
        switch (item.getItemId()) {
            case R.id.itemExitGame:
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.putExtra("resumegame", "resumegame");
                startActivity(intent);
                return true;
            case R.id.itemExitApp:
                mp.release();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("exitApp", "exitApp");
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Callback llamado cuando se mata a la actividad.
     * Guarda el cronómetro, las pulsaciones, los valores e ids de las casillas.
     * @param savedInstanceState: Bundle - recibido de forma automatica. Usado para generar mi bundle.
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putLong("Chronometer", chronometer.getBase());
        savedInstanceState.putInt("Presses", numberOfClicks);
        savedInstanceState.putIntArray("Values", matrixToArray(values));
        savedInstanceState.putIntArray("Ids", matrixToArray(ids));
    }
    /**
     * Callback llamado cuando se restaura la actividad.
     * Recoge el cronómetro, las pulsaciones, los valores e ids de las casillas.
     * @param savedInstanceState: Bundle - recibido de forma automatica. Usado para obtener mis datos.
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        chronometer.setBase(savedInstanceState.getLong("Chronometer"));
        chronometer.start();
        numberOfClicks = savedInstanceState.getInt("Presses");
        tvNumberOfClicks.setText(String.valueOf(numberOfClicks));
        values = arrayToMatrix(savedInstanceState.getIntArray("Values"), values.length, values[0].length);
        ids = arrayToMatrix(savedInstanceState.getIntArray("Ids"), ids.length, ids[0].length);
        updateViews();
    }

    /**
     * Aplica de forma recursiva la función updateView-
     */
    private void updateViews() {
        for (int i = 0; i < values.length; i++) {
            for (int j = 0; j < values[i].length; j++) {
                updateView(i, j);
            }
        }
    }

    /**
     * Ajusta el fondo de las casillas por el correcto.
     * Ya que cuando se rota el dispositivo se generan aleatoriamente.
     * @param x: int - Ubicación del elmento en el eje horizontal.
     * @param y: int - Ubicación del elmento en el eje vertical.
     */
    private void updateView(int x, int y){
        TileView tt = (TileView) findViewById(ids[x][y]);
        tt.setBackgroundResource(pictures[values[x][y]]);
        tt.invalidate();
    }

    /**
     * Convierte la matriz dada en un array unidimensional.
     * @param matrix: int[][] - Matriz a ser transformada.
     * @return int[] - Array unidimensional generado en base a la matriz dada.
     */
    private int[] matrixToArray(int[][] matrix){
        int array[] = new int[matrix.length*matrix[0].length];
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix[i].length; j++) {
                array[i * matrix[i].length + j] = matrix[i][j];
            }
        }
        return array;
    }

    /**
     * Convierte el array unidimensional dado en una matriz de x e y dimensiones.
     * @param array: int[] - Array unidimensional a ser transformado.
     * @param y: int - Elementos en la vertical.
     * @param x: int - Elementos en la horizontal.
     * @return int[][] - Matriz generada en base a los parámetros de entrada.
     */
    private int[][] arrayToMatrix(int[] array, int y, int x) {
        int[][] matrix = new int[x][y];
        for (int row = 0; row < y; row++) {
            for (int column = 0; column < x; column++) {
                matrix[row][column] = array[row * x + column];
            }
        }
        return matrix;
    }
}
