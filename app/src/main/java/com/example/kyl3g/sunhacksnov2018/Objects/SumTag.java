package com.example.kyl3g.sunhacksnov2018.Objects;

public class SumTag {
    private double sum = 0;
    private String store;

    public SumTag(){

    }

    public SumTag(String store, double sum){
        this.store = store;
        this.sum = sum;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public void addSum(double amount){
        this.sum += amount;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
}
