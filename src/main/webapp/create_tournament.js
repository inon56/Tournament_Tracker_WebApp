let teamsList = ["dodgers", "lakers", "celtic"];
let prizesList = ["first", "second"];
let enteredTeams = [];
let enteredPrizes = [];

window.onload = () => {
    populateTeamsDropDownList();
    populatePrizesDropDownList();
    // getTournaments();
}

// GET tournaments
function getTournaments()
{
    fetch("tournamentServlet", {
        method: "GET",
        headers: {'Accept': 'application/json'} // Explicitly asking for JSON
    })
    .then(res => {return res.json()}) // res.json returns a promise 
    .then((data) => {
        updateLists(data);
    })
    .then(() => {
        populateTeamDropDownList();
        populatePrizesDropDownList();
    })
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
    for (const prize of data["prizes"])
    {
        prizesList.push(prize);
    }
}

function populateTeamsDropDownList()
{
    for (let team of teamsList)
    {
        let teams = document.getElementById("teamsList");
        let option = document.createElement("option");
        option.text = team;
        teams.add(option);
    }
}

function populatePrizesDropDownList()
{
    for (let prize of prizesList)
    {
        let prizes = document.getElementById("prizesList");
        let option = document.createElement("option");
        option.text = prize;
        prizes.add(option);
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
    if (index != -1)
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
    if (index != -1)
        enteredTeams.splice(index,1);
}

function addPrizeClicked()
{
    let prizeSelected = document.getElementById("prizesList");
    let prizeValue = prizeSelected.value;
    let prizeText = prizeSelected.options[prizeSelected.selectedIndex].text;
    prizeSelected.remove(prizeSelected.selectedIndex);

    let prizesInTournametSelected = document.getElementById("prizesInTournamentList");
    let option = document.createElement("option");
    option.text = prizeText;
    prizesInTournametSelected.add(option);

    // update lists
    enteredPrizes.push(prizeText);
    
    let index = prizesList.indexOf(prizeValue)
    if (index != -1)
        prizesList.splice(index,1);
}

function removeSelectedPrizeClicked()
{
    let prizesInTournamentSelected = document.getElementById("prizesInTournamentList");
    let prizeValue = prizesInTournamentSelected.value;
    let prizeText = prizesInTournamentSelected.options[prizesInTournamentSelected.selectedIndex].text;
    prizesInTournamentSelected.remove(prizesInTournamentSelected.selectedIndex);

    let prizeSelected = document.getElementById("prizesList");
    let option = document.createElement("option");
    option.text = prizeText;
    prizeSelected.add(option);

    // update lists
    prizesList.push(prizeText); 

    let index = enteredPrizes.indexOf(prizeValue)
    if (index !== -1)
        enteredPrizes.splice(index,1);
}


function createTournamentClicked()
{
    let tournamentName = document.getElementById("tournamentName").value;
    let entryFee = document.getElementById("entryFee").value;

    validateCreateTournament(tournamentName, entryFee);

    postTournament(tournamentName, entryFee);
    // window.close();

    // clearFields(); // no need clear if close the window
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
}

function postTournament(tournamentName, entryFee)
{
    // TODO: maybe delete this
    // let teamsListSelected = [];
    // for (let i = 0; i < teamsList.options.length; i++)
    // {
    //     teamsListSelected.push(teamsListSelected.options[i].text);
    // }
    //
    // let prizesList = document.getElementById("prizesList");
    // let prizesListSelected = [];
    // for (let i = 0; i < prizesList.options.length; i++)
    // {
    //     prizesListSelected.push(prizesListSelected.options[i].text);
    // }

    const data = {"tournamentName": tournamentName, "entryFee": entryFee, "enteredTeams": enteredTeams, "enteredPrizes": enteredPrizes};

	fetch("tournamentServlet", {
	    method: 'POST',
	    headers: { "content-type": "application/json" }, // Explicitly posting JSON
	    body: JSON.stringify(data) // JSON.stringify(object) utility function is used to transform a JavaScript object into a JSON string. body option accepts a string but not an object.
	})
	.then(res => {return res.json()})
    .catch(() => {
        alert('There has been a problem with your fetch operation:');
    })
    .then(alert("success"))
}

function clearFields()
{
    document.getElementById("tournamentName").value = "";
    document.getElementById("entryFee").value = "0";
}
