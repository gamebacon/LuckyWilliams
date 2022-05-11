
const WHEEL_ELEMENTS = [
    document.getElementById("slot-wheel-1"),
    document.getElementById("slot-wheel-2"),
    document.getElementById("slot-wheel-3"),
]

//firefox block audio?
const coinSound = new Audio("/sound/coin.mp3");
coinSound.volume = .1

const loseSound = new Audio("/sound/lose.mp3");
loseSound.volume = .20

const winSound = new Audio("/sound/win.mp3");

const intervals = [WHEEL_ELEMENTS.length]

let totalSpins = []
let wheelResults = []
let maxSpins = 0;
let busy = false;

const spinButton = $('#slots-spin');
const betInput = $('#bet-input');
const fastMode = $('#fast-mode-button');

const useSound = $('#slot-sound-button');
useSound.prop('checked', true)

const balanceLabel = $('#slot-balance')
const winLabel = $('#slot-win-text')
let _winAmount = 0;


function requestSpin() {


    if(busy)
        return;

    busy = true;

    const input = {
            bet: betInput.val(),
        }

        $.ajax({
            url: "/games/slots/spin",
            type: "POST",
            data: input,
            dataType: 'json',
            success: function (result) {
                //console.log(JSON.stringify(result));
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
        winLabel.val("0")
        spin(result)
    } else {
        alert("Not enough money.")
        playSoundAsync("/sound/lose.mp3", .1)
        busy = false
    }
}


function setIcon(element, num) {
    element.className = `tile-${num}`;
}

function spin(result) {

    spinButton.prop("disabled", true)
    betInput.prop("disabled", true)
    fastMode.prop("disabled", true)


    totalSpins = [0, 0, 0]
    wheelResults = result['wheels'];
    maxSpins = rand(15)

    for(let i = 0; i < WHEEL_ELEMENTS.length; i++)
        startIntervalls(i)
}

function startIntervalls(wheelIndex) {

    const maxSpin = fastMode.prop('checked') ? 0 : 8;
    intervals[wheelIndex] = setInterval(spinOnce, fastMode.prop('checked') ? 120 : 200, wheelIndex, maxSpin);
}

function spinOnce(wheelIndex, maxSpin) {
    let num = rand(9);
    let wheel = WHEEL_ELEMENTS[wheelIndex];

    if(wheelIndex === 2)
        playSoundAsync("/sound/spin.mp3", .2)

    if(totalSpins[wheelIndex] >= maxSpin + (wheelIndex + 1) * (fastMode.prop('checked') ? 1 : 5)) {
        num = wheelResults[wheelIndex]
        clearInterval(intervals[wheelIndex])

        if(!fastMode.prop('checked'))
            playSoundAsync("/sound/coin.mp3", .1)

        if(wheelIndex === 2) {
            finish()
        }
    }

    setIcon(wheel, num)
    wheel.innerText = num.toString();
    totalSpins[wheelIndex]++;
}

function finish() {

    if(_winAmount > 0) {
        winLabel.val(_winAmount)
        playSoundAsync("/sound/win.mp3")
    } else if (!fastMode.prop('checked')){
        playSoundAsync("/sound/lose.mp3", .1)
    }

    _winAmount = 0;

    spinButton.prop("disabled", false)
    betInput.prop("disabled", false)
    fastMode.prop("disabled", false)

    busy = false;
}

$(window).on("beforeunload", function () {

    if(!busy)
        return;

    return "A game is in progress are you sure?";

},);

function playSoundAsync(path, volume = .3) {

    if(!useSound.prop('checked'))
        return;

    sound = new Audio(path)
    sound.volume = volume
    sound.play()
}

function rand(max) {
    return Math.floor(Math.random() * max)
}