package com.tournamenttrucker;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tournamenttrucker.contracts.*;
import com.tournamenttrucker.dataAccess.SQLConnector;
import com.tournamenttrucker.models.PersonModel;
import com.tournamenttrucker.models.TeamModel;

import java.sql.Driver;
import java.sql.DriverManager;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        List<PersonModel> players = SQLConnector.getAllTeamMembers(1029);
        players.forEach((val) -> System.out.println(val.getFirstName()));
        Map<Integer,Double> map = TournamentLogic.calculatePrizePayout(100,10,2);
        for (Map.Entry<Integer,Double> entry : map.entrySet())
        {
            System.out.println("key: " + entry.getKey() + " val: " + entry.getValue());
        }
//        EmailLogic.sendEmail("fifa", "lakers", players, 100.0);
    }
}
