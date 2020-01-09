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
    console.log(escape(newPath));
}