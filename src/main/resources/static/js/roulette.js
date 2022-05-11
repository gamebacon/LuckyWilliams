
const board = [0, 32, 15, 19, 4, 21, 2, 25, 17, 34, 6, 27, 13, 36, 11, 30, 8, 23, 10, 5, 24, 16, 33, 1, 20, 14, 31, 9, 22, 18, 29, 7, 28, 12, 35, 3, 26]
const rouletteWheel = document.getElementById('roulette-wheel')
const rouletteArrow = document.getElementById('roulette-arrow')
const oneDeg = 9.73


function resetRoulette() {
    rouletteArrow.style.transform = `rotate(0deg)`
}

function spinRoulette(deg = Math.random() * 360) {


    if(deg < 0) {
        deg = Math.random() * 360;
    }


    const inp = document.getElementById('rol-in')
    console.log("val: " + inp.getAttribute('value'));


    result = num = board[Math.floor(deg / oneDeg)];

    console.log(`deg: ${deg}, Num: ${num}`)

    rouletteArrow.style.transform = `rotate(${deg}deg)`

}