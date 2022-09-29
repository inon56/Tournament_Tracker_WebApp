let teamsList = [];
let enteredTeams = [];
let prizesList = [];

window.onload = () => {
    getTournament();
}

function getTournament()
{
    fetch("tournamentServlet", {
        method: "GET",
    })
        .then(res => {return res.json()}) // res.json returns a promise
        .then((data) => {
            updateTeamsList(data);
            updatePrizesList(data);
        })
        .then(() => {
            populateTeamsDropDownList();
            populatePrizeOptionsDropDownList();
        })
        .catch((error) => alert(error));
}


function updateTeamsList(data)
{
    for (const team of data["teamsNames"])
    {
        teamsList.push(team);
    }
}

function updatePrizesList(data)
{
    const count = Object.keys(data["prizeOptions"]).length;
    for (let i = 1; i <= count; i++)
    {
        prizesList.push(data["prizeOptions"][i]);
    }
}

function populateTeamsDropDownList()
{
    let teams = document.getElementById("teamsList");

    for (const team of teamsList)
    {
        let option = document.createElement("option");
        option.text = team;
        teams.add(option);
    }
}

function populatePrizeOptionsDropDownList()
{
    let prizes = document.getElementById("prizeOptionsList");
    let txt = "";
    let concatTxt ="";

    for (const prize of prizesList)
    {
        let option = document.createElement("option");
        concatTxt += "option " // TODO: add the number , needed for the post

        for (const [key, val] of Object.entries(prize))
        {
            txt = (`${key}: ${val} `);
            concatTxt += txt;
        }
        option.text = concatTxt;
        prizes.add(option);
        concatTxt ="";
    }
}

function addTeamClicked()
{
    let teamSelected = document.getElementById("teamsList");
    let teamValue = teamSelected.value;
    let teamText = teamSelected.options[teamSelected.selectedIndex].text;
    teamSelected.remove(teamSelected.selectedIndex);

    let teamsInTournamentSelected = document.getElementById("teamsInTournametList");
    let option = document.createElement("option");
    option.text = teamText;
    teamsInTournamentSelected.add(option);

    // update lists
    enteredTeams.push(teamText);

    const index = teamsList.indexOf(teamValue);
    if (index !== -1)
        teamsList.splice(index,1);

}

function removeSelectedTeamClicked()
{
    let teamsInTournamentSelected = document.getElementById("teamsInTournametList");
    let teamValue = teamsInTournamentSelected.value;
    let teamText = teamsInTournamentSelected.options[teamsInTournamentSelected.selectedIndex].text;
    teamsInTournamentSelected.remove(teamsInTournamentSelected.selectedIndex);

    let teamSelected = document.getElementById("teamsList");
    let option = document.createElement("option");
    option.text = teamText;
    teamSelected.add(option);

    // update lists
    teamsList.push(teamText);

    let index = enteredTeams.indexOf(teamValue)
    if (index !== -1)
        enteredTeams.splice(index,1);
}

function createTournamentClicked()
{
    let tournamentName = document.getElementById("tournamentName").value;
    let entryFee = document.getElementById("entryFee").value;
    // let txt = document.getElementById("prizeOptionsList").value.split(" ",2); // string
    // let prizeOption = 2// txt[1];

    let index = document.getElementById("prizeOptionsList").value.split(" ",2);
    let prizeOption = prizesList.indexOf(index);

    if (validateCreateTournament(tournamentName, entryFee, prizeOption))
    {
        postTournament(tournamentName, entryFee, prizeOption);
    }
}

function validateCreateTournament(tournamentName, entryFee)
{
    let stringRegex = /^[a-zA-Z]+$/;

    if (!stringRegex.test(tournamentName))
    {
        alert("Invalid team name, letters only!");
        return false;
    }
    if (isNaN(entryFee))
    {
        alert("Invalid entry fee, numbers only!");
        return false;
    }
    if (parseInt(entryFee) === 0)
    {
        alert("Please enter entry fee!");
        return false;
    }
    if (enteredTeams.length % 2 !== 0)
    {
        alert("the number of entered teams is not even!")
        return false;
    }

    return true;
}

function postTournament(tournamentName, entryFee, prizeOption)
{
    const tournament = {"tournamentName": tournamentName, "entryFee": entryFee, "enteredTeams": enteredTeams, "prizeOption": prizeOption};

    fetch("tournamentServlet", {
        method: 'POST',
        headers: { "content-type": "application/json" }, // Explicitly posting JSON
        body: JSON.stringify(tournament) // JSON.stringify(object) utility function is used to transform a JavaScript object into a JSON string. body option accepts a string but not an object.
    })
    .then(res => {
        if (res.ok) {
            return res.text();
        }
        else
            return "status code: 400";
    })
    .then((text) => alert(text + ", click to close the window"))
    .then(window.close)
    .catch(() => alert((error) => alert(error)))
}
