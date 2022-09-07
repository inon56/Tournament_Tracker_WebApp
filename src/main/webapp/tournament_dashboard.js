
window.onload = () => {
    let dataSample = ["first", "second", "third"]
    populateTournamentsDropDown(dataSample);
    // getExistingTournaments();
}

// window.onload = () => {
//     let dataSample = ["first", "second", "third"]
//     // populateTournamentsDropDown(dataSample);
//     getExistingTournaments();
// }

function loadTournamentClicked()
{
    let tournamentSelected = document.getElementById("tournamentList");
    let tournamentName = tournamentSelected.options[tournamentSelected.selectedIndex].text;
    
    sessionStorage.setItem("tournamentName", tournamentName);
    window.open('tournament_viewer.html')
}

// GET existing tournaments
function getExistingTournaments()
{
    fetch("tournamentDashboardServlet", {
        method: "GET"
    })
    .then(res => {return res.json()})
    .then((data) => {
        console.log(data);
        // populateTournamentsDropDown(data);
    })
    .catch(() => {
        alert('There has been a problem with your fetch operation:');
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

