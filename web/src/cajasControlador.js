define(['sucursalesControlador'], function (sucursal) {
    var exports = {};
    var tabla;
    exports.Cajas = function () {
        $("#inicio").load("vistas/cajas.html", function () {
            $(".level").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
            $(".active").removeClass("active");
            $(".item").parent().addClass("active");
            exports.ListarCajas();
            exports.EliminarCaja();
        });
    };

    exports.ListarCajas = function () {
        tabla = $("#tabla-caja").DataTable({
            destroy: true,
            ajax: {
                url: "cajasControlador",
                type: "post",
                data: {accion: "listar"}
            },
            columns: [
                {data: "codigo_caja"},
                {data: "nombre_sucursal"},
                {data: null,
                    render: (data, type, full, meta) => {
                        return `<div class="btn-group">
                                    <a class="btn btn-primary btn-xs" href="#/sistema/cajas/editar?_id=${data.id_caja}"><i class="far fa-edit"> Editar</i></a>
                                    <a class="btn btn-info btn-xs delete" data-id="${data.id_caja}"><i class="fa fa-trash"> Eliminar</i></a>
                                </div>`;
                    }
                }
            ]
        });
    };

    exports.BuscarCaja = function (id) {
        $("#modal").load("vistas/insertCajas.html", function () {
            $("#Modal-caja").modal();
            exports.AgregarCaja(id);
            $.post("cajasControlador", {
                accion: "buscar",
                id_caja: id
            }).done((response) => {
                $('input[name="codigo_caja"]').val(response.codigo_caja);
                setTimeout(() => {
                    $('select[name="id_sucursal"]').val(response.id_sucursal);
                }, 250);
            }).fail((response, jqxhr, error) => {
                toastr.error(error + ", contacte con el desarrollador");
            }).always((response, jqxhr, error) => {
                if (jqxhr === 'success') {
                    sucursal.SelectSucursales();
                    setTimeout(() => {
                        $("#sucursal").select2({
                            allowClear: true,
                            dropdownParent: $("#sucursal").parent()
                        });
                    }, 250);
                }
            });
            $("#Modal-caja").on("hidden.bs.modal", function () {
                window.history.back();
            });
        });
    };

    exports.AgregarCaja = function (id) {
        $("#form-caja").on('submit', function (e) {
            e.preventDefault();
            var fd = ConvertFormToJSON(this);
            fd.id_caja = id;
            $.post("cajasControlador", {
                accion: "insertar",
                datos: JSON.stringify(fd)
            }).done((response) => {
                if (response) {
                    toastr.success("¡Guardado!");
                    $("#Modal-caja").modal("hide");
                }
            }).fail((response, jqxhr, error) => {
                toastr.error(error + ", error contacte con el desarrollador");
            });
        });
    };

    exports.EliminarCaja = function () {
        $("#tabla-caja tbody").on('click', 'a.delete', function () {
            var id = this.getAttribute("data-id");
            $.post("cajasControlador", {
                accion: 'eliminar',
                id_caja: id

            }).done((response) => {
                toastr.success("¡Eliminado!");
                tabla.ajax.reload();
            }).fail((response, jqxhr, error) => {
                toastr.error(error + ", contacte con el desarrollador");
            });
        });
    };

    exports.SelectCaja = function (id) {
        $.post("cajasControlador", {
            accion: "select",
            id_caja: id
        }).done((response) => {
            $.each(response, (i) => {
                var option = document.createElement("option");
                option.setAttribute("value", response[i].id_caja);
                option.innerHTML = response[i].codigo_caja;
                $('select[name="id_caja"]').append(option);
            });
        }).fail((response, jqxhr, error) => {
            toastr.error(error + ", contacte con el desarrollador");
        });
    };

    return exports;
});

