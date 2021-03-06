var tipAnimationsFinished;
var tipsNumber;
var isIntroFinished;

function intro() {
    var tipsContainer = $("#tips");

    tipsContainer.addEventListener("animationstart", tipsAnimationStartHandler);
    tipsContainer.addEventListener("animationend", tipsAnimationEndHandler);

    document.body.addEventListener("keypress", interruptIntro);
    document.body.addEventListener("click", interruptIntro);
}

function interruptIntro(ev) {
    $("#tips").classList.add("tips-animated-hide");

    removeBodyKeyAndClickListeners();
}

function removeBodyKeyAndClickListeners() {
    document.body.removeEventListener("keypress", interruptIntro);
    document.body.removeEventListener("click", interruptIntro);
}

function tipsAnimationStartHandler(ev) {
    if(ev.animationName === "tip-show") {
        var tipsContainer = $("#tips");
        placeTips(tipsContainer);
        tipsContainer.removeEventListener("animationstart", tipsAnimationStartHandler);
    }
}

function tipsAnimationEndHandler(ev) {
    if(typeof tipAnimationsFinished === 'undefined') {
        tipAnimationsFinished = 0;
        tipsNumber = $$(".tip", $("#tips")).length;
    }

    var tipsContainer = $("#tips");

    if(ev.animationName === "tip-show") {
        var nextTip = $$("#tips>.tip.tip-animated")[tipAnimationsFinished++].nextElementSibling;
        if(nextTip != null) {
            nextTip.classList.add("tip-animated");
        }
    }

    if(tipAnimationsFinished === tipsNumber) {
        tipsContainer.classList.add("tips-animated-hide");
        removeBodyKeyAndClickListeners();   // to remove body event listeners on keypress and click
    }

    if(ev.animationName === "tips-hide") {
        tipsContainer.parentElement.removeChild(tipsContainer);
        isIntroFinished = true;
    }
}