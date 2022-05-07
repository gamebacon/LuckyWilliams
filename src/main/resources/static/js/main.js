

const yearE = document.getElementsByClassName("year");

const companyName = "Bingo Casino&trade;"
const companyNameElements = document.getElementsByClassName("companyName");


for (let i = 0; i < yearE.length; i++) {
    yearE.item(i).innerHTML = new Date().getFullYear()
}

for (let i = 0; i < companyNameElements.length; i++) {
    companyNameElements.item(i).innerHTML = companyName;
}
