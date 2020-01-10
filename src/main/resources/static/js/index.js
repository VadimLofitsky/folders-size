document.addEventListener("DOMContentLoaded", pageInit)

function pageInit() {
    document.body.onclick = onBodyClick;
    console.log("Init");
}

function onBodyClick(clickEvent) {
    console.log("onClick");
    var element = window.event.srcElement;
    if (element.tagName != "TD") {
        return false;
    }

    var parent = element.parentElement;
    var path = parent.dataset.path;
    if (typeof path == "undefined") {
        return false;
    }

    getNewPage(path);
}

function getNewPage(newPath) {
    // https://stackoverflow.com/questions/34319709/how-to-send-an-http-request-with-a-header-parameter
    // https://learn.javascript.ru/ajax-xmlhttprequest

    var xhr = new XMLHttpRequest();
    xhr.open( "GET", "/", true);
    xhr.setRequestHeader("folders-size-path", newPath);
    xhr.send(null);

    xhr.onreadystatechange = function() { // (3)
        if (xhr.readyState != 4) return;

        // button.innerHTML = 'Готово!';

        if (xhr.status != 200) {
            console.log(xhr.status + ': ' + xhr.statusText);
        } else {
            document.body.parentElement.innerHTML = xhr.responseText;
            // console.log(xhr.responseText);
        }
    }
}