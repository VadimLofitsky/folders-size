document.addEventListener("DOMContentLoaded", pageInit);

function pageInit() {
    document.body.onclick = onBodyClick;
}

function exitButtonMouseEnter() {
    var element = window.event.srcElement;
    element.classList.remove("far");
    element.classList.add("fas");
}

function exitButtonMouseLeave() {
    var element = window.event.srcElement;
    element.classList.remove("fas");
    element.classList.add("far");
}

function exitButtonClick() {
    if (confirm("Really quit?")) {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "/exit", true);
        xhr.send(null);

        document.body.classList.add("waiting");
        document.querySelector("table#filesShow").classList.add("anim-exiting");
        window.setTimeout(exitThis, 1000);
    }
}

function exitThis() {
    window.close();
}

function onBodyClick(clickEvent) {
    var element = window.event.srcElement;
    if (element.tagName != "TD") {
        return false;
    }

    var parent = element.parentElement;
    var path = parent.dataset.path;
    if (typeof path == "undefined") {
        return false;
    }

    waitingMode(true);
    getNewTable(path);
}

function getNewTable(newPath) {
    // https://stackoverflow.com/questions/34319709/how-to-send-an-http-request-with-a-header-parameter
    // https://learn.javascript.ru/ajax-xmlhttprequest

    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/", true);
    xhr.setRequestHeader("folders-size-path", encodeURI(newPath));
    xhr.send(null);

    xhr.onreadystatechange = function () {
        if (xhr.readyState != 4) return;

        if (xhr.status != 200) {
            console.log(xhr.status + ': ' + xhr.statusText);
        } else {
            var element = document.createElement("html");
            element.innerHTML = xhr.responseText;
            document.querySelector("table#filesShow").outerHTML = element.querySelector("table#filesShow").outerHTML;
            waitingMode(false);  // not necessary. Table contents replaced
        }
    }
}


function waitingMode(turnOn) {
    makeBodyWaiting(turnOn);
    balanceScaleAnimate(turnOn);
    logoHide(turnOn);
}

function makeBodyWaiting(turnOn) {
    var bodyClassList = document.body.classList;

    if (turnOn) {
        bodyClassList.add("waiting");
    } else {
        bodyClassList.remove("waiting");
    }
}

function balanceScaleAnimate(animate) {
    var els = document.querySelectorAll("table#filesShow td#logo>*[class*='balance-scale']");

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