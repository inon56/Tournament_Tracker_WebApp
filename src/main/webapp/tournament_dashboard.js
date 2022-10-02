
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

// GET
function getExistingTournaments()
{
    fetch("tournamentDashboardServlet", {
        method: "GET"
    })
    .then(res => {return res.json()})
    .then((data) => {
        populateTournamentsDropDownList(data);
    })
    .catch((error) => alert(error))
}


function populateTournamentsDropDownList(data)
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

    // POST
    fetch("tournamentDashboardServlet", {
        method: 'POST',
        headers: { "content-type": "application/text" },
        body: JSON.stringify(tournamentName)
    })
    .then(res => {
        if (res.ok) {
            return res.text();
        }
        else
            return "status code: 400";
    })
    .then((text) => alert(text + "\nplease reload the page!"))
    .catch((error) => alert(error))
}


