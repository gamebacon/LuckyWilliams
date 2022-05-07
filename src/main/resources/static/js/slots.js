





const SLOTS_SPIN_BUTTON = document.getElementById("slotsSpin");


async function spin(result) {

    const WHEEL_ELEMENTS = [
        document.getElementById("slot-wheel-1"),
        document.getElementById("slot-wheel-2"),
        document.getElementById("slot-wheel-3"),
    ]

    console.log(WHEEL_ELEMENTS.length)
    console.log(WHEEL_ELEMENTS[0])
    console.log(WHEEL_ELEMENTS[1])
    console.log(WHEEL_ELEMENTS[2])
    console.log("Result: " + result);

    for(let i = 0; i < 10; i++) {
        for(let j = 0; j < 3; j++) {

            let num = rand(10);
            let wheel = WHEEL_ELEMENTS[j];
            wheel.innerText = num.toString();
        }

        await delay(80)

    }

}


function delay(ms) {
    return new Promise( resolve => {
        setTimeout(() => resolve(''), ms)
    })
}


function rand(max) {
    return Math.floor(Math.random() * max)
}
