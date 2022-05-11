

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
    //sessionid & cards

    const cards = results['cards'];

    for(let i = 0; i < 5; i++) {
        cards[i]['keep'] = cardSelectors[i].checked;
    }

    $.ajax({
        url: "/games/videopoker/draw",
        type: "POST",
        data: JSON.stringify(results),
        contentType: "application/json; charset=utf-8",
        dataType: 'json',
        success: function (result) {

            console.log(JSON.stringify(result))

            console.log("SUCCESS BACK: " + result)
            //validateRequest(result)

        },
        error: function (err) {

            console.log("Error :(((((((((((((((( : " + err)
        }
    })
    console.log(JSON.stringify(results))

    results = null;
    busy = false;
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
            console.log(JSON.stringify(result))
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
        draw(result)
    }  else {
        busy = false;
    }

}

function draw(result) {
    busy = false;
    results = result;

    const cards = result['cards'];

    for(let i = 0; i < 5; i++) {
        setTimeout( function(){
            displayCard(cards[i], i) },
           100 * i
        )
    }

}

function displayCard(card, pos) {
    const suit = suits[card['suit']];
    const value = card['value'];
    cardElements[pos].className = suit + (value + 2);
    playSoundAsync("/sound/deal3.wav")
}


function playSoundAsync(path, volume = .3) {
/*
    if(!useSound.prop('checked'))
        return;
 */

    sound = new Audio(path)
    sound.volume = volume
    sound.play()
}

