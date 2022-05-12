

const cardElements = [
    document.getElementById('card1'),
    document.getElementById('card2'),
    document.getElementById('card3'),
    document.getElementById('card4'),
    document.getElementById('card5')
];

const cardSelectors = [
    document.getElementById('card-select-1'),
    document.getElementById('card-select-2'),
    document.getElementById('card-select-3'),
    document.getElementById('card-select-4'),
    document.getElementById('card-select-5')
];


const suits = [ 'S', 'D', 'C', 'H' ];

let results;


let busy = false;

function requestDraw() {

    if(busy)
        return;

    busy = true;

    if(results) {
        finishGame();
    } else {
        requestNewGame();
    }

}

function finishGame() {

    const cards = results['cards'];

    for(let i = 0; i < 5; i++) {
        if(cardSelectors[i].checked) {
            cards[i]['keep'] = true;
            console.log("Keep: " + cards[i])
        } else {
            cardElements[i].className = "back";
        }
    }

    $.ajax({
        url: "/games/videopoker/draw",
        type: "POST",
        data: JSON.stringify(results),
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (result) {
            console.log("Finish: " + JSON.stringify(result))
            setTimeout(function () { draw(result, true) }, 1000)
        },
        error: function (err) {
            console.log("Error: " + err)
            busy = false;
            results = null;
        }
    })

}

function requestNewGame() {

const session = {
  "withdrawResult": {
    "successful": false,
    "balanceLeft": -1 
  },
  "winAmount": -1,
  "bet": 2,
  "cards": [],
  "sessionId": -1
};


    $.ajax({
        url: "/games/videopoker/draw",
        type: "POST",
        data: JSON.stringify(session),
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (result) {
            console.log("New: " + JSON.stringify(result))
            validateRequest(result)
        },
        error: function (err) {
            console.log("Error: " + err)
            busy = false;
        }
    })
}

function validateRequest(result) {
    const {withdrawResult, sessionId, winAmount, cards} = result



    const {successful, balanceLeft} = withdrawResult;

    if(successful) {
        unSelect()
        showBack()
        setTimeout( function(){
            draw(result, false)
        }, 0)
    }  else {
        busy = false;
    }

}

function draw(result, reset) {
    results = result;

    const cards = results['cards'];

    for(let i = 0; i < 5; i++) {
        if(!cards[i].keep) {
            setTimeout( function(){
                displayCard(cards[i], i)
            }, 150 * i)
        }

    }

    busy = false;

    if(reset)
        results = null;
}

function displayCard(card, pos) {
    const suit = suits[card['suit']];
    const value = card['value'];
    cardElements[pos].className = suit + (value + 2);
    playSoundAsync("/sound/deal3.wav")
}

function unSelect() {
    for(let i = 0; i < 5; i++) {
        cardSelectors[i].checked = false;
    }
}

function showBack() {
    for(let i = 0; i < 5; i++) {
        cardElements[i].className = "back";

    }
}


function playSoundAsync(path, volume = .3) {
    sound = new Audio(path)
    sound.volume = volume
    sound.play()
}

