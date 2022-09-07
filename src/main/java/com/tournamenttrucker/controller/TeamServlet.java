package com.tournamenttrucker.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tournamenttrucker.contracts.CreateTeamRequest;
import com.tournamenttrucker.dataAccess.SQLConnector;
import com.tournamenttrucker.models.PersonModel;
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

@WebServlet(value = "/teamServlet")
public class TeamServlet extends HttpServlet {

//    private List<PersonModel> availablePlayers = new ArrayList<>();
//    private List<PersonModel> teamPlayers = new ArrayList<>();

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        System.out.println("team servlet get");
        List<PersonModel> playersList = SQLConnector.getPerson_All();
        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(playersList);

        System.out.println(jsonString);

//        PrintWriter out = res.getWriter();
//        res.setContentType("application/json");
//        out.write(jsonString);
//        out.close();
    }
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        System.out.println("the team servlet post method called");

        Gson gson = new Gson();
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
            System.out.println(sb);
        }
        reader.close();

        CreateTeamRequest team = gson.fromJson(sb.toString(), CreateTeamRequest.class);

        System.out.println(team.getTeamName());

//        TeamModel model = new TeamModel(teamName, teamPlayers);
//        SQLConnector.createTeam(model);
    }


}
