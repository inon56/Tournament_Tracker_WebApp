
function createPlayerClicked()
{
    let firstName =  document.getElementById("firstName").value;
    let lastName =  document.getElementById("lastName").value;
    let email = document.getElementById("email").value;
    let cellphone = document.getElementById("cellphone").value;

    if (validateCreatePlayer(firstName, lastName, email, cellphone))
    {
        const player = {
            "firstName" : firstName,
            "lastName" : lastName,
            "emailAddress" : email,
            "cellphoneNumber" : cellphone
        };

        postPlayer(player);

        clearPlayerFields();
    }
}


// change here the argument to player and all the rest inside
function validateCreatePlayer(firstName, lastName, email, cellphone)
{
    let stringRegex = /^[a-zA-Z]+$/;

    if (!stringRegex.test(firstName))
    {
        alert("Invalid firstName, letters only");
        return false;
    }

    if (!stringRegex.test(lastName))
    {
        alert("Invalid lastName, letters only");
        return false;
    }

    // validate email
    if (!(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(email)))
    {
        alert("Invalid Email address");
        return false;
    }

    if (isNaN(cellphone))
    {
        alert("Invalid cellphone, numbers only");
        return false;
    }

    return true;
}

function clearPlayerFields()
{
    document.getElementById("firstName").value = "";
    document.getElementById("lastName").value = "";
    document.getElementById("email").value = "";
    document.getElementById("cellphone").value = "";
}

function postPlayer(data)
{
    fetch("personServlet", {
        method: 'POST',
        headers: { 'content-type': 'application/json' }, // Explicitly posting JSON
        body: JSON.stringify(data) // JSON.stringify(object) utility function is used to transform a JavaScript object into a JSON string. body option accepts a string but not an object.
    })
    .then(res => {return res.text()})
    .then((text) => alert(text))
    .catch(() => {alert('There has been a problem with your fetch operation:')})
}