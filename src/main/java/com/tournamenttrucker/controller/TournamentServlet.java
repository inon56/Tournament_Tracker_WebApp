package com.tournamenttrucker.controller;

import com.google.gson.Gson;
import com.tournamenttrucker.TournamentLogic;
import com.tournamenttrucker.contracts.CreateTournamentRequest;
import com.tournamenttrucker.contracts.TournamentResponse;
import com.tournamenttrucker.dataAccess.SQLConnector;
import com.tournamenttrucker.models.PrizeGenerator;
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

@WebServlet(value = "/tournamentServlet")
public class TournamentServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        System.out.println("tournament servlet get called");

        List<TeamModel> teams = SQLConnector.getAllAvailableTeams();
        List<String> teamsNames = new ArrayList<>();
        for (TeamModel team : teams) {
            System.out.println(team.getTeamName());
            teamsNames.add(team.getTeamName());
        }


        TournamentResponse tournamentResponse = new TournamentResponse(teamsNames);

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
        }
        reader.close();

//        sample: String jsonString = "{\"tournamentName\": NBA, \"entryFee\": 50, \"enteredTeams\": [lakers,dodgers,bulls,PSZ], \"prizeOption\": 1}";
        Gson gson = new Gson();
        CreateTournamentRequest tournamentRequest = gson.fromJson(sb.toString(), CreateTournamentRequest.class);

        try{
            PrizeGenerator.getByOption(tournamentRequest.getPrizeOption());
        } catch (Exception e){
            res.sendError(400, "Wrong option number");
        }

        TournamentModel tournament = new TournamentModel();
        tournament.setTournamentName(tournamentRequest.getTournamentName());
        tournament.setEntryFee(tournamentRequest.getEntryFee());
        tournament.setPrizeOption(tournamentRequest.getPrizeOption());

        List<TeamModel> teamsEntered = new ArrayList<>();
        for (String teamName : tournamentRequest.getEnteredTeams())
        {
            teamsEntered.add(new TeamModel(teamName));
        }

        SQLConnector.createTournament(tournament, teamsEntered); // called only once
        TournamentLogic.findNumberOfRounds(teamsEntered.size()); // TODO: adjust this
        TournamentLogic.createNextRound(tournament.getId());

        PrintWriter out = res.getWriter();
        res.setContentType("application/text;charset=utf-8");
        out.print("Tournament created");
        out.close();
    }
}
