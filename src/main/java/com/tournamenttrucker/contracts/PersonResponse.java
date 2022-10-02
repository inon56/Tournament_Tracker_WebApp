package com.tournamenttrucker.contracts;

public class PersonResponse {
    private String firstName;
    private String lastName;
    private String emailAddress;


    public PersonResponse(String firstName, String lastName, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

}
