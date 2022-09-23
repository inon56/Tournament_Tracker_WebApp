package com.tournamenttrucker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tournamenttrucker.contracts.CreatePersonRequest;
import com.tournamenttrucker.contracts.CreateTeamRequest;
import com.tournamenttrucker.contracts.PersonResponse;
import com.tournamenttrucker.contracts.TournamentResponse;
import com.tournamenttrucker.dataAccess.SQLConnector;
import com.tournamenttrucker.models.PersonModel;
import com.tournamenttrucker.models.TeamModel;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        int tournamentId = SQLConnector.getTournamentIdByName("NBA");
        System.out.println(tournamentId);





//        EmailLogic.sendEmail();

    }
}
