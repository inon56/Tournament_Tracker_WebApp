package com.tournamenttrucker.models;

// Represents one team in the matchup
public class MatchupEntryModel {

    // The unique identifier for the matchup entry.
    private transient int id;

    // The unique identifier for the team
    private transient int teamCompetingId;

    // Represents one team in the matchup.
    private TeamModel teamCompeting;

    // Represents the score for this particular team
    private double score;

    // The unique identifier for the parent matchup (team)
    private transient int parentMatchupId;

    // Represents the matchup that this team came from as the winner
    private MatchupModel parentMatchup;


    public int getId() {
        return id;
    }

    public int getTeamCompetingId() {
        return teamCompetingId;
    }

    public TeamModel getTeamCompeting() {
        return teamCompeting;
    }

    public double getScore() {
        return score;
    }


    public MatchupModel getParentMatchup() {
        return parentMatchup;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setTeamCompeting(TeamModel teamCompeting) {
        this.teamCompeting = teamCompeting;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setParentMatchup(MatchupModel parentMatchup) {
        this.parentMatchup = parentMatchup;
    }
}
