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
        List<PersonModel> playersList = SQLConnector.getAllAvailablePerson();
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
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        Gson gson = new Gson();
        CreateTeamRequest teamRequest = gson.fromJson(sb.toString(), CreateTeamRequest.class);

        if (!validateCreateTeam(res, teamRequest))
            return;

        String teamName = teamRequest.getTeamName();
        List<String > teamMembers = teamRequest.getTeamMembersEmails();

        SQLConnector.createTeam(teamName, teamMembers);

        PrintWriter out = res.getWriter();
        res.setContentType("application/text;charset=utf-8");
        out.print("Team created");
        out.close();
    }

    public static boolean validateCreateTeam(HttpServletResponse res, CreateTeamRequest teamRequest) throws IOException {
        String nameRegex = "^[a-zA-Z]*$";
        String badParameter = null;

        if (!(teamRequest.getTeamName().matches(nameRegex)))
            badParameter = "invalid";
        if (!SQLConnector.checkPlayersExist(teamRequest.getTeamMembersEmails()))
            badParameter = "invalid";

        if (badParameter != null)
        {
            res.sendError(400);
            return false;
        }

        return true;
    }
}
