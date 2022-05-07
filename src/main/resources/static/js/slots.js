
const WHEEL_ELEMENTS = [
    document.getElementById("slot-wheel-1"),
    document.getElementById("slot-wheel-2"),
    document.getElementById("slot-wheel-3"),
]

//firefox block audio?
const coinSound = new Audio("/sound/coin.mp3");
coinSound.volume = .25

const loseSound = new Audio("/sound/spin.mp3");
const winSound = new Audio("/sound/spin.mp3");

const intervals = [WHEEL_ELEMENTS.length]

const spins = [0, 0, 0]
let maxSpins = rand(15)

let _result = []

function spin(result) {
    _result = result;
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
        playSpinSound()
    if(spins[wheelIndex] >= maxSpin + (wheelIndex + 1) * 5) {
        num = _result[wheelIndex]
        clearInterval(intervals[wheelIndex])
        coinSound.play();
    }
    wheel.className = `tile-${num}`;
    wheel.innerText = num.toString();
    spins[wheelIndex]++;
}

function playSpinSound() {
    sound = new Audio("/sound/spin.mp3")
    sound.volume = .3
    sound.play()
}

function rand(max) {
    return Math.floor(Math.random() * max)
}