package com.tournamenttrucker.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tournamenttrucker.contracts.CreatePersonResponse;
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
import java.util.List;

@WebServlet(value = "/teamServlet")
public class TeamServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
    {
        List<CreatePersonResponse> playersList = SQLConnector.getPersonAll();
        Gson gson = new Gson();
        String jsonString = gson.toJson(playersList);

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

        Gson gson = new Gson();
        CreateTeamRequest team = gson.fromJson(sb.toString(), CreateTeamRequest.class);
//        System.out.println(team);
        SQLConnector.createTeam(team);

        PrintWriter out = res.getWriter();
        res.setContentType("application/text;charset=utf-8");
        out.print("Team created");
        out.close();
    }
}
