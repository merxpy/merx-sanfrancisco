define(function () {
    exports = {};
    exports.InformeInventario = function () {
        $("#modal").load("vistas/reporte_inventario.html", function () {
            $("#modal-inventario").modal();
            SelectMarcas();
            ImprimirReporte();
        });
    };

    let SelectMarcas = function () {
        $.ajax({
            url: "marcasControlador",
            type: "post",
            data: {accion: 'select'},
            success: function (response) {
                for (var x = 0; x < response.length; x++) {
                    var mar = $('select[name="id_marca"]');
                    var option = document.createElement("option");
                    option.setAttribute("value", response[x].id_marca);
                    option.innerHTML = response[x].nombre_marca;
                    $(mar).append(option);
                }
            },
            error: function (response, jqxhr, error) {
                toastr.error(error, ", contacte con el desarrollador");
            }
        });

        $('select[name="id_marca"]').select2({
            allowClear: true,
            placeholder: "Seleccione una marca",
            dropdownParent: $('select[name="id_marca"]').parent(),
            width: '100%'
        });
    };

    let ImprimirReporte = function () {
        $("button[type='submit']").on('click', function (e) {
            e.preventDefault();
            $.post('stockControlador', {
                accion: "sxm",
                id_marca: $('select[name="id_marca"]').val(),
                estado: $('select[name="estado"]').val()
            }).done((response) => {
                if (response === 0) {
                    toastr.info("No se encuentran registros con esos paramentros. Por favor verifiquelos");
                } else {
                    var newwindow = window.open("inventario?precio=" + $('select[name="precio"]').val() + "&id_marca=" + $('select[name="id_marca"]').val() + "&estado=" + $('select[name="estado"]').val(), "", "width=1200,height=780");
                    return newwindow;
                }
            }).fail((response, jqxhr, error) => {
                toastr.error(error, " contacte con el desarrollador.");
            });
        });
    };

    return exports;
});