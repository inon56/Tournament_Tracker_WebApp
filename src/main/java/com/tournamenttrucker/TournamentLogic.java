package com.tournamenttrucker;

import com.tournamenttrucker.dataAccess.SQLConnector;
import com.tournamenttrucker.models.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TournamentLogic {
//    public static void prizeDistribution(int numEntries)
//    {
//        PrizeDistributionModel prize = new PrizeDistributionModel();
//        prize.setNumEntries(numEntries);
//
//    }
    public static void createRounds(TournamentModel model)
    {
        List<TeamModel> randomizedTeams = new ArrayList<>(model.getEnteredTeams());
        Collections.shuffle(randomizedTeams);
        int rounds = findNumberOfRounds(randomizedTeams.size());
        model.getRounds().add(createFirstRound(randomizedTeams));
        createOtherRounds(model, rounds);

//        int rounds = findNumberOfRounds(randomizedTeams.size());
//        int byes = numberOfByes(rounds, randomizedTeams.size());
//        model.getRounds().add(createFirstRound(byes, randomizedTeams));
//        CreateOtherRounds(model, rounds);

    }
    private static int findNumberOfRounds(int teamCount)
    {
        int output = 1;
        int val = 2;

        while (val < teamCount)
        {
            output += 1;
            val *= 2;
        }
        return output;
    }

//    private static int numberOfByes(int rounds, int numberOfTeams)
//    {
//        int output = 0;
//        int totalTeams = 1;
//
//        for (int i = 1; i < rounds; i++)
//        {
//            totalTeams *= 2;
//        }
//        output = totalTeams - numberOfTeams;
//
//        return output;
//    }

    private static List<MatchupModel> createFirstRound(List<TeamModel> teams)
    {
        List<MatchupModel> output = new ArrayList<>();
        MatchupModel curr = new MatchupModel();

        for (TeamModel team : teams)
        {
            MatchupEntryModel matchEntry = new MatchupEntryModel();
            matchEntry.setTeamCompeting(team);
            curr.getEntries().add(matchEntry);
        }
        return output;
    }

     // Create every round after the first
    private static void createOtherRounds(TournamentModel model, int rounds)
    {
        int round = 2; // Current round
        List<MatchupModel> previousRound = model.getRounds().get(0);
        List<MatchupModel> currRound = new ArrayList<>();
        MatchupModel currMatchup = new MatchupModel();

        while (round <= rounds)
        {
            for (MatchupModel match : previousRound)
            {
                MatchupEntryModel matchEntry = new MatchupEntryModel();
                matchEntry.setParentMatchup(match);
                currMatchup.getEntries().add(matchEntry);

                if (currMatchup.getEntries().size() > 1)
                {
                    currMatchup.setMatchupRound(round);
                    currRound.add(currMatchup);
                    currMatchup = new MatchupModel();
                }
            }
            model.getRounds().add(currRound);
            previousRound = currRound;
            currRound = new ArrayList<>();
            round++;
        }
    }

    private static void completeTournament(TournamentModel model)
    {
        SQLConnector.CompleteTournament(model);
//        TeamModel winners1 = model.getRounds().Last().First().Winner;
//        TeamModel runnerUp2 = model.getRounds().Last().First().Entries.Where(x => x.TeamCompeting != winners).First().TeamCompeting;

        double winnerPrize = 0;
        double runnerUpPrize = 0;

        // if we have no prizes we haven't give any money
        if (model.getPrizes().size() > 0)
        {
            double totalIncome = model.getPrizes().size() * model.getEntryFee();

//            PrizeModel firstPlacePrize1 = model.getPrizes().Where(x => x.PlaceNumber == 1).FirstOrDefault(); // FirstOrDefault meaning find the first value but if you dont find one then return null or 0 according to the type
//            PrizeModel secondPlacePrize2 = model.getPrizes().Where(x => x.PlaceNumber == 1).FirstOrDefault();

            PrizeModel firstPlacePrize = model.getPrizes().stream().filter((prize) -> prize.getPlaceNumber() == 1).findFirst().orElse(null);
            PrizeModel secondPlacePrize = model.getPrizes().stream().filter((prize) -> prize.getPlaceNumber() == 1).findFirst().orElse(null);

            if (firstPlacePrize != null)
            {
                winnerPrize = calculatePrizePayout(firstPlacePrize, totalIncome);
            }

            if (secondPlacePrize != null)
            {
                runnerUpPrize = calculatePrizePayout(secondPlacePrize, totalIncome);
            }
        }

        // Send Email to all tournament

        StringBuilder body = new StringBuilder(); // not synchronized

//        String subject = "In " + model.getTournamentName() + " " + winners.getTeamName() + " has won!";
        body.append("<h1>We have a winner</h1>");
        body.append("<p>Congratulations to our winner on a great tournament.</p>");
        body.append("<br/>");

        if (winnerPrize > 0)
//            body.append("<p>" + winners.getTeamName() + " " + "will receive " + winnerPrize + "</P>");

        if (runnerUpPrize > 0)
//            body.append("<p>" + runnerUp.getTeamName() + " " + "will receive " + runnerUpPrize +"</P>");

        body.append("<p>Thanks for a great tournament everyone!</P>");

        List<String> recipients = new ArrayList<>();

        for (TeamModel team : model.getEnteredTeams())
        {
            for (PersonModel person : team.getTeamMembers())
            {
                String email = person.getEmailAddress();
                if (email.length() > 0)
                    recipients.add(email);
            }
        }

//        EmailLogic.sendEmail(recipients, subject, body.toString());
    }

    private static double calculatePrizePayout(PrizeModel prize, double totalIncome)
    {
        double output = 0;



//        if (prize.getPrizeAmount() > 0)
//            output = prize.getPrizeAmount();
//
//        else
//            output = totalIncome * (prize.getPrizePercentage() / 100);

        return output;
    }

}
