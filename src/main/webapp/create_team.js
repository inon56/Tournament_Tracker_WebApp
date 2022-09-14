let availablePlayersList =
    [
        {"firstName":"dan", "lastName": "joy", "emailAddress": "dan@gmail.com"},
        {"firstName": "rock", "lastName": "dell", "emailAddress": "rock@gmail.com"},
        {"firstName": "mike", "lastName": "sylvester", "emailAddress": "mike@gmail.com"}
    ];
let teamPlayersList = [];

window.onload = () => {
    populatePlayersDropDown();
    // getPlayers();
}

function getPlayers()
{
    fetch("teamServlet", {
        method: 'GET'
    })
    .then(res => {return res.json()})
    .then((data) => {
        // updatePlayersLists(data);
    })
    // .then(() => {
    //     populatePlayersDropDown()
    // })
    .catch(() => {
        alert('There has been a problem with your fetch operation:');
    })
}

// args: {  "players": [ {"firstName": val, "lastName": val, "email": val, "cellphone": val}, ... ], "teamPlayers [ {"firstName": val, "lastName": val, "email": val, "cellphone": val}, ... ] }
// adding the players to the availablePlayersList
function updatePlayersLists(data)
{
    let players = JSON.parse(data);
    availablePlayersList = players['players'];
}

function populatePlayersDropDown()
{
    let players = document.getElementById("playersList");

    for (const player of availablePlayersList)
    {
        let option = document.createElement("option");
        let values = Object.values(player);
        let text = values.toString();
        option.text = text.replaceAll(","," ");
        players.add(option);
    }
}

// add player to drop-down list
function addSelectedPlayerClicked()
{
    let playerSelected = document.getElementById("playersList");
    let playerValue = playerSelected.value;
    let playerText = playerSelected.options[playerSelected.selectedIndex].text;
    playerSelected.remove(playerSelected.selectedIndex);

    let teamPlayerAdded = document.getElementById("teamPlayersList");
    let option = document.createElement("option");
    option.text = playerText;
    teamPlayerAdded.add(option);

    const array = playerText.split(" ");
    const email = array[2];

    const playerIndex = availablePlayersList.findIndex((availablePlayersList) =>
    {
        return availablePlayersList["emailAddress"] === email;
    })

    // update lists
    // teamPlayersList.push(playerValue);

    if (playerIndex !== -1)
    {
        let player = availablePlayersList.splice(playerIndex, 1)[0];
        teamPlayersList.push(player);
    }
}

// remove the selected player from the drop-down list
function removeSelectedTeamPlayerClicked()
{    
    let playerSelected = document.getElementById("teamPlayersList");
    let playerValue = playerSelected.value;
    let playerText = playerSelected.options[playerSelected.selectedIndex].text;
    playerSelected.remove(playerSelected.selectedIndex);

    let playerRemoved = document.getElementById("playersList");
    let option = document.createElement("option");
    option.text = playerText;
    playerRemoved.add(option);

    const array = playerText.split(" ");
    const email = array[2];

    const playerIndex = teamPlayersList.findIndex(() =>
    {
        return teamPlayersList["emailAddress"] === email;
    })

    // update lists
    if (playerIndex !== -1)
    {
        let player = teamPlayersList.splice(playerIndex, 1)[0];
        availablePlayersList.push(player);
    }
}


function createTeamClicked()
{
    let teamName = document.getElementById("teamName").value;

    // if (!validateCreateTeam(teamName))
    // {
        let emails = [];
        for (const player of teamPlayersList)
        {
            emails.push(player['emailAddress']);
        }

        const team = {"teamName": teamName, "teamMembersEmails": emails};
        postTeam(team);

        // clear teamName field
        document.getElementById("teamName").value = "";
    // }
}

function validateCreateTeam(teamName)
{
    let stringRegex = /^[a-zA-Z]+$/;

    if (teamName.length == 0)
    {
        alert("Missing team name")
        return false;
    }
    if (!stringRegex.test(teamName))
    {
        alert("Invalid team name, letters only")        
        return false;
    }
    return true;
}


// { "teamName": "name", "teamMembersEmails": ["email_1", "email_2", "email_3"] }
function postTeam(team)
{
	fetch("teamServlet", {
	    method: 'POST',
	    headers: { "content-type": "application/json" },
	    body: JSON.stringify(team) // JSON.stringify(object) utility function is used to transform a JavaScript object into a JSON string. body option accepts a string but not an object.
	})
	.then(res => {return res.json()})
    .catch(() => {
        alert('There has been a problem with your fetch operation:');
    })
}




