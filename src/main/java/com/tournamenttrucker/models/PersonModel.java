package com.tournamenttrucker.models;

import com.google.gson.annotations.Expose;

public class PersonModel {

    // The unique identifier for the person
    private transient int id;
    private String firstName;
    private String lastName;
    private String emailAddress;
//    @Expose(serialize = true, deserialize = false)
    private String cellphoneNumber;

    public PersonModel() {
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

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private String fullName;

    public PersonModel(String firstName, String lastName, String emailAddress, String cellPhoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.cellphoneNumber = cellPhoneNumber;
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

    @Override
    public String toString() {
        return
                "{ firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", cellphoneNumber='" + cellphoneNumber + '\'' +
                '}';
    }

    public String getFullName() {
        return "";//$"{FirstName} {LastName}";;
    }

    public void setId(int id) {
        this.id = id;
    }


}
