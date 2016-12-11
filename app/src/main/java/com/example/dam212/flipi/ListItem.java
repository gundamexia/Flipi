package com.example.dam212.flipi;

public class ListItem {

    private String name, date, time, score;

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

    public String getTime() {
        return time;
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

    public void setTime(String time) {
        this.time = time;
    }
}
