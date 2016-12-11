package com.example.dam212.flipi;

/**
 * Elemento contenedor de la información necesaria en la clase Scores.
 * Variables internas:
 * name: String - Nombre del usuario.
 * date: String - Fecha actual.
 * score: String - Cantidad de pulsaciones realizadas.
 */
public class ListItem {

    private String name, date, score;

    /**
     * Constructor de la clase, que instanciada, constituye el elemento básico de nuestro ListView.
     * @param name String - Nombre del usuario.
     * @param date String - Tiempo de finalización.
     * @param score String - Cantidad de pulsaciones realizadas.
     */
    public ListItem(String name, String date, String score){
        setName(name);
        setDate(date);
        setScore(score);
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getScore() {
        return score;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(String score) {
        this.score = score;
    }

}
