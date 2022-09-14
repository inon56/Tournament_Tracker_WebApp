package com.tournamenttrucker.contracts;

public class CreatePrizeRequest {
    private int placeNumber;
    private String placeName;
    private double prizeAmount; // decimal
    private double prizePercentage;

    public int getPlaceNumber() {
        return placeNumber;
    }

    public String getPlaceName() {
        return placeName;
    }

    public double getPrizeAmount() {
        return prizeAmount;
    }

    public double getPrizePercentage() {
        return prizePercentage;
    }
}
