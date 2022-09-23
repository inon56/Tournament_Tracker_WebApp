package com.tournamenttrucker.controller;

import com.google.gson.Gson;
import com.tournamenttrucker.contracts.CreatePersonRequest;
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


@WebServlet(value = "/personServlet")
public class PersonServlet extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
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
        if (validateCreatePlayer(personRequest))
        {
            // create person from the CreatePersonRequest
            PersonModel person = new PersonModel(
                    personRequest.getFirstName(),
                    personRequest.getLastName(),
                    personRequest.getEmailAddress(),
                    personRequest.getCellphoneNumber());

            SQLConnector.createPerson(person);
        }

        PrintWriter out = res.getWriter();
        res.setContentType("application/text;charset=utf-8");
        out.print("Person created");
        out.close();
    }

    private static boolean validateCreatePlayer(CreatePersonRequest personRequest)
    {
        String nameRegex = "^[a-zA-Z]*$";
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\\\.[A-Z]{2,6}$";
        String digitsRegex = "[0-9]+";

        if (!(personRequest.getFirstName().matches(nameRegex)))
            return false;
        if (!(personRequest.getLastName().matches(nameRegex)))
            return false;
        if (!(personRequest.getEmailAddress().matches(emailRegex)))
            return false;
        if (!(personRequest.getCellphoneNumber().matches(digitsRegex)))
            return false;
        return true;
    }
}
