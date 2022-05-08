
const WHEEL_ELEMENTS = [
    document.getElementById("slot-wheel-1"),
    document.getElementById("slot-wheel-2"),
    document.getElementById("slot-wheel-3"),
]

//firefox block audio?
const coinSound = new Audio("/sound/coin.mp3");
coinSound.volume = .20

const loseSound = new Audio("/sound/spin.mp3");
const winSound = new Audio("/sound/spin.mp3");

const intervals = [WHEEL_ELEMENTS.length]

let totalSpins = []
let wheelResults = []
let maxSpins = 0;
let isSpinning = false;

const spinButton = $('#slots-spin');
const betInput = $('#bet-input');

const balanceLabel = $('#slot-balance')
const winLabel = $('#slot-win-text')
let _winAmount = 0;


init();

function init() {
    WHEEL_ELEMENTS.forEach(e => setIcon(e, rand(9)))
}


function requestSpin() {

        if(isSpinning) {
            return;
        }


    const input = {
            bet: betInput.val(),
        }

        $.ajax({
            url: "/games/slots/spin",
            type: "POST",
            data: input,
            dataType: 'json',
            success: function (result) {
                console.log(JSON.stringify(result));
                validateRequest(result)
            },
            error: function (err) {
                console.log("Error: " + err)
            }
        });
}

function validateRequest(result) {
    const {withdrawResult, winAmount, wheels} = result
    const {successful, balanceLeft} = withdrawResult;

    if(successful) {
        balanceLabel.val(balanceLeft)
        _winAmount = winAmount;
        spin(result)
    } else {
        alert("Not enough funds!!")
    }
}


function setIcon(element, num) {
    element.className = `tile-${num}`;
}

function spin(result) {

    isSpinning = true;

    spinButton.prop("disabled", true)
    betInput.prop("disabled", true)


    totalSpins = [0, 0, 0]
    wheelResults = result['wheels'];
    maxSpins = rand(15)

    for(let i = 0; i < WHEEL_ELEMENTS.length; i++)
        startIntervalls(i)
}

function startIntervalls(wheelIndex) {
    const maxSpin = 10;
    intervals[wheelIndex] = setInterval(spinOnce, 200, wheelIndex, maxSpin);
}

function spinOnce(wheelIndex, maxSpin) {
    let num = rand(9);
    let wheel = WHEEL_ELEMENTS[wheelIndex];

    if(wheelIndex === 2)
        playSoundAsync("/sound/spin.mp3")

    if(totalSpins[wheelIndex] >= maxSpin + (wheelIndex + 1) * 5) {
        num = wheelResults[wheelIndex]
        clearInterval(intervals[wheelIndex])
        coinSound.play();

        if(wheelIndex === 2) {
            finish()
        }
    }

    setIcon(wheel, num)
    wheel.innerText = num.toString();
    totalSpins[wheelIndex]++;
}

function finish() {
    isSpinning = false;

    if(_winAmount > 0)
        winLabel.val(_winAmount)


    _winAmount = 0;
    spinButton.prop("disabled", false)
    betInput.prop("disabled", false)
}

function playSoundAsync(path) {
    sound = new Audio(path)
    sound.volume = .3
    sound.play()
}

function rand(max) {
    return Math.floor(Math.random() * max)
}