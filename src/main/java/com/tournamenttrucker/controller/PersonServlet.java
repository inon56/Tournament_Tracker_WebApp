package com.tournamenttrucker.controller;

import com.google.gson.Gson;
import com.tournamenttrucker.contracts.CreatePersonRequest;
import com.tournamenttrucker.dataAccess.SQLConnector;
import com.tournamenttrucker.models.PersonModel;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(value = "/personServlet")
public class PersonServlet extends HttpServlet {
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
        CreatePersonRequest personRequest = gson.fromJson(sb.toString(), CreatePersonRequest.class);

        // validate the person details
        if (!validateInput(res, personRequest))
            return;

        // create person from the CreatePersonRequest
        PersonModel person = new PersonModel(
                personRequest.getFirstName(),
                personRequest.getLastName(),
                personRequest.getEmailAddress(),
                personRequest.getCellphoneNumber());

        SQLConnector.createPerson(person);

        PrintWriter out = res.getWriter();
        res.setContentType("application/text;charset=utf-8");
        out.print("Person created");
        out.close();
    }

    private static boolean validateInput(HttpServletResponse res, CreatePersonRequest personRequest) throws IOException {
        String nameRegex = "^[a-zA-Z]*$";
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        String digitsRegex = "[0-9]+";
        String badParameter = null;

        if (!(personRequest.getFirstName().matches(nameRegex)))
            badParameter = "invalid";
        if (!(personRequest.getLastName().matches(nameRegex)))
            badParameter = "invalid";
        if (!(personRequest.getEmailAddress().matches(emailRegex)))
            badParameter = "invalid";
        if (!(personRequest.getCellphoneNumber().matches(digitsRegex)))
            badParameter = "invalid";

        if (badParameter != null)
        {
            res.sendError(400);
            return false;
        }

        return true;
    }
}
