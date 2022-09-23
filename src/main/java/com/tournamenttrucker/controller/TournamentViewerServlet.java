package com.tournamenttrucker.controller;

import com.google.gson.Gson;
import com.tournamenttrucker.TournamentLogic;
import com.tournamenttrucker.contracts.MatchupTeamsCompeting;
import com.tournamenttrucker.contracts.SubmitRoundResultRequest;
import com.tournamenttrucker.contracts.TournamentRoundResponse;
import com.tournamenttrucker.dataAccess.SQLConnector;
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

@WebServlet(value = "/tournamentViewerServlet")
public class TournamentViewerServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        System.out.println("tournament viewer servlet get method called");

        String tournamentName = req.getParameter("tournamentName");
        int tournamentId = SQLConnector.getTournamentIdByName(tournamentName);
        TournamentModel tournament = SQLConnector.getTournamentById(tournamentId);
        int currentRound = tournament.getCurrentRound();

        // return the matchups of tournament currnet round
        List<MatchupTeamsCompeting> matchupTeamsCompeting = SQLConnector.getCurrentRoundMatchupsTeamsNames(tournamentId, currentRound);

        TournamentRoundResponse tournamentRoundResponse = new TournamentRoundResponse(tournamentName, currentRound, matchupTeamsCompeting);
        Gson gson = new Gson();
        String jsonString = gson.toJson(tournamentRoundResponse);

//        System.out.println("current round: " + currentRound + " tournamentId: " + tournamentId);
//        System.out.println(jsonString);

        PrintWriter out = res.getWriter();
        res.setContentType("application/json;charset=utf-8");
        out.print(jsonString);
        out.close();
    }

    // Submit result of current round
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException
    {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        Gson gson = new Gson();
        SubmitRoundResultRequest request = gson.fromJson(sb.toString(), SubmitRoundResultRequest.class);

        SQLConnector.updateMatchupsResult(request.getMatchupResults());

        // TODO: validate tournamentId
        int tournamentId = SQLConnector.getTournamentIdByName(request.getTournamentName());
        TournamentModel tournament = SQLConnector.getTournamentById(tournamentId);
        if (tournament.getCurrentRound() == TournamentLogic.numberOfTotalRounds) // tournament completed
        {
            List<String> teamsNames = new ArrayList<>();
            TournamentLogic.completeTournament(tournament, teamsNames);
        } else {
            SQLConnector.incrementTournamentRound(tournamentId);
            TournamentLogic.createNextRound(tournamentId);
        }

        PrintWriter out = res.getWriter();
        res.setContentType("application/text;charset=utf-8");
        out.print("Round completed");
        out.close();
    }
}
