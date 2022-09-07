// example:
window.onload = () => {
    // getRounds();
    populateTeamDropDownList()
}

let unplayedRounds = [];

let round =
    {
        "number": 1,
        "games":
            [
                {
                    "teamOne": "jhon",
                    "teamTwo": "dan"
                },
                {
                    "teamOne": "sony",
                    "teamTwo": "exbox"
                }
            ]
    };
// end

const tournamentName = sessionStorage.getItem("tournamentName");
document.getElementById("tournamentName").innerHTML = tournamentName;

const selectMatchupList = document.getElementById('matchupList');
selectMatchupList.addEventListener('change', (event) =>
{
    let winner = document.getElementById("winner");
    winner.style.display = 'none';
    document.getElementById("teamOneScore").value = "";
    document.getElementById("teamTwoScore").value = "";

    let matchText = selectMatchupList.options[selectMatchupList.selectedIndex].text;
    const array = matchText.split(" vs ");
    document.getElementById('teamOneName').innerHTML = array[0];
    document.getElementById('teamTwoName').innerHTML = array[1];
});

// GET Rounds
function getRounds()
{
    fetch("tournamentViewerServlet", {
        method: "GET"
    })
    .then(res => {return res.json()})
    .then((data) => {
        updatePlayersLists(data);
    })
    .then(() => {
        populateTeamDropDownList()
    })
    .catch((error) => {
        alert('There has been a problem with your fetch operation:', error);
    })
}


// json: { "rounds": [{"team1": "cool-team", "team2": "super-team"}, {..}, {...}]
function updateRoundsList(data)
{
    let rounds = JSON.parse(data);
    roundsList = rounds['rounds'];
}

function populateTeamDropDownList()
{
    for (const round of roundsList['rounds'])
    {
        let option = document.createElement("option");
        option.text = round['teamOne'] + " vs " + round['teamTwo'];
        selectMatchupList.add(option);
    }
}

function scoreButtonClicked()
{
    let matchText = selectMatchupList.value;
    if (matchText === '')
    {
        alert("option not selected");
        return false;
    }

    let teamOneScore = parseInt(document.getElementById("teamOneScore").value, 10);
    let teamTwoScore = parseInt(document.getElementById("teamTwoScore").value, 10);
    let winner = document.getElementById("winner");

    if (isNaN(teamOneScore) || isNaN(teamTwoScore))
    {
        alert("Invalid score, please enter numbers!");
        return false;
    }
    else if (teamOneScore === teamTwoScore)
    {
        alert("I do not handle tie games!");
        return false;
    }
    else if (teamOneScore > teamTwoScore)
    {
        winner.innerHTML = document.getElementById("teamOneName").innerHTML + " wins!";
    }
    else if (teamTwoScore > teamOneScore)
    {
        winner.innerHTML = document.getElementById("teamTwoName").innerHTML + " wins!";
    }
    winner.style.display = 'block';

    selectMatchupList.options[selectMatchupList.selectedIndex].remove();
    selectMatchupList.value = '';

    // TODO:
    // send the number of winning team

    
}

