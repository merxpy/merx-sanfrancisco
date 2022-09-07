function ffive() {
    $(".ffive").on('keypress', function () {
        var l = $(this).val();
        if (l > 45) {
            return false;
        }
    });
}

function thundred() {
    $(".thundred").on('keypress', function () {
        var l = $(this).val();
        if (l > 200) {
            return false;
        }
    });
}