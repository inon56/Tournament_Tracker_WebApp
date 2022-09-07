let availablePlayersList =
    [
        {"firstName":"dan", "lastName": "joy", "emailAddress": "dan@gmail.com"},
        {"firstName": "rock", "lastName": "dell", "emailAddress": "rock@gmail.com"},
        {"firstName": "mike", "lastName": "sylvester", "emailAddress": "mike@gmail.com"}
    ];
let teamPlayersList = [];

window.onload = () => {
    populatePlayersDropDown();
    getPlayers();
}


function getPlayers()
{
    fetch("teamServlet", {
        method: 'GET'
    })
    .then(res => {return res.json()})
    .then((data) => {
        console.log(data);
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
// adding the players to the availablePlayersArray
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

// add player to drop down list
function addSelectedPlayerClicked()
{
    // let playerSelected = document.getElementById("playersList").value;
    // let playerText = playerSelected.options[playerSelected.selectedIndex].text;
    // playerSelected.remove(playerSelected.selectedIndex);
    //
    // let teamPlayerSelected = document.getElementById("teamPlayersList");
    // let option = document.createElement("option");
    // option.text = playerText;
    // teamPlayerSelected.add(option);


    let playerSelected = document.getElementById("playersList")
    let playerText = playerSelected.options[playerSelected.selectedIndex].value;
    // // playerSelected.remove(playerSelected.selectedIndex);
    // playerSelected.options[playerSelected.selectedIndex].remove();

    // let teamPlayerSelected = document.getElementById("teamPlayersList");
    // let option = document.createElement("option");
    // option.text = playerText;
    // teamPlayerSelected.add(option);

    const playerIndex = availablePlayersList.findIndex((element) =>
    {
        return element === JSON.parse(playerText);
    })

    // update lists
    const index = availablePlayersList.indexOf(playerIndex)
    if (index !== -1)
    {
        const player = availablePlayersList.splice(index, 1);
        teamPlayersList.push(player);
    }

}


// remove the selected player from the list
function removeSelectedTeamPlayerClicked()
{    
    // let teamPlayersSelected = document.getElementById("teamPlayersList");
    // let playerText = teamPlayersSelected.options[teamPlayersSelected.selectedIndex].value;
    // teamPlayersSelected.remove(teamPlayersSelected.selectedIndex);
    //
    // let playerSelected = document.getElementById("teamsList");
    // let option = document.createElement("option");
    // option.text = playerText;
    // playerSelected.add(option);

    // const playerValue = teamPlayersList.findIndex(object =>
    // {
    //     return object['emailAddress'] ===
    // })


    // update lists
    const index = teamPlayersList.indexOf(playerValue)
    if (index !== -1)
    {
        const player = teamPlayersList.splice(index, 1);
        availablePlayersList.push(player);
    }
}

function createTeamClicked()
{
    let teamName = document.getElementById("teamName").value;

    // if (!validateCreateTeam(teamName))
    //     return;

    const team = {"teamName": teamName, "teamPlayersList": teamPlayersList};
    postTeam(team);

    // clear teamName field
    document.getElementById("teamName").value = "";
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
}

// POST availablePlayers and teamPlayers
// { "players": availablePlayersList }
function postTeam(team)
{
    console.log(team);

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




