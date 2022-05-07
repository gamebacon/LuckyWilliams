





const SLOTS_SPIN_BUTTON = document.getElementById("slotsSpin");

const WHEEL_ELEMENTS = [
    document.getElementById("slot-wheel-1"),
    document.getElementById("slot-wheel-2"),
    document.getElementById("slot-wheel-3"),
]

const intervals = [WHEEL_ELEMENTS.length]

async function spin(result) {

    console.log("Result: " + result);


    for(let i = 0; i < WHEEL_ELEMENTS.length; i++) {
        //intervals[i] = setInterval(spinWheel(WHEEL_ELEMENTS[i], i), 100);
        startIntervalls(WHEEL_ELEMENTS[i], i);
    }

}

function startIntervalls(wheel, index) {

    intervals[index] = setInterval(spinOnce(wheel), 100);

}

function spinOnce(wheel) {

    let num = rand(10);
    console.log("spin once! " + num)
    wheel.innerText = num.toString();
}








function delay(ms) {
    return new Promise( resolve => {
        setTimeout(() => resolve(''), ms)
    })
}


function rand(max) {
    return Math.floor(Math.random() * max)
}
