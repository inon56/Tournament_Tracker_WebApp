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
import java.util.List;

@WebServlet("/tournamentServlet")
public class TournamentServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        System.out.println("tournamentServlet get method called");

        List<String> teams = SQLConnector.getTeam_All();
        List<String> prizes = SQLConnector.getPrizes_All();
        CreateTournamentResponse data = new CreateTournamentResponse(teams,prizes);

        Gson gson = new Gson();
        String jsonString = gson.toJson(data.toString());

        PrintWriter out = res.getWriter();
        res.setContentType("application/json;charset=utf-8");
        out.print(jsonString);
        out.close();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
            System.out.println(sb);
        }
        reader.close();

        Gson gson = new Gson();
        String jsonString = "{\"tournamentName\": NBA, \"entryFee\": 50, \"enteredTeams\": [lakers,dodgers,bools], \"enteredPrizes\": [first,second]}";
        CreateTournamentRequest request = gson.fromJson(jsonString, CreateTournamentRequest.class);

        // TODO:
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
        tournament.setTournamentName(request.getTournamentName());
        tournament.setEntryFee(request.getEntryFee());

        // TODO: convert the string to TeamModel and PrizeModel
//        tournament.setEnteredTeams(request.getEnteredTeams());
//        tournament.setPrizes((request.getPrizes()));


        TournamentLogic.createRounds(tournament);
//        SQLConnector.createTournament(model);
    }
}
