document.addEventListener("DOMContentLoaded", pageInit);

function pageInit() {
    document.body.onclick = onBodyClick;

    defineGlobalUtilFunctions();

    window.osPathSeparator = "";
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

    waitingMode(true);
    getNewContent(path);
}

function getNewContent(newPath) {
    // https://stackoverflow.com/questions/34319709/how-to-send-an-http-request-with-a-header-parameter
    // https://learn.javascript.ru/ajax-xmlhttprequest

    if (osPathSeparator === "")
        osPathSeparator = $("#header").dataset.pathSeparator;

    if (newPath.charAt(newPath.length - 1) === ":")
        newPath += osPathSeparator;

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
            $("#header").outerHTML = $("#header", element).outerHTML;
            $("table#filesShow").outerHTML = $("table#filesShow", element).outerHTML;

            waitingMode(false);
            fragmentizePath();
        }
    }
}

function waitingMode(turnOn) {
    makeBodyWaiting(turnOn);
    balanceScaleAnimate(turnOn);
    logoHide(turnOn);
}