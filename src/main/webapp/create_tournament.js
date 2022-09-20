let teamsList = [];
let enteredTeams = [];
let prizesList = [];


let data =
    {
        "teams": ["cool-team","super-team"],

        "prizeOptions":
            [
                {"option": "1", "firstPlace": "100%"},
                {"option": "2", "firstPlace": "80%", "secondPlace": "20%"},
                {"option": "3", "firstPlace": "70%", "secondPlace": "30%"},
                {"option": "4", "firstPlace": "60%", "secondPlace": "40%"},
                {"option": "5", "firstPlace": "60%", "secondPlace": "30%", "thirdPlace": "10%"}
            ]
    }


window.onload = () => {
    // getTournament();
    updateLists(data);
    populatePrizeOptionsDropDownList();
    populateTeamsDropDownList();
}

function getTournament()
{
    fetch("tournamentServlet", {
        method: "GET",
    })
        .then(res => {return res.json()}) // res.json returns a promise
        .then((data) => {updateLists(data);})
        .then(() => {
            populateTeamsDropDownList();
            populatePrizesDropDownList();
        })
        .catch((error) => alert(error));
}

// json: { "teams": ["cool-team","super-team", ...], "prizes": ["first", "second", ...]  }
function updateLists(data)
{
    // teamsList
    for (const team of data["teams"])
    {
        teamsList.push(team);
    }

    // prizesList
    for (const prizeOption of data["prizeOptions"])
    {
        prizesList.push(prizeOption)
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
    teamsList.push(teamText); // TODO maybe need to add value

    let index = enteredTeams.indexOf(teamValue) // TODO maybe need to add value
    if (index !== -1)
        enteredTeams.splice(index,1);
}

function createTournamentClicked()
{
    let tournamentName = document.getElementById("tournamentName").value;
    let entryFee = document.getElementById("entryFee").value;
    if (validateCreateTournament(tournamentName, entryFee))
    {
        postTournament(tournamentName, entryFee);
    }
}

function validateCreateTournament(tournamentName, entryFee)
{
    let stringRegex = /^[a-zA-Z]+$/;

    if (!stringRegex.test(tournamentName))
    {
        alert("Invalid team name, letters only")
        return false;
    }
    if (isNaN(entryFee))
    {
        alert("Invalid entry fee, numbers only");
        return false;
    }

    return true;
}

function postTournament(tournamentName, entryFee)
{
    const tournament = {"tournamentName": tournamentName, "entryFee": entryFee, "enteredTeams": enteredTeams};

    fetch("tournamentServlet", {
        method: 'POST',
        headers: { "content-type": "application/json" }, // Explicitly posting JSON
        body: JSON.stringify(tournament) // JSON.stringify(object) utility function is used to transform a JavaScript object into a JSON string. body option accepts a string but not an object.
    })
        .then(res => {return res.text()})
        .then((text) => alert(text + ", click to close the window"))
        .then(window.close)
        .catch(() => alert((error) => alert(error)))
}

// function clearFields()
// {
//     document.getElementById("tournamentName").value = "";
//     document.getElementById("entryFee").value = "0";
// }
