package com.tournamenttrucker.dataAccess;

import com.tournamenttrucker.contracts.CreateTeamRequest;
import com.tournamenttrucker.models.*;
import org.apache.commons.dbutils.ResultSetHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// server name - INON_PC
// user name - INON_PC\leino

public class SQLConnector implements IDataConnection {

//    private static final String connectionUrl = "jdbc:sqlserver://localhost\\MSSQLSERVER;user=yakir;password=&UY^%Tr43e;encrypt=true;trustServerCertificate=true";
    private static final String connectionUrl = "jdbc:sqlserver://localhost\\MSSQLSERVER;user=yakir;password=u&6yI84HFd36^;encrypt=true;trustServerCertificate=true";

    // MYSQL example:
//    private static final String connectionUrl = "jdbc:mysql://localhost:3306/database/sql_store";
//    private static final String username = "root";
//    private static final String password = "inon56";
//
//    public static void initConnection()
//    {
//        try {
//            Connection connection = DriverManager.getConnection(connectionUrl,username, password);
//            System.out.println("connected");
//        } catch (SQLException e) {
//            System.out.println("error connection");
//        }
//    }
    // END

    public static void printPerson_All() {
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             CallableStatement myStmt = connection.prepareCall("{call dbo.spPeople_GetAll}");
        ){
            myStmt.execute();
            ResultSet myRes = myStmt.getResultSet();
            ResultSetMetaData myData = myRes.getMetaData();

            for (int i =1; i <= myData.getColumnCount(); i++)
            {
                System.out.print(myData.getColumnName(i) + "\t");
            }
            System.out.println();
            while (myRes.next()) {
                for (int i = 1; i <= myData.getColumnCount(); i++) {
                    System.out.print(myRes.getString(i) + "\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createPerson(PersonModel model)
    {

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             CallableStatement myStmt = connection.prepareCall("{call dbo.spPeople_Insert(?,?,?,?)}"); // prepare the stored procedure call
        ){
            myStmt.setString(1, model.getFirstName());
            myStmt.setString(2, model.getLastName());
            myStmt.setString(3, model.getEmailAddress());
            myStmt.setString(4, model.getCellphoneNumber());
            myStmt.registerOutParameter(5, Types.INTEGER);

            // call stored procedure
            myStmt.execute();

            model.setId(myStmt.getInt(5));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public static void createPrize(PrizeModel model)
    {
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             CallableStatement myStmt = connection.prepareCall("{call dbo.spPrizes_Insert(?,?,?,?)}");
        ){
            myStmt.setInt(1, model.getPlaceNumber());
            myStmt.setString(2, model.getPlaceName());
            myStmt.setDouble(3, model.getPrizeAmount());
            myStmt.setDouble(4, model.getPrizePercentage());
            myStmt.registerOutParameter(5, Types.INTEGER);
//            myStmt.executeQuery("f")
            // call stored procedure
            myStmt.execute();

            model.setId(myStmt.getInt(5));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTeam(CreateTeamRequest team)
    {
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             CallableStatement myStmt_1 = connection.prepareCall("{call dbo.spTeams_Insert(?)}");
             CallableStatement myStmt_2 = connection.prepareCall("{call dbo.spTeamMembers_Insert(?,?)}");
        ){

            myStmt_1.setString(1, team.getTeamName());
            myStmt_1.registerOutParameter(2, Types.INTEGER);
            myStmt_1.execute();

            // get the value of the OUT parameter

            int teamId = myStmt_1.getInt(2); // put here the output id of myStmt_1
//            select player where email == player with same email

            for (String memberName : team.getTeamMembers())
            {
                myStmt_2.setInt(1, teamId);
//                myStmt_2.setInt(2, person.getId());
                myStmt_2.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTournament(TournamentModel model)
    {
        saveTournament(model);
        saveTournamentPrizes(model);
        saveTournamentEntries(model);
        saveTournamentRounds(model);
    }

    private static void saveTournament(TournamentModel model)
    {
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             CallableStatement myStmt = connection.prepareCall("{call dbo.spTournaments_Insert(?,?)}");
        ){
            myStmt.setString(1, model.getTournamentName());
            myStmt.setDouble(2, model.getEntryFee());
            myStmt.registerOutParameter(3, Types.INTEGER);

            myStmt.execute();

            model.setId(myStmt.getInt(3));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static void saveTournamentPrizes(TournamentModel model)
    {
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             CallableStatement myStmt = connection.prepareCall("{call dbo.spTournamentPrizes_Insert(?,?)}");
        ){
            for (PrizeModel pz : model.getPrizes())
            {
                myStmt.setInt(1, model.getId());
                myStmt.setInt(2, pz.getId());
                myStmt.registerOutParameter(3, Types.INTEGER);

                myStmt.execute();

                // TODO: check if need to get the id output
//                model.setId(myStmt.getInt(3));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveTournamentEntries(TournamentModel model)
    {
        try (Connection connection = DriverManager.getConnection(connectionUrl);
             CallableStatement myStmt = connection.prepareCall("{call dbo.spTeammembers_insert(?,?)}");
        ) {
            for (TeamModel tm : model.getEnteredTeams())
            {
                myStmt.setInt(1, model.getId());
                myStmt.setInt(2, tm.getId());
                myStmt.registerOutParameter(3, Types.INTEGER);
                myStmt.execute();

                // TODO: check if need to get the id output
//                model.setId(myStmt.getInt(3));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveTournamentRounds(TournamentModel model)
    {
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        ){
            CallableStatement myStmt = connection.prepareCall("{call dbo.spMatchups_Insert(?,?)}");

            for (List<MatchupModel> round : model.getRounds())
            {
                for (MatchupModel matchup : round)
                {
                    myStmt.setInt(1, model.getId());
                    myStmt.setInt(2, matchup.getMatchupRound());
                    myStmt.registerOutParameter(3, Types.INTEGER);

                    myStmt.execute();

                    model.setId(myStmt.getInt(3));

                    // Typically in normal matchup you have two entries,team a and team b.
                    // but in the case of a "bye" we only have one entry.
                    for (MatchupEntryModel entry : matchup.getEntries())
                    {
                        //@MatchupId int,
                        //@ParentMatchupId int,
                        //@TeamCompetingId int,
                        myStmt = connection.prepareCall("{call dbo.spMatchupEntries_Insert(?,?)}");

                        myStmt.setInt(1, matchup.getId());
                        if (entry.getParentMatchup() == null)
                            myStmt.setNull(2, Types.INTEGER);
                        else
                            myStmt.setInt(2, entry.getParentMatchup().getId());

                        if (entry.getTeamCompeting() == null)
                            myStmt.setNull(3, Types.INTEGER);
                        else
                            myStmt.setInt(2, entry.getTeamCompeting().getId());

                        myStmt.execute();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<PersonModel> getPerson_All()  {
        String ss = System.getenv("CATALINA_HOME");
        System.out.println(ss);
//        try{
////            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//        }catch (Exception e){
//            System.out.println(e.toString());
//        }
        List<PersonModel> output = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(connectionUrl);
        ){
            CallableStatement myStmt = connection.prepareCall("{call dbo.spPeople_GetAll}");
            myStmt.execute();
            ResultSet result = myStmt.getResultSet();

            while (result.next())
            {
                PersonModel person = new PersonModel();
                person.setFirstName(result.getString("FirstName"));
                person.setLastName(result.getString("LastName"));
                person.setEmailAddress(result.getString("EmailAddress"));
                person.setCellphoneNumber(result.getString("CellphoneNumber"));

                output.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return output;
    }

    public static List<TeamModel> getTeam_All()
    {
        List<TeamModel> output = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(connectionUrl);
        ) {
            CallableStatement myStmt = connection.prepareCall("{call dbo.spPeople_GetAll}");
            myStmt.execute();
            ResultSet result = myStmt.getResultSet();

            while (result.next())
            {
                TeamModel team = new TeamModel();
                team.setTeamName(result.getString("TeamName"));

                output.add(team);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return output;
    }

    // TODO
    public static List<TournamentModel> getTournament_All()
    {
        List<TournamentModel> output = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(connectionUrl);
        ) {
            CallableStatement myStmt = connection.prepareCall("{call dbo.spTournaments_GetAll}");
            myStmt.execute();
            ResultSet result = myStmt.getResultSet();

            while (result.next())
            {
                TournamentModel team = new TournamentModel();
                team.setTournamentName(result.getString("TeamName"));

                output.add(team);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return output;
    }

    public static void updateMatchup(MatchupModel model)
    {
        try (Connection connection = DriverManager.getConnection(connectionUrl);
        ) {
            CallableStatement myStmt;
            if (model.getWinner() != null)
            {
                // spMatchups_Update @id, @WinnerId
                myStmt = connection.prepareCall("{call dbo.spMatchups_Update(?,?)}");
                myStmt.setInt(1, model.getId());
                myStmt.setInt(2, model.getWinner().getId());
                myStmt.execute();
            }

            // spMatchupsEntries_Update id, TeamCompetingId, Score
            for (MatchupEntryModel me : model.getEntries())
            {
                if (me.getTeamCompeting() != null)
                {
                    myStmt = connection.prepareCall("{call dbo.spMatchupsEntries_Update(?,?,?)}");
                    myStmt.setInt(1, me.getId());
                    myStmt.setInt(1, me.getTeamCompetingId());
                    myStmt.setDouble(3, me.getScore());
                    myStmt.execute();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void CompleteTournament(TournamentModel model)
    {
        try (Connection connection = DriverManager.getConnection(connectionUrl)
        ){
            CallableStatement myStmt = connection.prepareCall("{call dbo.spTournaments_Complete}");
            myStmt.setInt(1, model.getId());
            myStmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
