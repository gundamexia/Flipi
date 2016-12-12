package com.example.dam212.flipi;

import android.content.Context;
import android.widget.Button;

/**
 * Clase que abstrae la lógica de las casillas.
 * x: int - Posición horizontal del elemento.
 * y: int - Posición vertical del elemento.
 * topElements: int - El máximo de elementos.
 */
public class TileView extends Button {
    // coordenadas
    public int x = 0;
    public int y = 0;
    // trama a mostrar
    private int index = 0;
    //max tramas
    private int topElements = 0;

    /**
     *  constructor de la clase.
     * @param context Context - Contexto donde se desarollará la ejecución.
     * @param x int - Posición horizontal del elemento.
     * @param y int - Posición vertical del elemento.
     * @param topElements int - El máximo de elementos.
     * @param index int - Index de la trama a aplicar.
     * @param background int - Background a aplicar.
     */
    public TileView(Context context, int x, int y, int topElements, int index, int background) {
        super(context);
        this.x = x; //coordenada X
        this.y = y; //coordenada Y
        this.topElements = topElements; //max tramas
        this.index = index; //índice de trama
        this.setBackgroundResource(background);
    }

    /**
     * obtiene el siguiente index, controlando que no se salga del máximo.
     * @return int - El nuevo index, ya sea el inmediatamente siguiente o el primero de todos.
     */
    public int getNewIndex(){
        index ++;
        //controlar si necesitamos volver a comenzar el ciclo de tramas
        if (index == topElements)index = 0;
        return index;
    }
}
