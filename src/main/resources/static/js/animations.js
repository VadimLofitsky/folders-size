function makeBodyWaiting(turnOn) {
    var bodyClassList = document.body.classList;

    if (turnOn) {
        bodyClassList.add("waiting");
    } else {
        bodyClassList.remove("waiting");
    }
}

function balanceScaleAnimate(animate) {
    var els = document.querySelectorAll("#logo>*[class*='balance-scale']");

    if (animate) {
        els.forEach(function (el) {
            el.classList.remove("balance-scale-hidden");
            el.classList.add("fade-in-out");
        });
    } else {
        els.forEach(function (el) {
            el.classList.add("balance-scale-hidden");
            el.classList.remove("fade-in-out");
        })
    }
}

function logoHide(hideLogo) {
    var logoClassList = document.querySelector(".logo").classList;

    if (hideLogo) {
        logoClassList.add("hidden");
    } else {
        logoClassList.remove("hidden");
    }
}