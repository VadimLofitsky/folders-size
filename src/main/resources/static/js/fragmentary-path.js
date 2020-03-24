function fragmentizePath() {
    var path = getCurrentPath();
    var pathSeparator = $("#header").dataset.pathSeparator;

    var parts = path.split(pathSeparator);
    var html = "";
    var pathAccumulator = "";

    parts.forEach(function (part, index) {
        if (part === "") return;

        if (index === 0) {
            pathAccumulator = part;
            html = generateFragmentHtml(part, pathAccumulator);
        } else {
            pathAccumulator += pathSeparator + part;
            html += pathSeparator + generateFragmentHtml(part, pathAccumulator);
        }
    });

    $("#fragment-path").innerHTML = html;
}

function getCurrentPath() {
    return document.querySelector("#header").dataset.path;
}

function generateFragmentHtml(part, accumulator) {
    var html = "<span data-this-path='" + accumulator + "' class='path-fragment'>";
    html += part;
    html += "</span>";
    return html;
}