package com.tournamenttrucker.controller;

import com.google.gson.Gson;
import com.tournamenttrucker.dataAccess.SQLConnector;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
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
        String tournamentName = gson.fromJson(sb.toString(), String.class);

        if (!validateInput(res, tournamentName))
            return;

        int tournamentId = SQLConnector.getTournamentIdByName(tournamentName);
        SQLConnector.deleteTournament(tournamentId);

        PrintWriter out = res.getWriter();
        res.setContentType("application/text;charset=utf-8");
        out.print("Tournament deleted");
        out.close();
    }

    private static boolean validateInput(HttpServletResponse res, String tournamentName) throws IOException {
        String nameRegex = "^[a-zA-Z]*$";
        String badParameter = null;

        if (!tournamentName.matches(nameRegex))
            badParameter = "invalid";

        if (badParameter != null)
        {
            res.sendError(400);
            return false;
        }

        return true;
    }
}
