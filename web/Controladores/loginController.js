define(function () {
    var exports = {};
    exports.VerificarSession = function () {
        $.post("loginControlador", {
            accion: "session"
        }).done(function (response) {
            if (response === 0) {
                window.location = "login.jsp";
            }
        }).fail(function (response, jqxhr, error) {
            console.log("Internet error can't connect.");
        });
        setTimeout(exports.VerificarSession, 3000);
    };

    exports.LogoEmpresa = function () {
        $.post("empresaControlador", {
            accion: "mostrar"
        }).done((response) => {
            console.log(response);
            $("img").attr("src", response.logo_empresa);
        }).fail((response, jqxhr, error) => {

        });
    };
    return exports;
});




