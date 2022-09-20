
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
    .catch(() => {
        alert('problem with getExistingTournaments:');
    })
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

