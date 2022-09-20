package com.tournamenttrucker;

import com.google.gson.Gson;
import com.tournamenttrucker.contracts.CreatePersonRequest;
import com.tournamenttrucker.contracts.CreateTeamRequest;
import com.tournamenttrucker.dataAccess.SQLConnector;
import com.tournamenttrucker.models.PersonModel;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        Gson gson = new Gson();
        List<String> list = Arrays.asList("sdsd","two","three");
        CreateTeamRequest team = new CreateTeamRequest("fetchers", list);
        SQLConnector.createTeam(team);





//        List<PersonModel> playersList = SQLConnector.getPerson_All();
//        Gson gson = new Gson();
//        String jsonString2 = gson.toJson(playersList);
//        System.out.println(jsonString2);


//        EmailLogic.sendEmail();

    }
}
