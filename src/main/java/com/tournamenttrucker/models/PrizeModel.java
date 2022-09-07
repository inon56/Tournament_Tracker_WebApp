package com.tournamenttrucker.models;


public class PrizeModel {

        // The unique identifier for the prize
        private transient int id;
        private int placeNumber;
        private String placeName;
        private double prizeAmount; // decimal
        private double prizePercentage;

    public PrizeModel() {
    }

    public PrizeModel(int placeNumber, String placeName, double prizeAmount) {
        this.placeNumber = placeNumber;
        this.placeName = placeName;
        this.prizeAmount = prizeAmount;
    }

    public PrizeModel(int placeNumber, String placeName, double prizeAmount, double prizePercentage) {
        this.placeNumber = placeNumber;
        this.placeName = placeName;
        this.prizeAmount = prizeAmount;
        this.prizePercentage = prizePercentage;
    }

    @Override
    public String toString() {
        return
                "{ placeNumber=" + placeNumber +
                ", placeName='" + placeName + '\'' +
                ", prizeAmount=" + prizeAmount +
                '}';
    }

    public PrizeModel(String placeName, String placeNumber, String prizeAmount, String prizePercentage)
        {
            this.placeName = placeName;

            int placeNumberValue = 0;
            //int.TryParse(placeNumber, out placeNumberValue);
            this.placeNumber = placeNumberValue;

            double prizeAmountValue = 0;
            //double.TryParse(prizeAmount, out prizeAmountValue);
            this.prizeAmount = prizeAmountValue;

            double prizePercentageValue = 0;
            //double.TryParse(prizePercentage, out prizePercentageValue);
            this.prizePercentage = prizePercentageValue;
        }

    public int getId() {
        return id;
    }

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

    public void setId(int id) {
        this.id = id;
    }

}
