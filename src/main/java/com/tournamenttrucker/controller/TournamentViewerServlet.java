package com.tournamenttrucker.controller;

import com.google.gson.Gson;
import com.tournamenttrucker.TournamentLogic;
import com.tournamenttrucker.contracts.MatchupResult;
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
import java.util.List;

@WebServlet(value = "/tournamentViewerServlet")
public class TournamentViewerServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String tournamentName = req.getParameter("tournamentName");
        int tournamentId = SQLConnector.getTournamentIdByName(tournamentName);
        TournamentModel tournament = SQLConnector.getTournamentById(tournamentId);
        int currentRound = tournament.getCurrentRound();

        // return the matchups of tournament currnet round
        List<MatchupTeamsCompeting> matchupTeamsCompeting = SQLConnector.getCurrentRoundMatchupsTeamsNames(tournamentId, currentRound);

        TournamentRoundResponse tournamentRoundResponse = new TournamentRoundResponse(tournamentName, currentRound, matchupTeamsCompeting);
        Gson gson = new Gson();
        String jsonString = gson.toJson(tournamentRoundResponse);

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
        SubmitRoundResultRequest roundRequest = gson.fromJson(sb.toString(), SubmitRoundResultRequest.class);

        if (!(validateInput(res, roundRequest)))
            return;

        int tournamentId = SQLConnector.getTournamentIdByName(roundRequest.getTournamentName());
        // check if tournament name exist
        if (tournamentId == 0) {
            res.sendError(400);
            return;
        }

        TournamentModel tournament = SQLConnector.getTournamentById(tournamentId);

        int currentRound = roundRequest.getRound();
        // check if round input is match to the current tournament round
        if (tournament.getCurrentRound() != currentRound){
            res.sendError(400);
            return;
        }

        if (!(checkInputInDB(res, tournamentId, currentRound, roundRequest.getMatchupsResults())))
            return;

        // update matchup results with scores, and update the winnerId
        SQLConnector.updateMatchupsResult(roundRequest.getMatchupsResults());

        String output = "";
        // tournament completed
        if (roundRequest.getMatchupsResults().size() == 1)
        {
            output = "Tournament " + tournament.getTournamentName() + " completed";
            // get teams in this matchup and give out prizes
            TournamentLogic.completeTournament(tournament, currentRound);
        } else {
            output = "Round: " + tournament.getCurrentRound() + " completed";
            SQLConnector.incrementTournamentRound(tournament.getId());
            TournamentLogic.createNextRound(tournament.getId());
        }

        PrintWriter out = res.getWriter();
        res.setContentType("application/text;charset=utf-8");
        out.print(output);
        out.close();
    }

    private static boolean checkInputInDB(HttpServletResponse res, int tournamentId, int round, List<MatchupResult> matchupResults) throws IOException
    {
        String badParameter = null;
        
        if (!SQLConnector.checkMatchupsExist(tournamentId, round, matchupResults))
            badParameter = "invalid";

        if (badParameter != null)
        {
            res.sendError(400);
            return false;
        }

        return true;
    }
    private static boolean validateInput(HttpServletResponse res, SubmitRoundResultRequest roundRequest) throws IOException
    {
        String nameRegex = "^[a-zA-Z]*$";
        String badParameter = null;

        if (!(roundRequest.getTournamentName().matches(nameRegex)))
            badParameter = "invalid";

        if (badParameter != null)
        {
            res.sendError(400);
            return false;
        }

        return true;
    }
}
