define(function () {
    var exports = {};
    exports.Declaracion = function () {
        $("#modal").load("vistas/declaraciones.html", function () {
            $("#modal-ddji").modal();
            PeriodoDeclaracion();
            $("select[name='tipo_info'").on('change', function () {
                PeriodoDeclaracion();
            });
            $("select[name='periodo'").on('change', function () {
                MesesDeclaracion();
            });
        });
    };

    let PeriodoDeclaracion = function () {
        $.post("declaracionesControlador", {
            libro: $("select[name='tipo_info'").val()
        }).done(function (response) {
            $('select[name="periodo"]').html("");
            $.each(response, function (i) {
                $('select[name="periodo"]').append(`<option value="${response[i].anho}">${response[i].anho}</option>`);
            });
            MesesDeclaracion();
        }).fail(function (response, jqxhr, error) {
            toastr.error(error + ", por favor contacte con el desarrollador");
        });
    };

    let MesesDeclaracion = function () {
        $.post("declaracionesControlador", {
            libro: "meses" + $("select[name='tipo_info'").val(),
            periodo: $("select[name='periodo'").val()
        }).done(function (response) {
            $('select[name="mes"]').html("");
            $.each(response, function (i) {
                $('select[name="mes"]').append(`<option value="${response[i].anho}">${response[i].mes}</option>`);
            });
        }).fail(function (response, jqxhr, error) {
            toastr.error(error + ", por favor contacte con el desarrollador");
        });
    };
    return exports;
});
