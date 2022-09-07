function spinneron() {
    $("#inicio").addClass("inner-spinner-sh")
    $("body").append(
            '<div id="spinner" class="container">' +
            '<div class="row inner-spinner">' +
            '<div class="spiner-example">' +
            '<div class="sk-spinner sk-spinner-three-bounce">' +
            '<div class="sk-bounce1"></div>' +
            '<div class="sk-bounce2"></div>' +
            '<div class="sk-bounce3"></div>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>' +
            '</div>');
}

function spinneroff() {
    $("#inicio").removeClass("inner-spinner-sh");
    $("#spinner").remove();
}

