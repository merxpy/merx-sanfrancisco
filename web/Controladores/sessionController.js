$(document).ready(function () {
    configuraciones();
    // Set idle time
    $("#example").on('click', function () {
        var config;
        if ($("#example").prop("checked")) {
            toastr.clear();
            config = {
                active: 1
            };
            $.post('sessionConfig', {accion: 0, config: JSON.stringify(config)}, function (response) {
                toastr.success("Cierre de session automatico activado!");
                $.idleTimer(36000);
            }).fail(function (response, jqxhr, error) {
                toastr.error(error);
            });
        } else {
            toastr.clear();
            config = {
                active: 0
            };
            $.post('sessionConfig', {accion: 0, config: JSON.stringify(config)}, function (response) {
                toastr.success("Cierre de session automatico desactivado!");
                $.idleTimer('destroy');
            }).fail(function (response, jqxhr, error) {
                toastr.error(error);
            });

        }
    });

});

$(document).on("idle.idleTimer", function (event, elem, obj) {
    window.location = "salir";
});

$(document).on("active.idleTimer", function (event, elem, obj, triggerevent) {
    // function you want to fire when the user becomes active again
    toastr.clear();
    $('.custom-alert').fadeOut();
    toastr.success('Great that you decided to move a mouse.', 'You are back. ');
});

function configuraciones() {
    $.getJSON("config.json", function (response) {
        if (response.active == 1) {
            $("#example").attr("checked", true);
            $.idleTimer(36000);
        } else {
            $.idleTimer('destroy');
        }
    });
}