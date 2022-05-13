
const red = [1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36];
const board = [0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 36, 11, 30, 8, 23, 10, 5, 24, 16, 33, 1, 20, 14, 31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26]
const numberElements = [];
const bettingBoard = document.getElementById('betting-board')
const rouletteWheel = document.getElementById('roulette-wheel')
const rouletteArrow = document.getElementById('roulette-arrow')

const soundButton = document.getElementById("roulette-sound-button");
const infoButton = document.getElementById("roulette-info");
const winLabel = document.getElementById("roulette-win-text");
const betText = document.getElementById("roulette-bet-input-show");

const oneDeg = 9.73
let results = null;

const maxBet = 30;

let busy = false;

soundButton.checked = true;
idle();
buildBettingBoard()

function idle () {
    rouletteArrow.style.cssText = 'animation: wheelRotate 2.5s linear infinite';
}

function stopIdle() {
    rouletteArrow.style.cssText = 'animation: none';
}

function canBet(canBet) {
    const bet_inputs = document.getElementsByClassName('roulette-bet-input');

    for(let i = 0; i < bet_inputs.length; i++) {
        bet_inputs[i].disabled = !canBet;
    }

}

function getBets() {
    const betsElements = document.getElementsByClassName('roulette-bet-input')
    const bets = []
    for(let i = 0; i < betsElements.length; i++) {
        let val = betsElements[i].value;
        bets[i] = (val.length > 0 ? parseInt(val) : 0);

        if(bets[i] > maxBet)  {
            betsElements[i].value = "";
            bets[i] = 0;
        }
    }
    return bets;
}

function requestStartGame() {

    if(busy)
        return;

    busy = true;

    canBet(false)

    const session = {
        "winningNumber": -1,
        "winAmount": -1,
        "bets": getBets(),
        "withdrawResult": {
            "successful": false,
            "balanceLeft": -1
        },
    };

    $.ajax({
        url: "/games/roulette/start",
        type: 'POST',
        data: JSON.stringify(session),
        dataType: 'json',
        contentType: "application/json; charset=utf-8",
        success: function (result) {
            validateRequest(result)
            //console.log("Success: " + JSON.stringify(result))
        },
        error: function (result) {
            //console.log("Error: " + JSON.stringify(result))
            resetRoulette()
        }
    })

}

function getBetSum() {
    const bets = getBets();
    let sum = 0;

    for(let i = 0; i < bets.length; i++) {
        sum += bets[i];
    }

    return sum;
}

function validateRequest(result) {
    const {withdrawResult, winAmount, winningNumber} = result
    const {successful, balanceLeft} = withdrawResult;

    if(successful) {
        //balanceInput.value = balanceLeft;
        //resetValues()
        betText.value = getBetSum();
        setTimeout( function(){
            startGame(result)
        }, 500)
    }  else {
        playSoundAsync("/sound/lose.mp3", .1)
        //betInput.disabled = false;
        //setBusy(false)
        resetRoulette()
    }

}


function startGame(_results) {
    results = _results;
    spinRoulette()
    numberElements[results.winningNumber].classList.add("roulette-number-win")

    winLabel.value = results.winAmount;

    if(results.winAmount > 0) {
        playSoundAsync("/sound/win.mp3", .1)
    } else {
        playSoundAsync("/sound/lose.mp3", .1)
    }

    setTimeout(function () {
        resetRoulette()
    }, 10_000)

}



function resetRoulette() {
    idle()
    rouletteArrow.style.transform = `rotate(0deg)`
    if(results)
        numberElements[results.winningNumber].classList.remove("roulette-number-win");

    betText.value = 0;
    winLabel.value = 0;

    removeBets()
    results = null;
    busy = false;
    canBet(true);
}

function removeBets() {
    const bet_inputs = document.getElementsByClassName('roulette-bet-input');

    for(let i = 0; i < bet_inputs.length; i++) {
        bet_inputs[i].value = "";
    }
}



function spinRoulette() {

    let deg = -1;

    for(let i = 0; i < board.length; i++) {
        if(board[i] == results.winningNumber) {
            deg = (i * oneDeg) + 362;
        }
    }

    //console.log(`Degree: ${deg}, WinNum: ${results.winningNumber}`)
    stopIdle()
    rouletteArrow.style.cssText = `transition-duration: 3s; transform: rotate(${deg}deg);`
}


function buildBettingBoard() {
    let num = 0;

    for(let i = 0; i < 13; i++) {
        const row = document.createElement("div");
        row.classList.add("row", "roulette-row")

        for(let j = 0; j < 3; j++) {
            const column = document.createElement("div");
            column.classList.add("col", "roulette-num")
            numberElements[num] = column;
            const num_label = document.createElement("label")
            const bet_input = document.createElement("input")
            bet_input.classList.add("roulette-bet-input")
            bet_input.setAttribute('id', `bet-input-${num}`)


            bet_input.type = 'number'
            bet_input.min = '0'
            bet_input.max = "" + maxBet;

            if(i == 0) {

                if(j == 1) {
                    num_label.innerText = num;

                    column.appendChild(num_label)
                    column.appendChild(bet_input)

                    column.classList.add("green");
                    row.appendChild(column)
                    num++;
                }

                continue;
            }


            num_label.innerText = num;

            if(red.includes(num)) {
                column.classList.add("red")
            } else {
                column.classList.add("black")
            }

            column.appendChild(num_label)
            column.appendChild(bet_input)
            row.appendChild(column)
            num++;
        }

        bettingBoard.appendChild(row);
    }


}

function playSoundAsync(path, volume = .3) {

    if(!soundButton.checked)
       return;

    sound = new Audio(path)
    sound.volume = volume
    sound.play()
}