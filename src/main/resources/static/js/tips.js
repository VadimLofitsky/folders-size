function placeTips(container) {
    var tips = $$(".tip", container);

    tips.forEach(function(el) {
        var dest = $(el.dataset.dest);
        placeTip(el, dest, el.dataset.faNum);
    });
}

function placeTip(element, dest, faNum) {
    faNum = parseInt(faNum);

    var destRect = dest.getBoundingClientRect();

    element.style.top = destRect.bottom + 10 + "px";
    var elRect = dest.getBoundingClientRect();

    var ownFA = $$("[class*='fa-']", element)[faNum-1];

    var xDiff = getElementCenter(dest).x - getElementCenter(ownFA).x;
    var left = (element.getBoundingClientRect().left + xDiff);
    left = left<0 ? 0 : left;

    element.style.left = left + "px";
}

function getElementCenter(element) {
    var rect = element.getBoundingClientRect();

    return {
        x: rect.left + rect.width/2,
        y: rect.top + rect.height/2
    };
}