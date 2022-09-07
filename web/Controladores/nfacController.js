function factura() {

    spinneron();
    $("#inicio").load("vistas/numero_facturas.html", {}, function () {
        spinneroff();
        $(".active").removeClass("active");
        $(".config").parent().find("ul.nav").removeClass("nav nav-second-level collapse").addClass("nav nav-second-level collapse in");
        $(".config").parent().addClass("active");
        ListarNfacturas();
        usarFactura();
    });
}

function NuevaFactura() {
    $("#modal").load("vistas/modal_nfac.html", {}, function () {
        num();
//        selectSucursales();
//        $("#sucursal").select2({
//            dropdownParent: $(".parent-suc"),
//            placeholder: "Seleccione una sucursal",
//            allowClear: true
//        });
        selectTimbrados(0);
        $("#Modal-nfac").modal();
        agregarNfacturas(0, 0);

        $("#Modal-nfac").on('hidden.bs.modal', () => {
            window.history.back();
        });
    });
}

function ListarNfacturas() {
    $("#table-nfac").DataTable({
        destroy: true,
        ajax: {
            type: "post",
            url: "nfacturasControlador",
            data: {accion: "listar"}
        },
        columns: [
            {data: "id_nfacturas",
                render: (data, type, full, meta) => {
                    return cero(data);
                }
            },
            {data: "del_nfac"},
            {data: "al_nfac"},
            {data: "estado_nfac",
                render: function (data, type, full, meta) {
                    if (data == 'N') {
                        return '<label class="label label-default">Ninguno</label>';
                    } else if (data == 'U') {
                        return '<label class="label label-info">En uso</label>';
                    } else if (data == 'V') {
                        return '<label class="label label-danger">Vencido</label>';
                    }
                }
            },
            {data: "numero_tim"},
            {data: null,
                render: function (data, type, full, meta) {
                    return '<div class="btn-group"><a class="btn btn-default btn-xs det" href=#/configuracion/nfactura/editar?_id=' + data.id_nfacturas + '><i class="far fa-edit"> Editar</i></a><a class="btn btn-primary btn-xs use" data-id=' + data.id_nfacturas + '><i class="fa fa-star"> Usar</i></a><a class="btn btn-info btn-xs clo" data-id="' + data.id_nfacturas + '"><i class="fa fa-ban"> Inhabilitar</i></a></div>';
                }
            }
        ]
    });
}

function agregarNfacturas(accion, id) {
    $("#form-nfac").submit(function (e) {
        e.preventDefault();
        if (parseInt($("#al").val()) > parseInt($("#del").val())) {
            $.post("nfacturasControlador", {
                accion: accion,
                id_nfacturas: id,
                del_nfac: $("#del").val(),
                al_nfac: $("#al").val(),
                id_timbrado: $("#tim").val(),
                id_sucursal: $("#sucursal").val()
            }).done(function (response) {
                if (response == 1) {
                    toastr.success("GUARDADO!");
                    ListarNfacturas();
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
}

function editarNdfacturas(id) {
    $("#modal").load("vistas/modal_nfac.html", {}, function () {
        nfac();
//        selectSucursales();
        $("#Modal-nfac").modal();
        $.post("nfacturasControlador", {
            accion: "buscar",
            id_nfactura: id
        }).done(function (response) {
            selectTimbrados(response.id_timbrado);
            $("#del").val(response.del_nfac);
            $("#al").val(response.al_nfac);
            $("#sucursal").val(response.id_sucursal);
            setTimeout(() => {
//                $("#sucursal").select2({
//                    allowClear: true,
//                    placeholder: "Seleccione una sucursal",
//                    dropdownParent: $(".parent-suc")
//                });
            }, 250);
        }).fail(function (response, jqxhr, error) {
            toastr.error(error);
        });
        agregarNfacturas(1, id);
        $("#Modal-nfac").on('hidden.bs.modal', () => {
            window.history.back();
        });
    });
}

function selectTimbrados(id) {
    $.post("timbradoControlador", {
        accion: "select",
        id: id
    }).done(function (response) {
        var select = document.getElementById("tim");
        for (var i = 0; i < response.length; i++) {
            var option = document.createElement("option");
            option.setAttribute("value", response[i].id_timbrado);
            option.innerHTML = response[i].numero_tim;

            if (response[i].id_timbrado === id) {
                option.setAttribute("selected", true);
            }

            select.appendChild(option);
        }
    }).fail(function (response, jqxhr, error) {
        toastr.error(error);
    });
    $("#tim").select2({
        placeholder: "Seleccione un timbrado",
        dropdownParent: $(".parent"),
        width: "100%",
        allowClear: true
    });
}

function usarFactura() {
    $("#table-nfac").on('click', 'tbody .use', function () {
        var t = this.getAttribute("data-id");
        $.post("nfacturasControlador", {
            accion: "usar",
            id_nfacturas: t
        }).done(function (response) {
            if (response == "1") {
                ListarNfacturas();
                toastr.info("En uso");
            } else {
                toastr.error("Error Inesperado");
            }
        }).fail(function (response, jqxhr, error) {
            toastr.error(error);
        });
    });
}