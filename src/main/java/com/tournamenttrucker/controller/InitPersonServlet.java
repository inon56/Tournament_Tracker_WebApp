package com.tournamenttrucker.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.annotation.Resource;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebServlet(value = "/initPersonServlet")
public class InitPersonServlet extends HttpServlet {
    private static final String connectionUrl = "jdbc:mysql://localhost:3306/tournaments";
    private static final String username = "root";
    private static final String password = "inon5656";

    @Resource(name = "jdbc/myproject")
    private DataSource dataSource;

    public InitPersonServlet(){
        System.out.println("ctor - init person");
    }

    public void init() throws ServletException
    {
        System.out.println("init person");
//        DriverManager.registerDriver(new com.mysql.jdbc.Driver ());


        try {
//            Connection connection = DriverManager.getConnection(connectionUrl, username, password);
            Connection connection = dataSource.getConnection();
//            connection.createStatement()
            System.out.println("connection success");
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
//        catch (ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }

//    public static void createPerson(CreatePersonRequest person)
//    {
//            String sql = "insert into Person(FirstName,LastName,EmailAddress,CellphoneNumber) values(?,?,?,?)";
//            PreparedStatement statement = connection.prepareStatement(sql);
//
//            statement.setString(1,person.getFirstName());
//            statement.setString(2,person.getLastName());
//            statement.setString(3,person.getEmailAddress());
//            statement.setString(4,person.getCellphoneNumber());
//            statement.execute();
//    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException
    {
        System.out.println("person post method");
        PrintWriter out = res.getWriter();
        res.setContentType("application/json;charset=utf-8");
        out.print("{\"dsd\":\"fdfd\"}");
        out.close();
    }

}
