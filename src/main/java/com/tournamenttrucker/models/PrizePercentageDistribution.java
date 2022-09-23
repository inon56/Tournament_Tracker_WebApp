package com.tournamenttrucker.models;

public class PrizePercentageDistribution {
    private int first;
    private int second;
//    private int third;

//    public PrizePercentageDistribution(int first, int second, int third) {
//        this.first = first;
//        this.second = second;
//        this.third = third;
//    }


    public PrizePercentageDistribution(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public void setSecond(int second) {
        this.second = second;
    }



    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }


}
