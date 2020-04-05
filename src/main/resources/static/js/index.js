document.addEventListener("DOMContentLoaded", (ev) => {
    defineGlobalUtilsFunctions();
    pageInit();
    intro();
});

function pageInit() {
    $("table#filesShow").onclick = onTableClick;
    $("#levelUp").onclick = onLevelupClick;

    window.osPathSeparator = $("#header").dataset.pathSeparator;

    fragmentizePath();
    turnOnFragmPathSplash();
    alignLegend();
}

function onTableClick(clickEvent) {
    if(!window.isIntroFinished) return false;

    var element = window.event.srcElement;
    if(element.tagName === "TD") {
        var path = element.parentElement.dataset.path;
        if (typeof path !== "undefined") {
            getNewContent(path);
        }
    }

    return false;
}

function onLevelupClick(clickEvent) {
    if(!window.isIntroFinished) return false;

    var element = window.event.srcElement;
    if(element.id === "levelUp") {
        var path = element.dataset.path;
        if (typeof path !== "undefined") {
            getNewContent(path);
        }
    }

    return false;
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
            pageInit();

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

function alignLegend() {
    var legendSpans = $$("#legend>span");

    $$("table#filesShow tr:first-child>td").forEach((cell, index) => {
        legendSpans[index].style.width = cell.getBoundingClientRect().width + "px";
    });

    var headerRect = $("#header").getBoundingClientRect();
    $("table#filesShow").style.top = (headerRect.y + headerRect.height) + "px";
}