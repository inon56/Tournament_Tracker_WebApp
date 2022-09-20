// example:
let unplayedRounds = [];

let playedRounds = []

let round =
    {
        "number": 1,
        "games":
            [
                {
                    "teamOne": "john",
                    "teamTwo": "dan",
                },
                {
                    "teamOne": "sony",
                    "teamTwo": "xbox",
                }
            ]
    };

window.onload = () => {
    document.getElementById('getNextRound').style.visibility = 'hidden';
    // getRound();
    updateRoundList(round);
    populateTeamDropDownList()
}
// end


const tournamentName = sessionStorage.getItem("tournamentName");
document.getElementById("tournamentName").innerHTML = tournamentName;

const selectMatchupList = document.getElementById('unplayedMatchupList');
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

// TODO: check if select have no options
function listenerNoOptions()
{
    if (selectMatchupList.options.length === 0)
        document.getElementById('getNextRound').style.visibility = 'visible';
}

function getNextRoundClicked()
{
    document.getElementById('getNextRound').style.visibility = 'hidden';
    getRound();
}

// GET Rounds
function getRound()
{
    fetch("tournamentViewerServlet", {
        method: "GET"
    })
    .then(res => {return res.json()})
    .then((data) => {
        updateRoundList(data);
    })
    .then(() => {
        populateTeamDropDownList()
    })
    .catch((error) => alert(error))
}

// json: { "number": 1,"games": [{"team1": "cool-team", "team2": "super-team"}, {..}, {...}]
function updateRoundList(data)
{
    // round = JSON.parse(data);
    document.getElementById("roundNumber").innerHTML = round['number'];
}

function populateTeamDropDownList()
{
    for (const game of round['games'])
    {
        let option = document.createElement("option");
        option.text = game['teamOne'] + " vs " + game['teamTwo'];
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

    if (validateScores(teamOneScore, teamTwoScore))
    {
        let result = matchText + " " +  teamOneScore.toString() + " " + teamTwoScore.toString();
        playedRounds.push(result);

        if (teamOneScore > teamTwoScore)
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

        let playedMatchupList = document.getElementById("playedMatchupList")
        let option = document.createElement("option");
        option.text = result;
        playedMatchupList.add(option);

        // TODO:
        // send the number of winning team
        // postGame(teamOneScore, teamTwoScore);

        if (unplayedRounds.length === 0)
        {
            checkForNextRound();
        }
    }
}

function checkForNextRound()
{
    document.getElementById('getNextRound').style.visibility = 'visible';
}


function validateScores(teamOneScore, teamTwoScore)
{
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
    return true;
}

function postGame(teamOneScore, teamTwoScore)
{
    const data = {"tournamentName": tournamentName, "game": {"teamOne": "aaa", "teamTwo": "bbb", "teamOneScore": teamOneScore, "teamTwoScore": teamTwoScore}};

    fetch("tournamentServlet", {
        method: 'POST',
        headers: { "content-type": "application/json" },
        body: JSON.stringify(data)
    })
    // .then(res => {return res.json()})
    // .then((text) => alert(text))
    .catch((error) => alert(error))
}

