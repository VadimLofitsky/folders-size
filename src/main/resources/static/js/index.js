document.addEventListener("DOMContentLoaded", pageInit);

function pageInit() {
    document.body.onclick = onBodyClick;
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

        // button.innerHTML = 'Готово!';

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
    var classList = document.querySelector(".logo").classList;
    if (turnOn) {
        classList.add("rotating");
    } else {
        classList.remove("rotating");
    }
}