package com.tournamenttrucker.dataAccess;

import com.tournamenttrucker.contracts.*;
import com.tournamenttrucker.models.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// server name - INON_PC
// user name - INON_PC\leino

public class SQLConnector {

//    private static final String connectionUrl = "jdbc:sqlserver://localhost\\MSSQLSERVER;user=yakir;password=&UY^%Tr43e;encrypt=true;trustServerCertificate=true";
//    private static final String connectionUrl = "jdbc:mysql://localhost:3306/tournaments";

//    private static final String username = "root";
//    private static final String password = "inon5656";
//    @Resource(name = "jdbc/myproject")

    private static final String connectionUrl = "jdbc:sqlserver://localhost\\MSSQLSERVER;user=yakir;password=u&6yI84HFd36^;encrypt=true;trustServerCertificate=true";

    private static boolean initialized = false;

    private static Connection getManualConnection() throws SQLException {
        if (!initialized) {
            try {
//                Class.forName("com.mysql.jdbc.Driver");
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                initialized = true;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return DriverManager.getConnection(connectionUrl); // , username, password
    }

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

    public static void createPerson(CreatePersonRequest person)
    {
        try (Connection connection = getManualConnection();
        ){
            String sql = "insert into Person(FirstName,LastName,EmailAddress,CellphoneNumber) values(?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1,person.getFirstName());
            statement.setString(2,person.getLastName());
            statement.setString(3,person.getEmailAddress());
            statement.setString(4,person.getCellphoneNumber());
            statement.execute();
//            model.setId(myStmt.getInt(5));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createPrize(CreatePrizeRequest prize)
    {
        try (Connection connection = getManualConnection();
        ){
            String sql = "insert into dbo.Prizes(PlaceNumber,PlaceName,PrizeAmount,PrizePrecentage) values(?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1,prize.getPlaceNumber());
            statement.setString(2,prize.getPlaceName());
            statement.setDouble(3,prize.getPrizeAmount());
            statement.setDouble(4,prize.getPrizePercentage());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTeam(CreateTeamRequest team)
    {
        try (Connection connection = getManualConnection()
        ){
            PreparedStatement statementPersonId = connection.prepareStatement("select PersonId from dbo.Person where EmailAddress = ?");
            ResultSet resultSet = null;

            // loop over the teamMembers and find by the email the corresponding personId
            // each personId add to dbo.TeamMembers
            List<Integer> teamMembersIds = new ArrayList<>();
            for (String teamMember: team.getTeamMembers())
            {
                statementPersonId.setString(1, teamMember);
                resultSet = statementPersonId.executeQuery();
                if (resultSet.next())
                    teamMembersIds.add(resultSet.getInt(1));
                else
                    break;

//                System.out.println(resultSet.getInt(1) + "  " + teamMember);
            }

            String sqlTeams = "insert into dbo.Teams(TeamName) values (?)";
            PreparedStatement statementTeamName = connection.prepareStatement(sqlTeams, Statement.RETURN_GENERATED_KEYS);

            statementTeamName.setString(1, team.getTeamName());
            statementTeamName.execute();
            resultSet = statementTeamName.getGeneratedKeys();
            int teamId = 0;
            if (resultSet.next())
            {
                teamId = resultSet.getInt(1);
                System.out.println(teamId);
            }


            String sqlTeamMembers = "insert into dbo.TeamMembers(TeamId, PersonId) values (?,?)";
            PreparedStatement statementTeamMembers = connection.prepareStatement(sqlTeamMembers);

            for (Integer id: teamMembersIds)
            {
                statementTeamMembers.setInt(1, teamId);
                statementTeamMembers.setInt(2, id);
                statementTeamMembers.execute();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void createTournament(CreateTournamentRequest tournament)
    {
        int tournamentId = saveTournament(tournament);
        System.out.println(tournamentId);
//        saveTournamentPrizes(tournamentId, tournament);
        saveTournamentEntries(tournamentId, tournament);
//        saveTournamentRounds(tournament);
    }

    private static int saveTournament(CreateTournamentRequest model)
    {
        int tournamentId = 0;
        try (Connection connection = getManualConnection()
        ){
            ResultSet resultSet = null;
            String sql = "insert into dbo.Tournaments (TournamentName, EntryFee, Active) values (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, model.getTournamentName());
            statement.setDouble(2, model.getEntryFee());
            statement.setInt(3, 1);
            statement.execute();

            resultSet = statement.getGeneratedKeys();

            if (resultSet.next())
                tournamentId = resultSet.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tournamentId;
    }
    private static void saveTournamentPrizes(int tournamentId, CreateTournamentRequest model)
    {
        try (Connection connection = getManualConnection()
        ){
            ResultSet resultSet = null;

            // TODO: delete
//            String sqlTournamentId = "select TournamentId from dbo.tournaments where TournamentName = ?";
//            PreparedStatement statementTournamentId = connection.prepareStatement(sqlTournamentId);
//            statementTournamentId.setString(1, model.getTournamentName());
//            resultSet = statementTournamentId.executeQuery();
//            if (resultSet.next())
//                tournamentId = resultSet.getInt(1);


            String sqlPrizeId = "select PrizeId from dbo.Prizes where PlaceName = ?";
            PreparedStatement statementPrizeId = connection.prepareStatement(sqlPrizeId);


            List<Integer> prizesIds = new ArrayList<>();
            for (String prize: model.getEnteredPrizes())
            {
                statementPrizeId.setString(1, prize);
                resultSet = statementPrizeId.executeQuery();
                if (resultSet.next())
                    prizesIds.add(resultSet.getInt(1));
                else
                    break;
            }


            String sqlTournamentPrizes = "insert into dbo.TournamentPrizes (TournamentId, PrizeId) values (?,?)";
            PreparedStatement statementTournamentPrizes = connection.prepareStatement(sqlTournamentPrizes);

            for (Integer id: prizesIds)
            {
                statementTournamentPrizes.setInt(1, tournamentId);
                statementTournamentPrizes.setInt(2, id);
                statementTournamentPrizes.execute();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveTournamentEntries(int tournamentId, CreateTournamentRequest model)
    {
        try (Connection connection = getManualConnection()
        ) {
            ResultSet resultSet = null;
            String sqlTeamId = "select TeamId from dbo.Teams where TeamName = ?";
            PreparedStatement statementTeamId = connection.prepareStatement(sqlTeamId);

            List<Integer> teamsIds = new ArrayList<>();
            for (String team: model.getEnteredTeams())
            {
                statementTeamId.setString(1, team);
                resultSet = statementTeamId.executeQuery();
                if (resultSet.next())
                    teamsIds.add(resultSet.getInt(1));
                else
                    break;
            }


            String sqlTournamentEntries = "insert into dbo.TournamentEntries (TournamentId, TeamId) values (?,?)";
            PreparedStatement statementTournamentEntries = connection.prepareStatement(sqlTournamentEntries);

            for (Integer id: teamsIds)
            {
                statementTournamentEntries.setInt(1, tournamentId);
                statementTournamentEntries.setInt(2, id);
                statementTournamentEntries.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveTournamentRounds(CreateTournamentRequest model)
    {
        try (Connection connection = getManualConnection();
        ){
            CallableStatement myStmt = connection.prepareCall("{call dbo.spMatchups_Insert(?,?)}");

//            for (List<MatchupModel> round : model.getRounds())
            {
//                for (MatchupModel matchup : round)
//                {
////                    myStmt.setInt(1, model.getId());
//                    myStmt.setInt(2, matchup.getMatchupRound());
//                    myStmt.registerOutParameter(3, Types.INTEGER);
//
//                    myStmt.execute();
//
////                    model.setId(myStmt.getInt(3));
//
//                    // Typically in normal matchup you have two entries,team a and team b.
//                    // but in the case of a "bye" we only have one entry.
//                    for (MatchupEntryModel entry : matchup.getEntries())
//                    {
//                        //@MatchupId int,
//                        //@ParentMatchupId int,
//                        //@TeamCompetingId int,
//                        myStmt = connection.prepareCall("{call dbo.spMatchupEntries_Insert(?,?)}");
//
//                        myStmt.setInt(1, matchup.getId());
//                        if (entry.getParentMatchup() == null)
//                            myStmt.setNull(2, Types.INTEGER);
//                        else
//                            myStmt.setInt(2, entry.getParentMatchup().getId());
//
//                        if (entry.getTeamCompeting() == null)
//                            myStmt.setNull(3, Types.INTEGER);
//                        else
//                            myStmt.setInt(2, entry.getTeamCompeting().getId());
//
//                        myStmt.execute();
//                    }
                }
            } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }


    public static List<CreatePersonResponse> getPersonAll()
    {
        List<CreatePersonResponse> output = new ArrayList<>();

        try (Connection connection = getManualConnection();
             Statement statement = connection.createStatement();
        ){
            String sql = "select * from Person";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next())
            {
                CreatePersonResponse person = new CreatePersonResponse();
                person.setFirstName(resultSet.getString("FirstName"));
                person.setLastName(resultSet.getString("LastName"));
                person.setEmailAddress(resultSet.getString("EmailAddress"));

                output.add(person);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return output;
    }

    public static List<String> getPrizesAll()
    {
        List<String> output = new ArrayList<>();

        try (Connection connection = getManualConnection();
             Statement statement = connection.createStatement();
        ){
            String sql = "select PlaceName from dbo.Prizes";
            ResultSet resultSet = statement.executeQuery(sql);

            String prize;
            while (resultSet.next())
            {
                prize = resultSet.getString("PlaceName");
                output.add(prize);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return output;
    }
    public static List<String> getTeamAll()
    {
        List<String> output = new ArrayList<>();

        try (Connection connection = getManualConnection();
             Statement statement = connection.createStatement();
        ){
            String sql = "select TeamName from dbo.Teams";
            ResultSet resultSet = statement.executeQuery(sql);

            String team;
            while (resultSet.next()) {
                team = resultSet.getString("TeamName");
                output.add(team);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return output;
    }

    public static void getPrizesByTournament()
    {

    }

    public static List<String> getTournamentAll()
    {
        List<String> output = new ArrayList<>();
        try (Connection connection = getManualConnection();
        ) {
            String sql = "select * from dbo.Tournaments where Active = 1;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.execute();
            ResultSet result = statement.getResultSet();

            while (result.next())
            {
                output.add(result.getString("TournamentName"));
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
