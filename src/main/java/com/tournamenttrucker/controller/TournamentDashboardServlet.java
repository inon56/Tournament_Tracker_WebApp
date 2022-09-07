package com.tournamenttrucker.controller;

import com.google.gson.Gson;
import com.tournamenttrucker.dataAccess.SQLConnector;
import com.tournamenttrucker.models.TournamentModel;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/tournamentDashboardServlet")
public class TournamentDashboardServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        System.out.println("tournament dashboard servlet get method called");
        Gson gson = new Gson();
//        List<TournamentModel> tournaments =  SQLConnector.getTournament_All();
//        String jsonString = gson.toJson(tournaments);

        String jsonString = "sfsf";
        System.out.println(jsonString);

        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        out.write(jsonString);
        out.close();
    }
}
