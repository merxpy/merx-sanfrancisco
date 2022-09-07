define(['timbradosControlador', 'cajasControlador'], function (timbrado, caja) {
    var exports = {};
    var tabla;
    exports.Seriales = function () {
        spinneron();
        $("#inicio").load("vistas/numero_facturas.html", {}, function () {
            spinneroff();
            $(".active").removeClass("active");
            $(".config").parent().find("ul.nav").removeClass("nav nav-second-level collapse").addClass("nav nav-second-level collapse in");
            $(".config").parent().addClass("active");
            exports.ListarSeriales();
        });
    };

    exports.ListarSeriales = function () {
        tabla = $("#tabla-seriales").DataTable({
            destroy: true,
            ajax: {
                url: "nfacturasControlador",
                type: "post",
                data: {accion: "listar"}
            },
            columns: [
                {data: "id_nfactura",
                    render: (data, type, full, meta) => {
                        return cero(data);
                    }
                },
                {data: "del_nfac"},
                {data: "al_nfac"},
                {data: "estado_nfac",
                    render: function (data, type, full, meta) {
                        if (data === 'U') {
                            return '<label class="label label-info">En uso</label>';
                        } else if (data === 'V') {
                            return '<label class="label label-danger">Vencido</label>';
                        }
                    }
                },
                {data: "numero_tim"},
                {data: "codigo_caja"},
                {data: null,
                    render: function (data, type, full, meta) {
                        return `<div class="btn-group">
                                    <a class="btn btn-default btn-xs det" href=#/configuracion/nfactura/editar?_id=${data.id_nfactura}><i class="far fa-edit"> Editar</i></a>
                                </div>`;
                    }
                }
            ]
        });
    };

    exports.BuscarNrosSeriales = function (id) {
        $("#modal").load("vistas/modal_nfac.html", function () {
            $("#Modal-nfac").modal();
            $.post("nfacturasControlador", {
                accion: "buscar",
                id_nfactura: id
            }).done(function (response) {
                $('input[name="del_nfac"]').val(response.del_nfac);
                $('input[name="al_nfac"]').val(response.al_nfac);
                timbrado.SelectTimbrado();
                caja.SelectCaja(response.id_caja);
                setTimeout(() => {
                    $('select[name="id_timbrado"]').val(response.id_timbrado);
                    $('select[name="id_caja"]').val(response.id_caja);
                }, 180);
                exports.AgregarNrosSeriales(id);
                $("#Modal-nfac").on("hidden.bs.modal", function () {
                    window.history.back();
                });
            }).fail(function (response, jqxhr, error) {
                toastr.error(error);
            }).always(function (response, jqxhr, error) {
                if (jqxhr === 'success') {
                    setTimeout(() => {
                        $('select[name="id_timbrado"]').select2({
                            allowClear: true,
                            dropdownParent: $('select[name="id_timbrado"]').parent().parent()
                        });
                        $('select[name="id_caja"]').select2({
                            allowClear: true,
                            dropdownParent: $('select[name="id_caja"]').parent().parent()
                        });
                    }, 250);
                }
            });
        });
    };

    exports.AgregarNrosSeriales = function (id) {
        $("#form-nfac").submit(function (e) {
            e.preventDefault();
            if (parseInt($('input[name="al_nfac"]').val()) > parseInt($('input[name="del_nfac"]').val())) {
                var fd = ConvertFormToJSON(this);
                console.log(fd);
                fd.id_nfactura = id;
                $.post("nfacturasControlador", {
                    accion: "insertar",
                    datos: JSON.stringify(fd)
                }).done(function (response) {
                    if (response == 1) {
                        toastr.success("GUARDADO!");
                        $("#Modal-nfac").modal("hide");
                    } else {
                        toastr.error("Error Inesperado");
                    }
                }).fail(function (response, jqxhr, error) {
                    toastr.error(error);
                });
            } else {
                $("#del-error").addClass("glyphicon glyphicon-exclamation-sign form-control-feedback text-danger");
                $("#al-error").addClass("glyphicon glyphicon-exclamation-sign form-control-feedback text-danger");
                $("#nfac-error").removeClass("hide");

                $(".nfac").on("focus", function () {
                    $("#del-error").removeClass("glyphicon glyphicon-exclamation-sign form-control-feedback text-danger");
                    $("#al-error").removeClass("glyphicon glyphicon-exclamation-sign form-control-feedback text-danger");
                    $("#nfac-error").addClass("hide");
                });
            }
        });
    };
    return exports;
});

