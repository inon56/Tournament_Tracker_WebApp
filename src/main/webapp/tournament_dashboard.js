// import {getRound} from "./tournament_viewer";

window.onload = () => {
    getExistingTournaments();
}

function loadTournamentClicked()
{
    let tournamentSelected = document.getElementById("tournamentList");
    let tournamentName = tournamentSelected.options[tournamentSelected.selectedIndex].text;
    
    sessionStorage.setItem("tournamentName", tournamentName);
    window.open('tournament_viewer.html')
}

function getExistingTournaments()
{
    fetch("tournamentDashboardServlet", {
        method: "GET"
    })
    .then(res => {return res.json()})
    .then((data) => {
        populateTournamentsDropDown(data);
    })
    .catch((error) => alert(error + "get"))
}


function populateTournamentsDropDown(data)
{
    for (let tournament of data)
    {
        let tournaments = document.getElementById("tournamentList");
        let option = document.createElement('option');
        option.text = tournament;
        tournaments.add(option);
    }
}

function deleteTournament()
{
    let tournaments = document.getElementById("tournamentList");
    let tournamentName = tournaments.options[tournaments.selectedIndex].text;

    fetch("tournamentDashboardServlet", {
        method: 'POST',
        headers: { "content-type": "application/text" },
        body: JSON.stringify(tournamentName)
    })
        .then(res => {return res.text()})
        .then((text) => alert(text + "\nplease reload the page!"))
        .catch((error) => alert(error))
}


