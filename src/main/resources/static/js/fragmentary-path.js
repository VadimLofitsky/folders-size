function fragmentizePath() {
    var path = $("#header").dataset.path;

    var parts = path.split(osPathSeparator);
    var html = "";

    parts.forEach(function (part, index) {
        if (part === "") return;

        if (index === 0) {
            html = generateFragmentHtml(part);
        } else {
            html += osPathSeparator + generateFragmentHtml(part);
        }
    });

    $("#fragment-path").innerHTML = html;
}

function generateFragmentHtml(part) {
    var html = "<span data-this-path='" + part + "' class='path-fragment'";
    html += " onmouseenter='pathFragmentMouseEnter()'";
    html += " onmouseleave='pathFragmentMouseLeave()'";
    html += " onclick='pathFragmentClick()'>";
    html += part;
    html += "</span>";
    return html;
}

function pathFragmentMouseEnter() {
    var ev = window.event;
    selectPathFragments(true, ev.target);
    return true;
}

function pathFragmentMouseLeave() {
    var ev = window.event;
    selectPathFragments(false, ev.target);
    return true;
}

function pathFragmentClick() {
    var ev = window.event;
    var el = ev.target.closest(".path-fragment");

    while (el.nextSibling != null)
        el.nextSibling.remove();

    var newPath = el.parentElement.innerText;
    getNewContent(newPath);

    ev.cancelBubble = false;
    return false;
}

function selectPathFragments(toSelect, element) {
    var parent = $("#fragment-path");

    if (toSelect) {
        var el = element.nextElementSibling;
        while (el != null && el.classList.contains("path-fragment-selected")) {
            el.classList.remove("path-fragment-selected");
            el = el.nextElementSibling;
        }

        el = element.previousElementSibling;
        while (el != null && !el.classList.contains("path-fragment-selected")) {
            el.classList.add("path-fragment-selected");
            el = el.previousElementSibling;
        }
    } else {
        $$(".path-fragment-selected", parent).forEach(function (el) {
            el.classList.remove("path-fragment-selected");
        });
    }
}

function turnOnFragmPathSplash() {
    window.setTimeout(() => {
        window.setInterval(() => {
            $("#fragment-path").classList.add("bg-animated");

            $("#fragment-path").addEventListener("animationend", (ev) => {
                if(ev.animationName === "bg-frames")
                    $("#fragment-path").classList.remove("bg-animated");
            }, {once: true});
        }, 15000);
    }, 7000);
}