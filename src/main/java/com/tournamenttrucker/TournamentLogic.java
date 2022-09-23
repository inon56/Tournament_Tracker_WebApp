package com.tournamenttrucker;

import com.tournamenttrucker.dataAccess.SQLConnector;
import com.tournamenttrucker.models.*;

import java.util.*;

public class TournamentLogic {
    public static int numberOfTotalRounds = 0;

    public static void findNumberOfRounds(int teamsEntered)
    {
        int val = 2;

        while (val < teamsEntered)
        {
            numberOfTotalRounds += 1;
            val *= 2;
        }

    }

    private static void calculatePrizePayout(TournamentModel tournament,int winningTeam, int prizeOption)
    {
        double totalIncome = winningTeam * tournament.getEntryFee();
        PrizePercentageDistribution prizeDistribution = PrizeGenerator.getPrizeOptions().get(prizeOption);

        double firstPlacePrize = prizeDistribution.getFirst() * totalIncome;
        double secondPlacePrize = prizeDistribution.getFirst() * totalIncome;

//        double thirdPlacePrize = prizeDistribution.getFirst() * totalIncome;
    }

    private static int completeRound(TournamentModel tournament) {
        // set score for all current matchups for the current round
        // if not last round:
        //     currentRound++
        int numberOfTotalRounds = 2;// TODO: delete this
        int currRound = tournament.getCurrentRound();
        if (currRound < numberOfTotalRounds)
        {
            tournament.setCurrentRound(currRound + 1);
            return 1;
        }
        // if last round:
        //     give prize to winners
        //calculatePrizePayout();
        else
        {
            return 0;
        }
    }

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
            teams = SQLConnector.getAllAvailableTeams();
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

    public static void completeTournament(TournamentModel tournament, List<String> teamsNames)
    {
        SQLConnector.completeTournament(tournament);
        calculatePrizePayout(tournament, teamsNames.size(), tournament.getPrizeOption());

        StringBuilder body = new StringBuilder(); // not synchronized

        body.append("<h1>We have a winner</h1>");
        body.append("<p>Congratulations to our winner on a great tournament.</p>");
        body.append("<br/>");

        body.append("<p>Thanks for a great tournament everyone!</P>");

        List<String> recipients = new ArrayList<>();

//        for (TeamModel team : model.getEnteredTeams())
//        {
//            for (PersonModel person : team.getTeamMembers())
//            {
//                String email = person.getEmailAddress();
//                if (email.length() > 0)
//                    recipients.add(email);
//            }
//        }

//        EmailLogic.sendEmail(recipients, subject, body.toString());
    }



}
