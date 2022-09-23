package com.tournamenttrucker.controller;

import com.google.gson.Gson;
import com.tournamenttrucker.contracts.PersonResponse;
import com.tournamenttrucker.contracts.CreateTeamRequest;
import com.tournamenttrucker.dataAccess.SQLConnector;
import com.tournamenttrucker.models.PersonModel;

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
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
    {
        List<PersonModel> playersList = SQLConnector.getAllPerson();
        List<PersonResponse> playersResponse = new ArrayList<>();

        // convert to personResponse
        for (PersonModel player : playersList){
            playersResponse.add(new PersonResponse(player.getFirstName(), player.getLastName(), player.getEmailAddress()));
        }

        Gson gson = new Gson();
        String jsonString = gson.toJson(playersResponse);

        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        out.write(jsonString);
        out.close();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException
    {
        System.out.println("the team servlet post method called");

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        System.out.println(sb.toString());
        Gson gson = new Gson();
        CreateTeamRequest team = gson.fromJson(sb.toString(), CreateTeamRequest.class);

        // validate team name
        String nameRegex = "^[a-zA-Z]*$";
        if (!(team.getTeamName().matches(nameRegex)))
            System.out.println("Invalid team name");

        String teamName = team.getTeamName();
        List<String > teamMembers = team.getTeamMembers();

        SQLConnector.createTeam(teamName, teamMembers);

        PrintWriter out = res.getWriter();
        res.setContentType("application/text;charset=utf-8");
        out.print("Team created");
        out.close();
    }
}
