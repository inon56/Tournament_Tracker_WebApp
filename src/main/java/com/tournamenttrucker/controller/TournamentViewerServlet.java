package com.tournamenttrucker.controller;

import com.google.gson.Gson;
import com.tournamenttrucker.contracts.CreateRoundGameRequest;
import com.tournamenttrucker.contracts.CreateTournamentRequest;
import com.tournamenttrucker.contracts.CreateTournamentResponse;
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
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/tournamentViewerServlet")
public class TournamentViewerServlet {
    private TournamentModel tournamentModel;

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        System.out.println("tournament viewer servlet get method called");

        CreateRoundGameRequest data = new CreateRoundGameRequest();

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
        }
        reader.close();

        // "tournamentName": tournamentName, "game": }
        Gson gson = new Gson();
        String jsonString = "{\"tournamentName\": NBA, \"game\": {\"teamOne\": \"aaa\", \"teamTwo\": \"bbb\", \"teamOneScore\": 2, \"teamTwoScore\": 5";
        CreateRoundGameRequest game = gson.fromJson(jsonString, CreateRoundGameRequest.class);



//        MatchupModel matchupModel = new MatchupModel();
//
//        for (int i = 0; i < matchupModel.getEntries().size(); i++)
//        {
//            if (i == 0)
//                if (matchupModel.getEntries().get(0).getTeamCompeting() != null)
//                    matchupModel.getEntries().get(0).setScore(teamOneScoreNum);
//
//            if (i == 1)
//                if (matchupModel.getEntries().get(1).getTeamCompeting() != null)
//                    matchupModel.getEntries().get(1).setScore(teamTwoScoreNum);
//        }
//
//        // need to send from html the winning team
//        String winningTeam = req.getParameter("winningTeam");
//        int winningTeamNum = Integer.parseInt(winningTeam);
//
//        // Team one wins
//        if (winningTeamNum == 1)
//            matchupModel.setWinner(matchupModel.getEntries().get(0).getTeamCompeting());
//
//        else if (winningTeamNum == 2)
//            matchupModel.setWinner(matchupModel.getEntries().get(1).getTeamCompeting());
//
//
//        for (List<MatchupModel> round : tournamentModel.getRounds())
//        {
//            for (MatchupModel rm : round) // rm stands for round matchup
//            {
//                for (MatchupEntryModel me : rm.getEntries())
//                {
//                    if (me.getParentMatchup() != null)
//                    {
//                        if (me.getParentMatchup().getId() == rm.getId())
//                        {
//                            me.setTeamCompeting(matchupModel.getWinner());
//                            SQLConnector.updateMatchup(rm);
//                        }
//                    }
//                }
//            }
//        }



        // LoadMatchups((int)roundDropDown.SelectedItem);
        // SQLConnector.updateMatchup(matchupModel);

    }
}
