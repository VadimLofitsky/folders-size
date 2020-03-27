function hideTips() {
    var tipsContainer = $("#tips");

    tipsContainer.addEventListener("animationstart", (ev) => {
        if(ev.animationName === "tip-show")
            placeTips(tipsContainer);
    }, false);

    var tipAnimationsFinished = 0;
    var tipsNumber = $$(".tip", tipsContainer).length;

    tipsContainer.addEventListener("animationend", (ev) => {
        if(ev.animationName === "tip-show") {
            ++tipAnimationsFinished;
            }

        if (tipAnimationsFinished == tipsNumber) {
            tipsContainer.classList.add("tips-animated-hide");
        }

        if(ev.animationName === "tips-hide") {
            tipsContainer.classList.add("hidden");
        }
    }, false);
}

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

function getHorizLeftOffset(parent, child) {
    return child.getBoundingClientRect().left - parent.getBoundingClientRect().left;
}
function getHorizCenterOffset(parent, child) {
    return getElementCenter(child).x - getElementCenter(parent).x;
}
function getHorizRightOffset(parent, child) {
    return parent.getBoundingClientRect().right - child.getBoundingClientRect().right;
}

