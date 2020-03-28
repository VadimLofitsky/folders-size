function markUpTheHeaviest() {
    var table = $("#filesShow");
    var filesRows = $$(".files", table);

    var sum = 0;
    var sumLimit = 80;

    filesRows.toArray().every(row => {
        var el = $(".file-size-ratio", row);
        var val = parseFloat(el.innerText.replace(",", "."));
        var sumAbove = sum + val;

        if(sumAbove > sumLimit)
            if(Math.abs(sumLimit-sum) < Math.abs(sumAbove-sumLimit))
                return false;

        sum = sumAbove;

        el.classList.add("files-heaviest");
        return true;
    });
}