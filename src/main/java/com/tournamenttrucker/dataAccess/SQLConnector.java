package com.tournamenttrucker.dataAccess;

import com.tournamenttrucker.contracts.MatchupResult;
import com.tournamenttrucker.contracts.MatchupTeamsCompeting;
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

    public static int getTournamentIdByName(String tournamentName)
    {
        int tournamentId = 0;
        try (Connection connection = getManualConnection()
        ){
            String sql = "select Id from dbo.Tournaments where TournamentName = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, tournamentName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
                tournamentId = resultSet.getInt("Id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tournamentId;
    }

    public static TournamentModel getTournamentById(int tournamentId){
        TournamentModel tournament = new TournamentModel();
        tournament.setId(tournamentId);

        try (Connection connection = getManualConnection();
        ){
            String sql = "select * from dbo.Tournaments where Id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, tournamentId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                tournament.setTournamentName(resultSet.getString("TournamentName"));
                tournament.setEntryFee(resultSet.getInt("EntryFee"));
                tournament.setActive(resultSet.getInt("Active"));
                tournament.setCurrentRound(resultSet.getInt("CurrentRound"));
                tournament.setPrizeOption(resultSet.getInt("PrizeOption"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return tournament;
    }

    public static void createPerson(PersonModel person)
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

    public static List<MatchupTeamsCompeting> getCurrentRoundMatchupsTeamsNames(int tournamentId, int currentRound){
        List<MatchupModel> matchupsTeamsIds = new ArrayList<>();
        List<MatchupTeamsCompeting> matchupsTeamsNames = new ArrayList<>();

        try (Connection connection = getManualConnection()
        ){
            String sqlTeamCompetingIds = "select TeamOneId, TeamTwoId from dbo.Matchups where TournamentId = ? and MatchupRound = ?";
            PreparedStatement statement = connection.prepareStatement(sqlTeamCompetingIds);
            statement.setInt(1, tournamentId);
            statement.setInt(2, currentRound);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                MatchupModel matchup = new MatchupModel();
                matchup.setTeamOneId(resultSet.getInt("TeamOneId"));
                matchup.setTeamTwoId(resultSet.getInt("TeamTwoId"));
                matchupsTeamsIds.add(matchup);
            }


            String sqlTeamCompetingNames = "select TeamName from dbo.Teams where Id = ?";
            statement = connection.prepareStatement(sqlTeamCompetingNames);
            String teamOneName = "";
            String teamTwoName = "";

            for (MatchupModel matchupTeamId : matchupsTeamsIds)
            {
                statement.setInt(1, matchupTeamId.getTeamOneId());
                resultSet = statement.executeQuery();
                if (resultSet.next())
                    teamOneName = resultSet.getString("TeamName");

                statement.setInt(1, matchupTeamId.getTeamTwoId());
                resultSet = statement.executeQuery();
                if (resultSet.next())
                    teamTwoName = resultSet.getString("TeamName");

                matchupsTeamsNames.add(new MatchupTeamsCompeting(teamOneName, teamTwoName));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return matchupsTeamsNames;
    }
    public static List<MatchupModel> getCurrentRoundMatchupsWinnerId(int tournamentId, int currentRound){
        List<MatchupModel> matchups = new ArrayList<>();

        try (Connection connection = getManualConnection()
        ){
            String sql = "select WinnerId from dbo.Matchups where TournamentId = ?, CurrentRound = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, tournamentId);
            statement.setInt(2, currentRound);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                MatchupModel matchup = new MatchupModel();
                matchup.setWinnerId(resultSet.getInt("WinnerId"));
                matchups.add(matchup);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return matchups;
    }
    public static void updateMatchupsResult(List<MatchupResult> matchupResults){
        try (Connection connection = getManualConnection();
        ){
            ResultSet resultSet;
            String teamOneName, teamTwoName;
            int teamOneScore, teamTwoScore,teamOneId ,teamTwoId, winnerId;

            for (MatchupResult matchupResult : matchupResults)
            {
                teamOneName = matchupResult.getTeamOneName();
                teamTwoName = matchupResult.getTeamOneName();
                teamOneScore = matchupResult.getTeamOneScore();
                teamTwoScore = matchupResult.getTeamTwoScore();

                // get teamOneId by teamOneName
                String sqlTeamOneId = "select Id from dbo.Teams where TeamName = ?";
                PreparedStatement statement = connection.prepareStatement(sqlTeamOneId);
                statement.setString(1, teamOneName);
                resultSet = statement.executeQuery();
                teamOneId = resultSet.getInt("Id");

                // get teamTwoId by teamTwoName
                String sqlTeamTwoId = "select Id from dbo.Teams where TeamName = ?";
                statement = connection.prepareStatement(sqlTeamTwoId);
                statement.setString(1, teamTwoName);
                resultSet = statement.executeQuery();
                teamTwoId = resultSet.getInt("Id");


                // assign the winner
                if (teamOneScore > teamTwoScore)
                    winnerId = teamOneId;
                else
                    winnerId = teamTwoId;

                String sqlInsertMatchupResults = "update dbo.Matchups set WinnerId = ?, TeamOneScore = ?, TeamTwoScore = ? where TeamOneId = ? and TeamTwoId = ?";

                statement = connection.prepareStatement(sqlInsertMatchupResults);
                statement.setInt(1, winnerId);
                statement.setInt(2, teamOneScore);
                statement.setInt(3, teamTwoScore);
                statement.setInt(4, teamOneId);
                statement.setInt(5, teamTwoId);
                statement.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void incrementTournamentRound(int tournamentId) {
        try (Connection connection = getManualConnection();
        ) {
            String sql = "update from dbo.Tournaments set CurrentRound = CurrentRound + 1 where TournamentId = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, tournamentId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createMatchup(int tournamentId, int matchupRound, int teamOneId, int teamTwoId){
        try (Connection connection = getManualConnection()
        ){
            String sql = "insert into dbo.Matchups (TournamentId,MatcupRound,TeamOneId,TeamTwoId) values (?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, tournamentId);
            statement.setInt(2, matchupRound);
            statement.setInt(3, teamOneId);
            statement.setInt(4, teamTwoId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTeam(String teamName, List<String> teamMembers)
    {
        try (Connection connection = getManualConnection()
        ){
            PreparedStatement statementPersonId = connection.prepareStatement("select Id from dbo.Person where EmailAddress = ?");
            ResultSet resultSet;

            // loop over the teamMembers and find by the email the corresponding personId
            // each personId add to dbo.TeamMembers
            List<Integer> teamMembersIds = new ArrayList<>();
            for (String teamMember: teamMembers)
            {
                statementPersonId.setString(1, teamMember);
                resultSet = statementPersonId.executeQuery();
                if (resultSet.next())
                    teamMembersIds.add(resultSet.getInt(1));
                else
                    break;
            }

            String sqlTeams = "insert into dbo.Teams(TeamName) values (?)";
            PreparedStatement statementTeamName = connection.prepareStatement(sqlTeams, Statement.RETURN_GENERATED_KEYS);

            statementTeamName.setString(1, teamName);
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

    public static void createTournament(TournamentModel tournament, List<TeamModel> teams)
    {
        saveTournament(tournament);
        saveTournamentTeams(tournament, teams);
    }

    private static void saveTournament(TournamentModel tournament)
    {
        int tournamentId = 0;
        try (Connection connection = getManualConnection()
        ){
            ResultSet resultSet;
            String sql = "insert into dbo.Tournaments (TournamentName, EntryFee, Active, CurrentRound, PrizeOption) values (?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, tournament.getTournamentName());
            statement.setDouble(2, tournament.getEntryFee());
            statement.setInt(3, 1);
            statement.setInt(4, 1);
            statement.setInt(5, tournament.getPrizeOption());
            statement.execute();

            resultSet = statement.getGeneratedKeys();

            if (resultSet.next())
                tournamentId = resultSet.getInt(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        tournament.setId(tournamentId); // TODO: check if id still equal 0
    }

    private static void saveTournamentTeams(TournamentModel tournament, List<TeamModel> teams)
    {
        try (Connection connection = getManualConnection()
        ) {
            ResultSet resultSet;
            String sqlTeamId = "select Id from dbo.Teams where TeamName = ?";
            PreparedStatement statementTeamId = connection.prepareStatement(sqlTeamId);

            List<Integer> teamsIds = new ArrayList<>();
            for (TeamModel team: teams)
            {
                statementTeamId.setString(1, team.getTeamName());
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
                statementTournamentEntries.setInt(1, tournament.getId());
                statementTournamentEntries.setInt(2, id); // teamId
                statementTournamentEntries.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<PersonModel> getAllPerson()
    {
        List<PersonModel> output = new ArrayList<>();

        try (Connection connection = getManualConnection();
             Statement statement = connection.createStatement();
        ){
            String sql = "select FirstName,LastName,EmailAddress from dbo.Person where Id not in (select PersonId from dbo.TeamMembers)";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next())
            {
                PersonModel person = new PersonModel();
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

    public static List<TeamModel> getAllAvailableTeams()
    {
        List<TeamModel> teams = new ArrayList<>();

        try (Connection connection = getManualConnection();
             Statement statement = connection.createStatement();
        ){
            String sql = "select TeamName from dbo.Teams where Id not in (select TeamId from dbo.TournamentEntries)";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                TeamModel team = new TeamModel();
                team.setTeamName(resultSet.getString("TeamName"));
                teams.add(team);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return teams;
    }

    public static List<String> getAllActiveTournaments()
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

    public static void completeTournament(TournamentModel tournament)
    {
        try (Connection connection = DriverManager.getConnection(connectionUrl)
        ){
            String sql = "update dbo.Tournaments set Active = 0 where id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setInt(1, tournament.getId());
            statement.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
