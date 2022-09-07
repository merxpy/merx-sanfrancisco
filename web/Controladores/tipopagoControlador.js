define(function () {
    var exports = {};

    exports.TipoPago = function () {
        spinneron();
        $("#inicio").load("vistas/tipopago.html", {}, function () {
            spinneroff();
            $(".active").removeClass("active");
            $(".sistema").parent().find("ul.nav").removeClass("nav nav-second-level collapse").addClass("nav nav-second-level collapse in");
            $(".sistema").parent().addClass("active");
            exports.ListarTipoPago();
        });
    };

    exports.ListarTipoPago = function () {
        $("#tabla-tipopago").DataTable({
            destroy: true,
            ajax: {
                type: "post",
                url: "tipopagoControlador",
                data: {accion: "listar"},
            },
            columns: [
                {data: "id_tipo_pago"},
                {data: "descripcion"},
                {data: "abr"},
                {data: null,
                    render: (data, type, full, meta) => {
                        return `<div class="btn-group">
                                    <a href="#/tipopago/editar?id=${data.id_tipo_pago}" class="btn btn-primary btn-xs"><i class="far fa-edit"> Editar</i></a>
                                    <a href="#/tipopago/eliminar?id=${data.id_tipo_pago}" class="btn btn-danger btn-xs"><i class="fa fa-ban"> Eliminar</i></a>
                                </div>`;
                    }
                }
            ]
        });
    };

    exports.BuscarTipoPago = function (id) {
        $("#modal").load("vistas/modal_tipopago.html", function () {
            $("#modal-tipopago").modal();
            $.post("tipopagoControlador", {
                accion: "buscar",
                id: id
            }).done((response) => {
                if (!$.isEmptyObject(response)) {
                    $('input[name="descripcion"]').val(response.descripcion);
                    $('input[name="abr"]').val(response.abr);
                    exports.AgregarTipoPago(id);
                }
            }).fail((response, jqxhr, error) => {
                toastr.error(error + " Contacte con el desarrollador");
            });
        });
    };

    exports.AgregarTipoPago = function (id) {
        $("#form-tipopago").on('submit', function (e) {
            e.preventDefault();
            var fd = ConvertFormToJSON(this);
            fd.id_tipo_pago = id;
            $.post("tipopagoControlador", {
                accion: "insertar",
                datos: JSON.stringify(fd)
            }).done((response) => {
                if (response) {
                    toastr.success("¡Guardado!");
                    $("#modal-tipopago").modal("hide");
                    setTimeout(() => {
                        window.location = "#/tipopago";
                    }, 300);
                } else {
                    toastr.error("No se ha podido completar la operación. Contacte con el desarrollador");
                }
            }).fail((response, jqxhr, error) => {
                toastr.error(error + " Contacte con el desarrollador");
            });
        });
    };

    exports.EliminarTipoPago = function (id) {
        $.post("tipopagoControlador", {
            accion: "eliminar",
            id: id
        }).done((response) => {
            if (response) {
                toastr.success("¡Eliminado!");
                setTimeout(() => {
                    window.location = "#/tipopago";
                }, 300);
            } else {
                toatr.error("No se ha podido completar la operación. Contacte con el desarrollador");
            }
        }).fail((response, jqxhr, error) => {
            toastr.error(error + " Contacte con el desarrollador");
        });
    };

    exports.SelectTipoPago = function () {
        $.post("tipopagControlador", {
            accion: "select"
        }).done((response) => {
            if (!$.isEmptyObject(response)) {
                for (var i = 0; i < response.length; i++) {
                    $("#select-tipopago").append(`<option value="${response[i].id_tipo_pago}">${response[i].descripcion}</option>`)
                }
            } else {
                toastr.error("Aún no se ha registrado ningún tipo de pago");
            }
        }).fail((response, jqxhr, error) => {
            toastr.error(error + " Contacte con el desarrollador");
        });
    };

    return exports;
});

