
let unplayedMatchups = {};

let roundToPost =
    {
        "tournamentName": "",
        "round": 0,
        "matchupsResults": []
    }

let playedMatchups =
    {
        "tournamentName": "",
        "round": 0,
        "matchupsResults": []
    };

window.onload = () => {
    document.getElementById("sendRound").style.visibility = 'hidden';
    getRound();
}


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
    {
        getRound();
        let tournamentName = unplayedMatchups['tournamentName'];
        let round = unplayedMatchups['round'];

        roundToPost =
            {
                "tournamentName": tournamentName,
                "round": round,
                "matchupsResults": []
            };
    }

    else
        alert("Please fill in all the matchups before getting the next round");
}


// GET Rounds
function getRound()
{
    fetch("tournamentViewerServlet?" + new URLSearchParams({tournamentName: tournamentName}), { // send message with GET
        method: "GET"
    })
    .then(res => {return res.json()})
    .then((data) => {
        updateRoundList(data);
    })
    .then(() => {
        populateMatchupsDropDownList()
    })
    .catch((error) => alert(error))
}

function updateRoundList(data)
{
    unplayedMatchups = data;
    document.getElementById("roundNumber").innerHTML = unplayedMatchups['round'];
    roundToPost["tournamentName"] = unplayedMatchups['tournamentName'];
    roundToPost["round"] = unplayedMatchups['round'];
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
        roundToPost['matchupsResults'].push(playedMatchup);
        playedMatchups['matchupsResults'].push(playedMatchup);

        // remove from unplayed matchups
        let matchupIndex;
        unplayedMatchups['matchups'].filter(function(item, index)
            {
                matchupIndex = index;
                return item.teamOneName === teamOneName;
            });

        unplayedMatchups['matchups'].splice(matchupText, 1);


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

        if (unplayedMatchups['matchups'].length === 0)
            document.getElementById("sendRound").style.visibility = 'visible';
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
    document.getElementById("sendRound").style.visibility = 'hidden';
    postRound();

}

function postRound()
{
    fetch("tournamentViewerServlet", {
        method: 'POST',
        headers: { "content-type": "application/json" },
        body: JSON.stringify(roundToPost)
    })
    .then(res => {
        if (res.ok) {
            return res.text();
        }
        else
            return "status code: 400";
    })
    .then((text) => alert(text))
    .catch((error) => alert(error))
}

