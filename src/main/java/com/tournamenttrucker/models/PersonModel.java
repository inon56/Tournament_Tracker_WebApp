package com.tournamenttrucker.models;


public class PersonModel {

    // The unique identifier for the person
    private int id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String cellphoneNumber;

    public PersonModel() {
    }

    public PersonModel(String firstName, String lastName, String emailAddress, String cellPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.cellphoneNumber = cellPhoneNumber;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setCellphoneNumber(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }
    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public void setId(int id) {
        this.id = id;
    }


}
