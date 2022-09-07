package com.tournamenttrucker.controller;

import com.google.gson.Gson;
import com.tournamenttrucker.dataAccess.SQLConnector;
import com.tournamenttrucker.models.MatchupEntryModel;
import com.tournamenttrucker.models.MatchupModel;
import com.tournamenttrucker.models.TournamentModel;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet("/tournamentViewerServlet")
public class TournamentViewerServlet {
    private TournamentModel tournamentModel;

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        String teamOneScore = req.getParameter("teamOneScore");
        double teamOneScoreNum = Double.parseDouble(teamOneScore);
        String teamTwoScore = req.getParameter("teamTwoScore");
        double teamTwoScoreNum = Double.parseDouble(teamTwoScore);
        String tournamentName = req.getParameter("tournamentName");

        TournamentModel tournament;
        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        tournament = gson.fromJson(sb.toString(), TournamentModel.class);

        System.out.println(tournament.getTournamentName());

        MatchupModel matchupModel = new MatchupModel();

        for (int i = 0; i < matchupModel.getEntries().size(); i++)
        {
            if (i == 0)
                if (matchupModel.getEntries().get(0).getTeamCompeting() != null)
                    matchupModel.getEntries().get(0).setScore(teamOneScoreNum);

            if (i == 1)
                if (matchupModel.getEntries().get(1).getTeamCompeting() != null)
                    matchupModel.getEntries().get(1).setScore(teamTwoScoreNum);
        }

        // need to send from html the winning team
        String winningTeam = req.getParameter("winningTeam");
        int winningTeamNum = Integer.parseInt(winningTeam);

        // Team one wins
        if (winningTeamNum == 1)
            matchupModel.setWinner(matchupModel.getEntries().get(0).getTeamCompeting());

        else if (winningTeamNum == 2)
            matchupModel.setWinner(matchupModel.getEntries().get(1).getTeamCompeting());


        for (List<MatchupModel> round : tournamentModel.getRounds())
        {
            for (MatchupModel rm : round) // rm stands for round matchup
            {
                for (MatchupEntryModel me : rm.getEntries())
                {
                    if (me.getParentMatchup() != null)
                    {
                        if (me.getParentMatchup().getId() == rm.getId())
                        {
                            me.setTeamCompeting(matchupModel.getWinner());
                            SQLConnector.updateMatchup(rm);
                        }
                    }
                }
            }
        }

        //LoadMatchups((int)roundDropDown.SelectedItem);

        SQLConnector.updateMatchup(matchupModel);
    }


}
