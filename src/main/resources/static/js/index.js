var osPathSeparator;
var isIntroFinished;
var isCalculated;

document.addEventListener("DOMContentLoaded", (ev) => {
    defineGlobalUtilsFunctions();
    pageInit();
    intro();
});

function pageInit() {
    $("table#filesShow").onclick = onTableClick;
    $("#levelUp").onclick = onLevelupClick;

    osPathSeparator = $("#header").dataset.pathSeparator;

    fragmentizePath();
    alignLegend();
}

function onTableClick(clickEvent) {
    if(!isIntroFinished) return false;

    var element = clickEvent.target.closest("td");
    if(element.tagName === "TD") {
        var path = element.parentElement.dataset.path;
        if (typeof path !== "undefined") {
            getNewContent(path);
        }
    }

    return false;
}

function onLevelupClick(clickEvent) {
    if(!isIntroFinished) return false;

    if(clickEvent.target.closest("#levelUp") !== null) {
        var path = $("#levelUp").dataset.path;
        if (typeof path !== "undefined") {
            getNewContent(path);
        }
    }

    return false;
}

function getNewContent(newPath, calculateSize) {
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
            isCalculated = xhr.getResponseHeader("folders-size-isCalculated").toLowerCase() === "true";

            var element = document.createElement("html");
            element.innerHTML = xhr.responseText;
            $("#header").outerHTML = $("#header", element).outerHTML;
            $("table#filesShow").outerHTML = $("table#filesShow", element).outerHTML;

            waitingMode(false);
            pageInit();

            if(isCalculated)
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