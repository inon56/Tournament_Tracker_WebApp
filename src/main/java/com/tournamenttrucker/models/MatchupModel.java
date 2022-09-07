package com.tournamenttrucker.models;

import java.util.ArrayList;
import java.util.List;

/// <summary>
/// Represents one match in the tournament
/// </summary>
public class MatchupModel {

    // The unique identifier for the matchup
    private transient int id;

    // The set of teams that were involved in this match
    private List<MatchupEntryModel> entries;

    /// <summary>
    /// The ID from the database that will be used to identify the winner
    /// </summary>
    private transient int winnerId;
    /// <summary>
    /// The winner of the match
    /// </summary>
    private TeamModel winner;
    /// <summary>
    /// Which round this match is a part of
    /// </summary>
    private int matchupRound;


    public int getId() {
        return id;
    }

    public List<MatchupEntryModel> getEntries() {
        return entries;
    }

    public int getWinnerId() {
        return winnerId;
    }

    public TeamModel getWinner() {
        return winner;
    }

    public int getMatchupRound() {
        return matchupRound;
    }

    public String getDisplayName() {
        String output = "";

        for (MatchupEntryModel me : entries)
        {
            if (me.getTeamCompeting() != null)
            {
                if (output.length() == 0)
                {
                    output = me.getTeamCompeting().getTeamName();
                }
                else
                {
                    //output += $" vs. {me.TeamCompeting.TeamName}";
                }
            }
            else
            {
                output = "Matchup not yet determined"; // because we are in a round where we dont know the winners from the previous round
                break;
            }
        }

        return output;
    }

    public void setWinner(TeamModel winner) {
        this.winner = winner;
    }

    public void setMatchupRound(int matchupRound) {
        this.matchupRound = matchupRound;
    }
}
