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
        document.querySelector("table#filesShow").classList.add("exiting");
        window.setTimeout(exitThis, 2000);
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

    rotateLogo(true);
    getNewTable(path);
}

function getNewTable(newPath) {
    // https://stackoverflow.com/questions/34319709/how-to-send-an-http-request-with-a-header-parameter
    // https://learn.javascript.ru/ajax-xmlhttprequest

    var xhr = new XMLHttpRequest();
    xhr.open( "GET", "/", true);
    xhr.setRequestHeader("folders-size-path", encodeURI(newPath));
    xhr.send(null);

    xhr.onreadystatechange = function() {
        if (xhr.readyState != 4) return;

        if (xhr.status != 200) {
            console.log(xhr.status + ': ' + xhr.statusText);
        } else {
            var element = document.createElement("html");
            element.innerHTML = xhr.responseText;
            document.querySelector("table#filesShow").outerHTML = element.querySelector("table#filesShow").outerHTML;
            rotateLogo(false);  // not necessary. Table contents replaced
        }
    }
}

function rotateLogo(turnOn) {
    var bodyClassList = document.body.classList;
    var logoClassList = document.querySelector(".logo").classList;

    if (turnOn) {
        bodyClassList.add("waiting");
        logoClassList.add("rotating");
    } else {
        bodyClassList.remove("waiting");
        logoClassList.remove("rotating");
    }
}