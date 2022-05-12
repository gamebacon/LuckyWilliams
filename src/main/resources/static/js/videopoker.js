

const cardElements = [
    document.getElementById('card1'),
    document.getElementById('card2'),
    document.getElementById('card3'),
    document.getElementById('card4'),
    document.getElementById('card5')
];

const cardSelectorLabels = [
    document.getElementById('keep-1'),
    document.getElementById('keep-2'),
    document.getElementById('keep-3'),
    document.getElementById('keep-4'),
    document.getElementById('keep-5')
];

const winText = document.getElementById('videopoker-win-text')
const balanceInput = document.getElementById('videopoker-balance')
const handResult = document.getElementById('videopoker-hand-result')
const betInput = document.getElementById('videopoker-bet-input')
const soundSwitch = document.getElementById('videopoker-sound-button')
const drawButton = document.getElementById('videopoker-draw')
const drawButtonLabel = document.getElementById('videopoker-button-label')


const suits = [ 'S', 'D', 'C', 'H' ];

let results;


let busy = false;

soundSwitch.checked = true;

 function requestDraw() {

    if(busy)
        return;

    setBusy(true)

     betInput.disabled = true;

    if(results) {
        finishGame();
    } else {
        requestNewGame();
    }

}

function finishGame() {

    const cards = results['cards'];

    for(let i = 0; i < 5; i++) {
        //if(cardSelectors[i].checked) {
         //   cards[i]['keep'] = true;

        if(!cards[i]['keep']){
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
            JSON.stringify(result)
            //console.log("Finish: " + JSON.stringify(result))
            /*
            for(let i = 0; i < 5; i++) {
                console.log(i + " Value: " + result['cards'][i]['value'])
            }
             */
            setTimeout(function () { draw(result, true) }, 1000)
        },
        error: function (err) {
            console.log("Error: " + err)
            setBusy(false)
            resetValues()
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
  "bet": betInput.value,
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
            JSON.stringify(result)
            //console.log("New: " + JSON.stringify(result))

            /*
            for(let i = 0; i < 5; i++) {
                console.log(i + " Value: " + result['cards'][i]['value'])
            }
             */

            validateRequest(result)
        },
        error: function (err) {
            console.log("Error: " + err)
            setBusy(false)
        }
    })
}

function validateRequest(result) {
    const {withdrawResult, sessionId, winAmount, cards} = result
    const {successful, balanceLeft} = withdrawResult;

    if(successful) {
        balanceInput.value = balanceLeft;
        resetValues()
        setTimeout( function(){
            draw(result, false)
        }, 0)
    }  else {
        playSoundAsync("/sound/lose.mp3", .1)
        setBusy(false)
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


    if(reset) {
        finish()
    } else {
        drawButtonLabel.innerText = "Finish"
    }

    setBusy(false)
}

function resetValues() {
    handResult.innerHTML = "";
    winText.value = "";
    betInput.disabled = true;
    unSelect()
    showBack()
    results = null;
}

function finish() {
    drawButtonLabel.innerText = "New game";
    handResult.innerHTML = results['handResult'];
    winText.value = results['winAmount']
    balanceInput.value = results['withdrawResult']['balanceLeft'];
    betInput.disabled = false;

    if(results['winAmount'] > 0) {
        playSoundAsync("/sound/win.mp3")
    } else {
        playSoundAsync("/sound/lose.mp3")
    }

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
        cardSelectorLabels[i].innerText = "";
    }
}

function showBack() {
    for(let i = 0; i < 5; i++) {
        cardElements[i].className = "back";
    }
}


function playSoundAsync(path, volume = .3) {

     if(!soundSwitch.checked)
         return;

    sound = new Audio(path)
    sound.volume = volume
    sound.play()
}


function check(index) {
     if(results && !busy) {
         const card = results['cards'][index];
         card['keep'] = !card['keep']
         cardSelectorLabels[index].innerText = card['keep'] ? "KEEP" : "";
     }
}

function setBusy(_busy) {
     busy = _busy;

     drawButton.disabled = busy;
}


