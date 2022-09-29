package com.tournamenttrucker.controller;

import com.google.gson.Gson;
import com.tournamenttrucker.TournamentLogic;
import com.tournamenttrucker.contracts.CreateTournamentRequest;
import com.tournamenttrucker.contracts.TournamentResponse;
import com.tournamenttrucker.dataAccess.SQLConnector;
import com.tournamenttrucker.PrizeGenerator;
import com.tournamenttrucker.models.TeamModel;
import com.tournamenttrucker.models.TournamentModel;
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
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
    {
        List<TeamModel> teams = SQLConnector.getAllAvailableTeams();
        List<String> teamsNames = new ArrayList<>();
        for (TeamModel team : teams) {
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
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        System.out.println(sb);
        Gson gson = new Gson();
        CreateTournamentRequest tournamentRequest = gson.fromJson(sb.toString(), CreateTournamentRequest.class);


        if (!validateInput(res, tournamentRequest))
            return;

        TournamentModel tournament = new TournamentModel();
        tournament.setTournamentName(tournamentRequest.getTournamentName());
        tournament.setEntryFee(tournamentRequest.getEntryFee());
        tournament.setPrizeOption(tournamentRequest.getPrizeOption());

        List<TeamModel> teamsEntered = new ArrayList<>();
        for (String teamName : tournamentRequest.getEnteredTeams())
        {
            teamsEntered.add(new TeamModel(teamName));
        }

        SQLConnector.createTournament(tournament, teamsEntered);
        TournamentLogic.createNextRound(tournament.getId());

        PrintWriter out = res.getWriter();
        res.setContentType("application/text;charset=utf-8");
        out.print("Tournament created");
        out.close();
    }

    private static boolean validateInput(HttpServletResponse res, CreateTournamentRequest tournamentRequest) throws IOException
    {
        String nameRegex = "^[a-zA-Z]*$";
        String badParameter = null;

        if (!tournamentRequest.getTournamentName().matches(nameRegex))
            badParameter = "invalid";
        if (tournamentRequest.getEntryFee() <= 0)
            badParameter = "invalid";
        if (!PrizeGenerator.getPrizeOptions().containsKey(tournamentRequest.getPrizeOption()))
            badParameter = "invalid";
        // check if the teams are in the DB
        if (!SQLConnector.checkTeamsExist(tournamentRequest.getEnteredTeams()))
            badParameter = "invalid";

        if (badParameter != null)
        {
            res.sendError(400);
            return false;
        }

        return true;
    }
}
