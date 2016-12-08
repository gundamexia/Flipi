package com.example.dam212.flipi;

import android.content.Context;
import android.widget.Button;

public class TileView extends Button {
    // coordenadas
    public int x = 0;
    public int y = 0;
    // trama a mostrar
    private int index = 0;
    //max tramas
    private int topElements = 0;

    public TileView(Context context, int x, int y, int topElements, int index, int background) {
        super(context);
        this.x = x; //coordenada X
        this.y = y; //coordenada Y
        this.topElements = topElements; //max tramas
        this.index = index; //índice de trama
        this.setBackgroundResource(background);
    }
    public int getNewIndex(){
        index ++;
        //controlar si necesitamos volver a comenzar el ciclo de tramas
        if (index == topElements)index = 0;
        return index;
    }
}
