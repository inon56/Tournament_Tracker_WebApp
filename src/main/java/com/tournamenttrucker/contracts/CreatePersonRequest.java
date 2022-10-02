package com.tournamenttrucker.contracts;

public class CreatePersonRequest {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String cellphoneNumber;

    public CreatePersonRequest(String firstName, String lastName, String emailAddress, String cellphoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.cellphoneNumber = cellphoneNumber;
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

}
