package com.example.dam212.flipi;

import android.app.Activity;
import android.os.Bundle;

public class Flipi extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flipi);

        // Bundle que contiene todos los extras.
        Bundle configs = getIntent().getExtras();

        receiveTypeOfGame(configs);
    }

    /**
     * Recibe un Bundle con las variables de las opciones y después se los asigna a las variables
     * de esta clase.
     *
     * @param configs Bundle que contiene las opciones escogidas en el menú principal.
     */
    private void receiveTypeOfGame(Bundle configs) {
        int typeOfGame = configs.getInt("selectedGame");

        // Según el tipo de juego que se haya escogido se recogen unas variables u otras.
        switch (typeOfGame) {
            case 0:
                initFlip(configs.getInt("seekBarRows"), configs.getInt("seekBarColumns"), configs.getInt("seekBarMaxLoop"),
                        configs.getBoolean("checkBoxSound"), configs.getBoolean("checkBoxHaptic"), "color");
                break;
            case 1:
                initFlip(configs.getInt("seekBarRows"), configs.getInt("seekBarColumns"), configs.getInt("seekBarMaxLoop"),
                        configs.getBoolean("checkBoxSound"), configs.getBoolean("checkBoxHaptic"), "number");
                break;
            default:
                break;
        }
    }

    /**
     * Inicia el juego Flip
     * @param rows int, número de filas
     * @param columns int, número de columnas
     * @param maxLoops int, número máximo de bucles
     * @param sound boolean, para tener o no sonido
     * @param haptic boolean, para que vibre o no
     * @param typeOfFlip String, cómo se representarán las casillas
     */
    private void initFlip(int rows, int columns, int maxLoops, boolean sound, boolean haptic, String typeOfFlip) {

    }
}
