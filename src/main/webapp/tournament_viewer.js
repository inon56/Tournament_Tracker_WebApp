
let unplayedMatchups = {};

let playedMatchups =
    {
        "tournamentName": "",
        "round": 0,
        "matchups": []
    };


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


function getNextRoundClicked()
{
    if (unplayedMatchups['matchups'].length === 0)
        getRound();
    else
        alert("Please fill in all the matchups before getting the next round");
}

window.onload = () => {
    getRound();
}

// GET Rounds
function getRound() // export?
{
    fetch("tournamentViewerServlet?" + new URLSearchParams({tournamentName: tournamentName}), {
        method: "GET"
    })
    .then(res => {return res.json()})
    .then((data) => {
        console.log(data);
        updateRoundList(data);
    })
    .then(() => {
        populateMatchupsDropDownList()
    })
    .catch((error) => alert(error))
}

function updateRoundList(roundMatchups)
{
    unplayedMatchups = roundMatchups
    document.getElementById("roundNumber").innerHTML = unplayedMatchups['round'];
    playedMatchups["tournamentName"] = unplayedMatchups['tournamentName'];
    playedMatchups["round"] = unplayedMatchups['round'];
}

function populateMatchupsDropDownList()
{
    for (const matchup of unplayedMatchups['matchups'])
    {
        let option = document.createElement("option");
        option.text = matchup['teamOneName'] + " vs " + matchup['teamTwoName'];
        selectMatchupList.add(option);
    }
}

function scoreButtonClicked()
{
    let matchupText = selectMatchupList.value;
    if (matchupText === '')
    {
        alert("option not selected");
        return false;
    }
    // let matchText = selectMatchupList.options[selectMatchupList.selectedIndex].text;
    const array = matchupText.split(" vs ");
    let teamOneName = document.getElementById('teamOneName').innerHTML = array[0];
    let teamTwoName = document.getElementById('teamTwoName').innerHTML = array[1];
    let teamOneScore = parseInt(document.getElementById("teamOneScore").value, 10);
    let teamTwoScore = parseInt(document.getElementById("teamTwoScore").value, 10);
    let winner = document.getElementById("winner");

    if (validateScores(teamOneScore, teamTwoScore))
    {
        // push into played matchups
        let playedMatchup = {"teamOneName": teamOneName, "teamTwoName": teamTwoName, "teamOneScore": teamOneScore, "teamTwoScore": teamTwoScore};
        playedMatchups['matchups'].push(playedMatchup);

        // remove from unplayed matchups
        unplayedMatchups = unplayedMatchups['matchups'].filter(matchup => matchup.teamOneName === teamOneName);

        if (teamOneScore > teamTwoScore) {
            winner.innerHTML = document.getElementById("teamOneName").innerHTML + " wins!";
        }
        else if (teamTwoScore > teamOneScore) {
            winner.innerHTML = document.getElementById("teamTwoName").innerHTML + " wins!";
        }

        winner.style.display = 'block';

        // insert into played matchups select box
        let playedMatchupList = document.getElementById("playedMatchupList")
        let option = document.createElement("option");
        let resultMatchupText = selectMatchupList.options[selectMatchupList.selectedIndex].text;
        option.text = resultMatchupText + " " + teamOneScore + " " + teamTwoScore;
        playedMatchupList.add(option);

        // remove from unplayed matchups select box
        selectMatchupList.options[selectMatchupList.selectedIndex].remove();
        selectMatchupList.value = '';
    }
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

function sendRoundClicked()
{
    if (playedMatchups['matchups'].length === 0)
        postRound();
    else
        alert("Please fill in all the matchups before sending");

}

function postRound()
{
    // const data = {"tournamentName": tournamentName, "round": 1, "matchups": {"teamOne": "aaa", "teamTwo": "bbb", "teamOneScore": 5, "teamTwoScore": 8}};

    fetch("tournamentViewerServlet", {
        method: 'POST',
        headers: { "content-type": "application/json" },
        body: JSON.stringify(playedMatchups)
    })
    .then(res => {return res.json()})
    .then((text) => alert(text))
    .catch((error) => alert(error))
}

