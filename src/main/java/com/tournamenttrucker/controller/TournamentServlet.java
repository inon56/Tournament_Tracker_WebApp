package com.tournamenttrucker.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tournamenttrucker.TournamentLogic;
import com.tournamenttrucker.contracts.CreatePersonRequest;
import com.tournamenttrucker.contracts.CreateTournamentRequest;
import com.tournamenttrucker.contracts.CreateTournamentResponse;
import com.tournamenttrucker.dataAccess.SQLConnector;
import com.tournamenttrucker.models.TeamModel;
import com.tournamenttrucker.models.TournamentModel;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebServlet(value = "/tournamentServlet")
public class TournamentServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        List<String> teams = SQLConnector.getTeamAll();
        CreateTournamentResponse tournamentResponse = new CreateTournamentResponse(teams);

        Gson gson = new Gson();
        String jsonString = gson.toJson(tournamentResponse);


        PrintWriter out = res.getWriter();
        res.setContentType("application/json;charset=utf-8");
        out.print(jsonString);
        out.close();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException
    {
        System.out.println("tournament servlet get method");

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            System.out.println(sb);
        }
        reader.close();

        Gson gson = new Gson();
        String jsonString = "{\"tournamentName\": NBA, \"entryFee\": 50, \"enteredTeams\": [lakers,dodgers,bulls], \"enteredPrizes\": [first,second]}";
        CreateTournamentRequest tournamentRequest = gson.fromJson(jsonString, CreateTournamentRequest.class);

        System.out.println(tournamentRequest);

        SQLConnector.createTournament(tournamentRequest);


        // 1 - get all teams id from DB with the same name as the enteredTeams
        // 2 - get all prizes id from DB with the same name as the enteredPrizes
        // 3 - call SQLConnector spPerson_GetByTournament
        // ? - call and assigned to  SQLConnector spPrizes_GetByTournament
        // ? - call and assigned to  SQLConnector spPerson_GetByTournament
        // 5 - tournament entries insert
        // 6 - tournament prizes insert


//        List<TeamModel> teams = new List<TeamModel>() {
//            // select prizes with
//        }
        List<TeamModel> teams = new ArrayList<>();

        TournamentModel tournament = new TournamentModel();
        tournament.setTournamentName(tournamentRequest.getTournamentName());
        tournament.setEntryFee(tournamentRequest.getEntryFee());

        // TODO: convert the string to TeamModel and PrizeModel
//        tournament.setEnteredTeams(tournamentRequest.getEnteredTeams());


//        TournamentLogic.createRounds(tournament);
//        SQLConnector.createTournament(model);



        PrintWriter out = res.getWriter();
        res.setContentType("application/text;charset=utf-8");
        out.print("Tournament created");
        out.close();
    }
}
