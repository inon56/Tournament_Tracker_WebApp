package com.tournamenttrucker.dataAccess;

import com.tournamenttrucker.contracts.MatchupResult;
import com.tournamenttrucker.contracts.MatchupTeamsCompeting;
import com.tournamenttrucker.models.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLConnector {

    private static final String connectionUrl = "jdbc:sqlserver://localhost\\MSSQLSERVER;user=yakir;password=u&6yI84HFd36^;encrypt=true;trustServerCertificate=true";

    private static boolean initialized = false;

    private static Connection getManualConnection() throws SQLException {
        if (!initialized) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                initialized = true;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return DriverManager.getConnection(connectionUrl);
    }

    public static String getTeamNameById(int teamId){
        String teamName = "";

        try (Connection connection = getManualConnection();
        ){
            String sqlTeam = "select TeamName from dbo.Teams where Id = ?";
            PreparedStatement statement = connection.prepareStatement(sqlTeam);
            statement.setInt(1, teamId);
            ResultSet resultSet = statement.executeQuery();

           if (resultSet.next())
           {
               teamName = resultSet.getString("TeamName");
           }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return teamName;
    }

    public static int getTeamIdByName(String teamName)
    {
        int teamId = 0;
        String sqlTeam = "select Id from dbo.Teams where TeamName = ?";

        try (Connection connection = getManualConnection();
             PreparedStatement statement = connection.prepareStatement(sqlTeam);
        ){
            statement.setString(1, teamName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next())
            {
                teamId = resultSet.getInt("Id");
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return teamId;
    }
    public static int getTournamentIdByName(String tournamentName)
    {
        int tournamentId = 0;
        String sql = "select Id from dbo.Tournaments where TournamentName = ?";

        try (Connection connection = getManualConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ){
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
        String sql = "select * from dbo.Tournaments where Id = ?";

        try (Connection connection = getManualConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ){
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
        String sql = "insert into Person(FirstName,LastName,EmailAddress,CellphoneNumber) values(?,?,?,?)";

        try (Connection connection = getManualConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setString(1,person.getFirstName());
            statement.setString(2,person.getLastName());
            statement.setString(3,person.getEmailAddress());
            statement.setString(4,person.getCellphoneNumber());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static MatchupModel getCurrentRoundMatchup(int tournamentId, int currentRound){
        MatchupModel matchup = new MatchupModel();
        String sqlMatchup = "select Id,WinnerId,TeamOneId,TeamTwoId,TeamOneScore,TeamTwoScore from dbo.Matchups where TournamentId = ? and MatchupRound = ?";

        try (Connection connection = getManualConnection();
             PreparedStatement statement = connection.prepareStatement(sqlMatchup);
        ){
            statement.setInt(1, tournamentId);
            statement.setInt(2, currentRound);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next())
            {
                matchup.setId(resultSet.getInt("Id"));
                matchup.setTournamentId(tournamentId);
                matchup.setMatchupRound(currentRound);
                matchup.setWinnerId(resultSet.getInt("WinnerId"));
                matchup.setTeamOneId(resultSet.getInt("TeamOneId"));
                matchup.setTeamTwoId(resultSet.getInt("TeamTwoId"));
                matchup.setTeamOneScore(resultSet.getInt("TeamOneScore"));
                matchup.setTeamTwoScore(resultSet.getInt("TeamTwoScore"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return matchup;
    }
    public static List<MatchupTeamsCompeting> getCurrentRoundMatchupsTeamsNames(int tournamentId, int currentRound){
        List<MatchupModel> matchupsTeamsIds = new ArrayList<>();
        List<MatchupTeamsCompeting> matchupsTeamsNames = new ArrayList<>();
        String sqlTeamCompetingIds = "select TeamOneId, TeamTwoId from dbo.Matchups where TournamentId = ? and MatchupRound = ?";
        String sqlTeamCompetingNames = "select TeamName from dbo.Teams where Id = ?";

        try (Connection connection = getManualConnection();
             PreparedStatement statementTeamCompetingIds = connection.prepareStatement(sqlTeamCompetingIds);
             PreparedStatement statementTeamCompetingNames = connection.prepareStatement(sqlTeamCompetingNames);
        ){
            statementTeamCompetingIds.setInt(1, tournamentId);
            statementTeamCompetingIds.setInt(2, currentRound);
            ResultSet resultSet = statementTeamCompetingIds.executeQuery();

            while (resultSet.next())
            {
                MatchupModel matchup = new MatchupModel();
                matchup.setTeamOneId(resultSet.getInt("TeamOneId"));
                matchup.setTeamTwoId(resultSet.getInt("TeamTwoId"));
                matchupsTeamsIds.add(matchup);
            }

            String teamOneName = "";
            String teamTwoName = "";

            for (MatchupModel matchupTeamId : matchupsTeamsIds)
            {
                statementTeamCompetingNames.setInt(1, matchupTeamId.getTeamOneId());
                resultSet = statementTeamCompetingNames.executeQuery();
                if (resultSet.next())
                    teamOneName = resultSet.getString("TeamName");

                statementTeamCompetingNames.setInt(1, matchupTeamId.getTeamTwoId());
                resultSet = statementTeamCompetingNames.executeQuery();
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
        String sql = "select WinnerId from dbo.Matchups where TournamentId = ? and MatchupRound = ?";

        try (Connection connection = getManualConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ){
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
    public static void updateMatchupsResult(List<MatchupResult> matchupResults)
    {
        String teamOneName, teamTwoName;
        int teamOneScore, teamTwoScore,teamOneId ,teamTwoId, winnerId;
        String sqlInsertMatchupResults = "update dbo.Matchups set WinnerId = ?, TeamOneScore = ?, TeamTwoScore = ? where TeamOneId = ? and TeamTwoId = ?";

        try (Connection connection = getManualConnection();
             PreparedStatement statement = connection.prepareStatement(sqlInsertMatchupResults);
        ){
            for (MatchupResult matchupResult : matchupResults)
            {
                teamOneName = matchupResult.getTeamOneName();
                teamTwoName = matchupResult.getTeamTwoName();
                teamOneScore = matchupResult.getTeamOneScore();
                teamTwoScore = matchupResult.getTeamTwoScore();

                // get teamOneId and teamTwoId by teamOneName and teamTwoName
                teamOneId = getTeamIdByName(teamOneName);
                teamTwoId = getTeamIdByName(teamTwoName);

                // assign the winnerId
                if (teamOneScore > teamTwoScore)
                    winnerId = teamOneId;
                else
                    winnerId = teamTwoId;

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
        String sql = "update dbo.Tournaments set CurrentRound = CurrentRound + 1 where Id = ?";

        try (Connection connection = getManualConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setInt(1, tournamentId);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void createMatchup(int tournamentId, int matchupRound, int teamOneId, int teamTwoId){
        String sql = "insert into dbo.Matchups (TournamentId,MatchupRound,TeamOneId,TeamTwoId) values (?,?,?,?)";

        try (Connection connection = getManualConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ){
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
        String sql = "select Id from dbo.Person where EmailAddress = ?";
        String sqlTeams = "insert into dbo.Teams(TeamName) values (?)";
        String sqlTeamMembers = "insert into dbo.TeamMembers(TeamId, PersonId) values (?,?)";

        try (Connection connection = getManualConnection();
             PreparedStatement statementPersonId = connection.prepareStatement(sql);
             PreparedStatement statementTeamName = connection.prepareStatement(sqlTeams, Statement.RETURN_GENERATED_KEYS);
             PreparedStatement statementTeamMembers = connection.prepareStatement(sqlTeamMembers);
        ){
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

            statementTeamName.setString(1, teamName);
            statementTeamName.execute();
            resultSet = statementTeamName.getGeneratedKeys();
            int teamId = 0;
            if (resultSet.next())
            {
                teamId = resultSet.getInt(1);
            }

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
        String sql = "insert into dbo.Tournaments (TournamentName, EntryFee, Active, CurrentRound, PrizeOption) values (?,?,?,?,?)";

        try (Connection connection = getManualConnection();
             PreparedStatement statement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        ){
            ResultSet resultSet;

            statement.setString(1, tournament.getTournamentName());
            statement.setDouble(2, tournament.getEntryFee());
            statement.setInt(3, 1);
            statement.setInt(4, 1);
            statement.setInt(5, tournament.getPrizeOption());
            statement.execute();

            resultSet = statement.getGeneratedKeys();

            if (resultSet.next())
                tournament.setId(resultSet.getInt(1));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private static void saveTournamentTeams(TournamentModel tournament, List<TeamModel> teams)
    {
        String sqlTeamId = "select Id from dbo.Teams where TeamName = ?";
        String sqlTournamentEntries = "insert into dbo.TournamentEntries (TournamentId, TeamId) values (?,?)";

        try (Connection connection = getManualConnection();
             PreparedStatement statementTeamId = connection.prepareStatement(sqlTeamId);
             PreparedStatement statementTournamentEntries = connection.prepareStatement(sqlTournamentEntries);
        ) {
            ResultSet resultSet;

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
    public static List<PersonModel> getAllAvailablePerson()
    {
        List<PersonModel> players = new ArrayList<>();

        try (Connection connection = getManualConnection();
             Statement statement = connection.createStatement();
        ){
            String sql = "select FirstName,LastName,EmailAddress from dbo.Person where Id not in (select PersonId from dbo.TeamMembers)";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next())
            {
                PersonModel player = new PersonModel();
                player.setFirstName(resultSet.getString("FirstName"));
                player.setLastName(resultSet.getString("LastName"));
                player.setEmailAddress(resultSet.getString("EmailAddress"));

                players.add(player);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return players;
    }
    public static List<TeamModel> getAllAvailableTeams()
    {
        List<TeamModel> teams = new ArrayList<>();
        String sql = "select TeamName from dbo.Teams where Id not in (select TeamId from dbo.TournamentEntries)";

        try (Connection connection = getManualConnection();
             Statement statement = connection.createStatement();
        ){
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
    public static List<TeamModel> getTournamentTeams(int tournamentId)
    {
        List<TeamModel> teams = new ArrayList<>();
        String sqlTournamentTeams = "select TeamId from dbo.TournamentEntries where TournamentId = ?";

        try (Connection connection = getManualConnection();
             PreparedStatement statement = connection.prepareStatement(sqlTournamentTeams);
        ){
            statement.setInt(1, tournamentId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next())
            {
                TeamModel team = new TeamModel();
                team.setId(resultSet.getInt("TeamId"));
                teams.add(team);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return teams;
    }
    public static List<PersonModel> getAllTeamMembers(int teamId)
    {
        List<PersonModel> players = new ArrayList<>();
        String sqlPersonId = "select PersonId from dbo.TeamMembers where TeamId = ?";
        String sqlPersonAttributes = "select FirstName,LastName,EmailAddress,CellphoneNumber from dbo.Person where Id = ?";

        try (Connection connection = getManualConnection();
             PreparedStatement statementPersonId = connection.prepareStatement(sqlPersonId);
             PreparedStatement statementPersonAttributes = connection.prepareStatement(sqlPersonAttributes)
        ){
            statementPersonId.setInt(1, teamId);
            ResultSet resultSet = statementPersonId.executeQuery();

            // assign players id's in the players list
            while (resultSet.next())
            {
                PersonModel player = new PersonModel();
                player.setId(resultSet.getInt("PersonId"));
                players.add(player);
            }

            // assign players attributes to the according player with the same id
            for (PersonModel player : players)
            {
                statementPersonAttributes.setInt(1, player.getId());
                resultSet = statementPersonAttributes.executeQuery();

                if (resultSet.next())
                {
                    player.setFirstName(resultSet.getString("FirstName"));
                    player.setLastName(resultSet.getString("LastName"));
                    player.setEmailAddress(resultSet.getString("EmailAddress"));
                    player.setCellphoneNumber(resultSet.getString("CellphoneNumber"));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return players;
    }
    public static List<String> getAllActiveTournaments()
    {
        List<String> output = new ArrayList<>();
        String sql = "select TournamentName from dbo.Tournaments where Active = 1;";

        try (Connection connection = getManualConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
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
        String sql = "update dbo.Tournaments set Active = 0 where id = ?";

        try (Connection connection = DriverManager.getConnection(connectionUrl);
             PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setInt(1, tournament.getId());
            statement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // deletes tournament entries and matchups and tournamentId
    public static void deleteTournament(int tournamentId)
    {
        String sqlDeleteEntries = "delete from dbo.TournamentEntries where TournamentId = ?";
        String sqlDeleteMatchups = "delete from dbo.Matchups where TournamentId = ?";
        String sqlDeleteTournament = "delete from dbo.Tournaments where Id = ?";

        try (Connection connection = getManualConnection();
             PreparedStatement statement = connection.prepareStatement(sqlDeleteEntries);
             PreparedStatement statementDeleteMatchups = connection.prepareStatement(sqlDeleteMatchups);
             PreparedStatement statementDeleteTournament = connection.prepareStatement(sqlDeleteTournament);
        ){
            statement.setInt(1, tournamentId);
            statement.execute();

            statementDeleteMatchups.setInt(1, tournamentId);
            statementDeleteMatchups.execute();

            statementDeleteTournament.setInt(1, tournamentId);
            statementDeleteTournament.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkPlayersExist(List<String> teamMembersEmails)
    {
        String sql = "select * from dbo.Person where EmailAddress = ?";

        try (Connection connection = getManualConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ){
            ResultSet resultSet;

            for (String email : teamMembersEmails)
            {
                statement.setString(1, email);
                resultSet = statement.executeQuery();

                if (!resultSet.next())
                    return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public static boolean checkTeamsExist(List<String> teams)
    {
        String sql = "select * from dbo.Teams where TeamName = ?";

        try (Connection connection = getManualConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ){
            ResultSet resultSet;

            for (String team : teams)
            {
                statement.setString(1, team);
                resultSet = statement.executeQuery();

                if (!resultSet.next())
                    return false;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    public static boolean checkMatchupsExist(int tournamentId, int matchupRound, List<MatchupResult> matchups)
    {
        String sql = "select TeamOneId from dbo.Matchups where TournamentId = ? and MatchupRound = ? and TeamOneId = ? and TeamTwoId = ?";

        try (Connection connection = getManualConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ){
            ResultSet resultSet;
            int teamOneId = 0;
            int teamTwoId = 0;

            for (MatchupResult matchup : matchups)
            {
                teamOneId = getTeamIdByName(matchup.getTeamOneName());
                teamTwoId = getTeamIdByName(matchup.getTeamTwoName());

                if (teamOneId == 0 || teamTwoId == 0)
                    return false;

                statement.setInt(1, tournamentId);
                statement.setInt(2, matchupRound);
                statement.setInt(3, teamOneId);
                statement.setInt(4, teamTwoId);
                resultSet = statement.executeQuery();

                if (!resultSet.next())
                    return false;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
