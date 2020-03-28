document.addEventListener("DOMContentLoaded", pageInit);

function pageInit() {
    defineGlobalUtilsFunctions();

    document.body.onclick = onBodyClick;

    window.osPathSeparator = $("#header").dataset.pathSeparator;

    fragmentizePath();
    turnOnFragmPathSplash();

    hideTips();
}

function onBodyClick(clickEvent) {
    var element = window.event.srcElement;
    if (element.tagName !== "TD") {
        return false;
    }
    var parent = element.parentElement;
    var path = parent.dataset.path;
    if (typeof path == "undefined") {
        return false;
    }

    getNewContent(path);
}

function getNewContent(newPath, calculateSize) {
    // https://stackoverflow.com/questions/34319709/how-to-send-an-http-request-with-a-header-parameter
    // https://learn.javascript.ru/ajax-xmlhttprequest

    waitingMode(true);

    if(calculateSize == null || typeof calculateSize !== 'boolean') calculateSize = false;

    if (newPath.charAt(newPath.length - 1) === ":")
        newPath += osPathSeparator;

    var xhr = new XMLHttpRequest();
    xhr.open("GET", "/", true);
    xhr.setRequestHeader("folders-size-path", encodeURI(newPath));
    xhr.setRequestHeader("folders-size-calculate-size", calculateSize);
    xhr.send(null);

    xhr.onreadystatechange = function () {
        if (xhr.readyState != 4) return;

        if (xhr.status != 200) {
            console.log(xhr.status + ': ' + xhr.statusText);
        } else {
            window.isCalculated = xhr.getResponseHeader("folders-size-isCalculated").toLowerCase() === "true";

            var element = document.createElement("html");
            element.innerHTML = xhr.responseText;
            $("#header").outerHTML = $("#header", element).outerHTML;
            $("table#filesShow").outerHTML = $("table#filesShow", element).outerHTML;

            waitingMode(false);
            fragmentizePath();

            if(window.isCalculated)
                markUpTheHeaviest();
        }
    }
}

function waitingMode(turnOn) {
    makeBodyWaiting(turnOn);
    balanceScaleAnimate(turnOn);
    logoHide(turnOn);
}

function calculateHere() {
    getNewContent($("#header").dataset.path, true);
}