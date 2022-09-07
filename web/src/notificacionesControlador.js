define(function () {
    const exports = {};
    exports.ListarNotificaciones = function () {
        $.ajax({
            url: 'notificacionesControlador',
            type: 'POST',
            data: {accion: 'listar'},
            success: function (response) {
                if (response.length) {
                    $(".dropdown-toggle.count-info span").html(response.length);
                    $(".dropdown-menu.dropdown-alert").html("");
                    for (var i = 0; i < response.length; i++) {
                        $(".dropdown-menu.dropdown-alert").append(`<li>
                                                                <a href="#">
                                                                    <div style="font-size: 12px;">
                                                                        ${response[i].mensaje}
                                                                    </div>
                                                                </a>
                                                            </li>`);
                    }
                } else {
                    $(".dropdown-toggle.count-info span").html("");
                    $(".dropdown-menu.dropdown-alert").html("");
                    $(".dropdown-menu.dropdown-alert").append(`<li>
                                                            <a>
                                                                <div style="font-size: 12px;">
                                                                   Sin notificaciones.
                                                                </div>
                                                            </a>
                                                        </li>`);
                }
            },
            error: function (response, jqxhr, error) {
                console.log(`${error}, no se pudo obtener las notificaciones`);
            },
            complete: setTimeout(function () {
                exports.ListarNotificaciones();
            }, 5000),
            timeout: 2000
        });
    };
    return exports;
});

