package com.tournamenttrucker.controller;

import com.google.gson.Gson;
import com.tournamenttrucker.contracts.CreatePersonRequest;
import com.tournamenttrucker.contracts.CreatePrizeRequest;
import com.tournamenttrucker.dataAccess.SQLConnector;
import com.tournamenttrucker.models.PrizeModel;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(value = "/prizeServlet")
public class PrizeServlet extends HttpServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException
    {

        Gson gson = new Gson();
        PrizeModel p1 = new PrizeModel(1,"aaa",10 , 5);
        PrizeModel p2 = new PrizeModel(2,"second",20,3);
        String jsonString = gson.toJson(p1);
        System.out.println(jsonString);

        PrintWriter out = res.getWriter();
        res.setContentType("application/json");
        out.write(jsonString);
        out.close();
    }
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException
    {
        System.out.println("prize post");

        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        Gson gson = new Gson();
        CreatePrizeRequest prize = gson.fromJson(sb.toString(), CreatePrizeRequest.class);

//        SQLConnector.createPrize(prize);
    }




    public void destroy()
    {
        System.out.println("servlet is destroyed now");
    }
}
