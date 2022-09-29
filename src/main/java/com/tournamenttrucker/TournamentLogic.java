package com.tournamenttrucker;

import com.tournamenttrucker.dataAccess.SQLConnector;
import com.tournamenttrucker.models.*;

import java.util.*;

public class TournamentLogic {
    private static HashMap<Integer, Integer> generateMatchups(List<Integer> teamsIds){
        HashMap<Integer, Integer> matchups = new HashMap<>();
        // for X teams generate X/2 random matches
        Collections.shuffle(teamsIds);

        for (int i = 0; i < teamsIds.size(); i += 2) {
            matchups.put(teamsIds.get(i), teamsIds.get(i+1));
        }

        return matchups;
    }
    private static List<TeamModel> getCurrentRoundTeams(TournamentModel tournament){
        List<TeamModel> teams = new ArrayList<>();

        if (tournament.getCurrentRound() == 1) {
            teams = SQLConnector.getTournamentTeams(tournament.getId());
        }
        else {
            List<MatchupModel> matchups = SQLConnector.getCurrentRoundMatchupsWinnerId(tournament.getId(), tournament.getCurrentRound() - 1);
            // from every matchup get the winner, and put them all in a list
            for (MatchupModel matchup : matchups) {
                TeamModel winnerTeam = new TeamModel(matchup.getWinnerId());
                teams.add(winnerTeam);
            }
        }

        return teams;
    }

    public static void createNextRound(int tournamentId)
    {
        // 1. take the tournamentId and get tournament from DB
        TournamentModel tournament = SQLConnector.getTournamentById(tournamentId);

        // 2. get teams according to tournament id and current round
        List<TeamModel> teams;
        teams = getCurrentRoundTeams(tournament);

        // 3. read all matchups from previous round and find winners that will continue to this round
        // 4. generate randomly matchups
        // 5. write matchups to DB
        // 6. call completeRound
        List<Integer> teamsIds = new ArrayList<>();
        for (TeamModel team : teams)
            teamsIds.add(team.getId());

        HashMap<Integer, Integer> matchups = generateMatchups(teamsIds);
        for (Map.Entry<Integer, Integer> matchup : matchups.entrySet()) {
            SQLConnector.createMatchup(tournamentId, tournament.getCurrentRound(), matchup.getKey(), matchup.getValue());
        }
    }

    private static Map<Integer, Double> calculatePrizePayout(TournamentModel tournament, int winningTeam, int prizeOption)
    {
        Map<Integer, Double> prizes = new HashMap<>();

        double totalIncome = winningTeam * tournament.getEntryFee();
        PrizePercentageDistribution prizeDistribution = PrizeGenerator.getPrizeOptions().get(prizeOption);

        double firstPlacePrize = prizeDistribution.getFirst() * totalIncome;
        double secondPlacePrize = prizeDistribution.getFirst() * totalIncome;
        prizes.put(1, firstPlacePrize);
        prizes.put(2, secondPlacePrize);

        return prizes;
    }

    public static void completeTournament(TournamentModel tournament, int currentRound)
    {
        // set tournament Active = 0 , meaning it is completed
        SQLConnector.completeTournament(tournament);

        MatchupModel matchup = SQLConnector.getCurrentRoundMatchup(tournament.getId(), currentRound);

        int firstPlaceTeamId;
        int secondPlaceTeamId;
        int teamOneScore = matchup.getTeamOneScore();
        int teamTwoScore =  matchup.getTeamTwoScore();
        if (teamOneScore > teamTwoScore) {
            firstPlaceTeamId = matchup.getTeamOneId();
            secondPlaceTeamId = matchup.getTeamTwoId();
        }
        else {
            firstPlaceTeamId = matchup.getTeamTwoId();
            secondPlaceTeamId = matchup.getTeamOneId();
        }

        Map<Integer, TeamModel> winningTeams = new HashMap<>();
        winningTeams.put(1, new TeamModel(firstPlaceTeamId));
        winningTeams.put(2, new TeamModel(secondPlaceTeamId));

        // get winning teams names
        for (Map.Entry<Integer, TeamModel> team : winningTeams.entrySet()) {
            team.getValue().setTeamName(SQLConnector.getTeamNameById(team.getValue().getId()));
        }

        // get prizes layout
        Map<Integer, Double> prizes = calculatePrizePayout(tournament, winningTeams.size(), tournament.getPrizeOption());

        // TODO: send emails
//        for (int i = 1; i < winningTeams.size() + 1; i++)
//        {
//            List<PersonModel> players = SQLConnector.getAllTeamMembers(winningTeams.get(i).getId());
//
//            EmailLogic.sendEmail(tournament,winningTeams.get(i).getTeamName(), players, prizes.get(i));
//        }
    }

}
