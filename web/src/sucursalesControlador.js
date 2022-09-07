define(() => {
    var exports = {};
    var tabla;
    exports.Sucursales = function () {
        spinneron();
        $("#inicio").load("vistas/sucursales.html", {}, function () {
            spinneroff();
            $(".active").removeClass("active");
            $("a[href='#/sistema/auditoria']").parent().find("ul.nav").removeClass("nav nav-second-level collapse in").addClass("nav nav-second-level collapse");
            $('a[href="#/sistema/auditoria"]').closest("ul").closest("li").addClass("active");
            exports.ListarSucursales();
            exports.EliminarSucursal();
        });
    };

    exports.BuscarSucursal = function (id) {
        $("#modal").load("vistas/modal_sucursal.html", {}, function () {
            $("#Modal-sucursal").modal();
            $.post("sucursalesControlador", {
                accion: "buscar",
                id_sucursal: id
            }).done((response) => {
                $('input[name="codigo_sucursal"]').val(response.codigo_sucursal);
                $('input[name="nombre_sucursal"]').val(response.nombre_sucursal);
                exports.AgregarSucursales(id);
                $("#Modal-sucursal").on("hidden.bs.modal", function () {
                    window.history.back();
                });
            }).fail((response, jqxhr, error) => {
                toastr.error(error + ", contacte con el desarrollador");
            });
        });
    };

    exports.ListarSucursales = function () {
        tabla = $("#table-suc").DataTable({
            "pageLength": 100,
            destroy: true,
            ajax: {
                type: "post",
                url: "sucursalesControlador",
                data: {accion: "listar"}
            },
            columns: [
                {data: "codigo_sucursal"},
                {data: "nombre_sucursal"},
                {data: null,
                    render: function (data, type, full, meta) {
                        return `<div class="btn-group">
                                    <a class="btn btn-primary btn-xs" href="#/sistema/sucursales/editar?_id=${data.id_sucursal}"><i class="far fa-edit"> Editar</i></a>
                                    <a class="btn btn-info btn-xs delete" data-id="${data.id_sucursal}"><i class="fa fa-trash"> Eliminar</i></a>
                                </div>`;
                    }
                }
            ]
        });
    };

    exports.AgregarSucursales = function (id) {
        $("#form-sucursal").submit(function (e) {
            e.preventDefault();
            $.post("sucursalesControlador",
                    {
                        accion: "insertar",
                        id_sucursal: id,
                        codigo_sucursal: $("#codigo").val(),
                        nombre_sucursal: $("#nombre").val()
                    })
                    .done(function (response) {
                        if (response) {
                            toastr.success("¡GUARDADO!");
                            $("#Modal-sucursal").modal("hide");
                        } else {
                            toastr.error("Error inesperado");
                        }
                    })
                    .fail(function (response, jqxhr, error) {
                        toastr.error(error);
                    });
        });
    };

    exports.SelectSucursales = function () {
        $.post("sucursalesControlador", {
            accion: "select"
        }).done(function (response) {
            if (!jQuery.isEmptyObject(response)) {
                for (var i = 0; i < response.length; i++) {
                    var s = document.getElementById("sucursal");
                    var option = document.createElement("option");
                    option.innerHTML = response[i].nombre_sucursal;
                    option.setAttribute("value", response[i].id_sucursal);
                    s.appendChild(option);
                }
            } else {
                toastr.error("Algo ha salido mal :(");
            }
        }).fail(function (response, jqxhr, error) {
            toastr.error(error);
        });
    };

    exports.EliminarSucursal = function () {
        $("#table-suc tbody").on('click', 'a.delete', function () {
            var id = this.getAttribute("data-id");
            $.post("sucursalesControlador", {
                accion: 'eliminar',
                id: id
            }).done((response) => {
                if (response) {
                    toastr.success("¡Eliminado!");
                    tabla.ajax.reload();
                } else {
                    toastr.warning("¡Imposible eliminar! Es posible que este registro se encuentre ligado a otro registro del sistema.");
                }
            }).fail((response, jqxhr, error) => {
                toastr.error(error + ", contacte con el desarrollador");
            });
        });
    };
    return exports;
});

