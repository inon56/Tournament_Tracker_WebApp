package com.tournamenttrucker.controller;

import com.google.gson.Gson;
import com.tournamenttrucker.dataAccess.SQLConnector;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(value="/tournamentDashboardServlet")
public class TournamentDashboardServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
    {
        List<String> tournamentNames = SQLConnector.getAllActiveTournaments();
        Gson gson = new Gson();
        String jsonString = gson.toJson(tournamentNames);

        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        out.write(jsonString);
        out.close();
    }
}
