package com.tournamenttrucker.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tournamenttrucker.TournamentLogic;
import com.tournamenttrucker.contracts.CreateTournamentRequest;
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
import java.util.List;

@WebServlet("/tournamentServlet")
public class TournamentServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        System.out.println("tournamentServlet get method called");

        List<TeamModel> teams = SQLConnector.getTeam_All();
        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(teams);

        PrintWriter out = res.getWriter(); // maybe I should try and catch
        res.setContentType("application/json;charset=utf-8");
//        out.print(teamString);
//        out.print(prizeString);
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
        String jsonString = "{\"tournamentName\": NBA, \"entryFee\": 50, \"enteredTeams\": [lakers,dodgers,bools], \"prizes\": [first,second]}";
        CreateTournamentRequest request = gson.fromJson(jsonString, CreateTournamentRequest.class);

//        List<TeamModel> teams = new List<TeamModel>() {
//            // select prizes with
//        }

        TournamentModel tournament = new TournamentModel();
        tournament.setTournamentName(request.tournamentName);

        TournamentLogic.CreateRounds(tournament);
//        SQLConnector.createTournament(model);
    }
}
