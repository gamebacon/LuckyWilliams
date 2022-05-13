
const red = [1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36];
const board = [0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 36, 11, 30, 8, 23, 10, 5, 24, 16, 33, 1, 20, 14, 31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26]
const numberElements = [];
const bettingBoard = document.getElementById('betting-board')
const rouletteWheel = document.getElementById('roulette-wheel')
const rouletteArrow = document.getElementById('roulette-arrow')
const inp = document.getElementById('rol-in')
const oneDeg = 9.73
let winningNumber = -1;


let busy = false;

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

function startGame() {

    if(busy)
        return;

    busy = true;

    canBet(false)



    winningNumber = Math.floor(Math.random() * 37);

    spinRoulette2()

    numberElements[winningNumber].classList.add("roulette-number-win")
}


function resetRoulette() {
    rouletteArrow.style.transform = `rotate(0deg)`
    numberElements[winningNumber].classList.remove("roulette-number-win");
    canBet(true);
    winningNumber = null;
    removeBets()
    busy = false;
}

function removeBets() {
    const bet_inputs = document.getElementsByClassName('roulette-bet-input');

    for(let i = 0; i < bet_inputs.length; i++) {
        bet_inputs[i].value = "";
    }
}

function spinWithInput() {
    const value = inp.value;
    spinRoulette2(value)
}



function Test() {
    rouletteArrow.style.cssText = inp.value;//;'animation: wheelRotate 5s linear infinite;';
}


function spinRoulette2() {

    let deg = -1;

    for(let i = 0; i < board.length; i++) {
        if(board[i] == winningNumber) {
            deg = (i * oneDeg) + 362;
        }
    }

    console.log(`Degree: ${deg}, WinNum: ${winningNumber}`)
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
            bet_input.max = '10'

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