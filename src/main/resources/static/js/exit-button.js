function exitButtonMouseEnter(ev) {
    var element = ev.target.closest(".far");
    element.classList.remove("far");
    element.classList.add("fas");
}

function exitButtonMouseLeave() {
    var element = ev.target.closest(".fas");
    element.classList.remove("fas");
    element.classList.add("far");
}

function exitButtonClick() {
    if (confirm("Really wanna quit?")) {
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