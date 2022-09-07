package com.tournamenttrucker.controller;

import com.google.gson.Gson;
import com.tournamenttrucker.dataAccess.SQLConnector;
import com.tournamenttrucker.models.PersonModel;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;


@WebServlet(value = "/personServlet")
public class PersonServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        System.out.println("person servlet get");
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        System.out.println("person servlet post");
        Gson gson = new Gson();
        PersonModel person = new PersonModel();

        // option 1:
//        BufferedReader reader = req.getReader();
//        String line = reader.readLine();
//        if (line != null)
//            person = gson.fromJson(line, PersonModel.class);
//
//        // option 2:
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = req.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();

        person = gson.fromJson(sb.toString(), PersonModel.class);

        System.out.println(person.toString());





        SQLConnector.createPerson(person);
    }

}
