// window.onload = () => {getPrize()}
let prizesList =
    [
        {
            "placeNumber": 3,
            "placeName": "third",
            "prizeValue": 120
        }
    ];


function createPrizeClicked()
{
    let placeNumber = document.getElementById("placeNumber").value;
    let placeName = document.getElementById("placeName").value;
    let prizeValue = document.getElementById("prizeValue").value;

    if (validateCreatePrize(placeNumber, placeName, prizeValue))
    {
        window.alert("ok");
        let prizeAmount = 0;
        let prizePercentage = 0;
        if (document.getElementById("prizeAmountRadio").checked)
            prizeAmount = prizeValue;
        else
            prizePercentage = prizeValue;

        const prize = 
        {
            "placeNumber": placeNumber,
            "placeName": placeName,
            "prizeAmount": prizeAmount,
            "prizePercentage": prizePercentage
        };

        // postPrize(prize);

        clearFields();
    }
}

function getPrize()
{
    fetch("prizeServlet", {
        method: 'GET'
    })
    .then(res => {return res.json()})
    .then((data) => {console.log(data)})
    .catch(() => {
        alert('There has been a problem with your fetch operation');
    })
}

function validateCreatePrize(placeNumber, placeName, prizeValue)
{
    let stringRegex= /^[a-zA-Z]+$/;

    if (placeNumber == "")
    {        
        alert("Missing place number");
        return false;
    }

    if (placeNumber < 1)
    {
        alert("Invalid, place number should be greater then 1!");
        return false;
    }

    if (isNaN(placeNumber))
    {
        alert("Invalid place number, numbers only!");
        return false;
    }
    
    if (placeName == "")
    {
        alert("Missing name");
        return false;
    }

    if (!stringRegex.test(placeName))
    {
        alert("Invalid name, letters only!");
        return false;
    }

    // prize amount selected
    if (document.getElementById("prizeAmountRadio").checked)
    {
        if (prizeValue == 0)
        {
            alert("Invalid prize amount, must be greater than 0!");
            return false;
        }
            
        if (isNaN(prizeValue))
        {
            alert("Invalid prize amount, numbers only!");
            return false;
        }    
    }

    // prize percentage selected
    else if (document.getElementById("prizePercentageRadio").checked)
    {
        if (!isNaN(prizeValue))
        {
            alert("Invalid prize percentage, numbers only!");
            return false;
        }
        if (prizeValue < 0 || prizeValue > 100)
        {
            alert("Invalid prize percentage, must be between 0 and 100!");
            return false;
        }        
    }

    return true;
}

function clearFields()
{
    document.getElementById("placeNumber").value ="";
    document.getElementById("placeName").value = "";
    document.getElementById("prizeValue").value ="";
}

function postPrize(prize)
{
    fetch("prizeServlet", {
        method: 'POST',
        headers: { 'content-type': 'application/json' }, // Explicitly posting JSON
        body: JSON.stringify(prize) // JSON.stringify(object) utility function is used to transform a JavaScript object into a JSON string. body option accepts a string but not an object.
    })
    .then(res => {return res.json()})
    .catch(() => {
        alert('There has been a problem with your fetch operation:');
    })
}

