package com.example.kevinbristowoption3;

public class WeightInput {
    private String date;
    private int weight;

    public WeightInput(String date, int weight) {
        this.date = date;
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }

    public int getWeight() {
        return weight;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

