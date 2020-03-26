function hideTips() {
    var tipsContainer = $("#tips");

    placeTips(tipsContainer);

    window.setTimeout(function () {
        tipsContainer.classList.add("hidden");
    }, 3000);
}

function placeTips(container) {
    var tips = $$(".tip", container);

    tips.forEach(function(el) {
        var dest = $(el.dataset.dest);
        placeTip(el, dest);
    });
}

function placeTip(element, dest) {
}